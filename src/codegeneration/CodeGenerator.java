package src.codegeneration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import src.AST.SimpleNode;
import src.AST.ASTArgument;
import src.AST.ASTMethod;
import src.AST.ASTVar;
import src.AST.Node;
import src.AST.ProgramTreeConstants;

import src.semantic.Symbol;

public class CodeGenerator {

    private SimpleNode root;

    private String file_name;

    private PrintWriter output;

    private AtomicInteger count;

    private int counter;

    private int ifInstruction = 0;

    private int elseInstruction = 0;

    public CodeGenerator(SimpleNode root, String file_name) throws IOException {
        this.root = (SimpleNode) root.getChildren()[0];

        this.file_name = file_name;

        FileWriter filewriter = new FileWriter(file_name, false);
        BufferedWriter bufferedwriter = new BufferedWriter(filewriter);

        this.output = new PrintWriter(bufferedwriter);
        this.count = new AtomicInteger(0);

    }

    public void generateCode() {

        generateClassHeader();
        generateGlobals(root);
        generateStatic();
        generateMethods();
        output.close();

    }

    private void generateClassHeader() {
        output.println(".source " + this.file_name);
        output.println(".class public " + root.getName());
        output.println(".super java/lang/Object" + "\n");

    }

    private void generateGlobals(SimpleNode current_node) {

        for (int i = 0; i < current_node.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) current_node.jjtGetChild(i);

            if (child.getId() == ProgramTreeConstants.JJTVAR) {
                generateVar((ASTVar) child);
            }
        }
    }

    private void generateStatic() {
        // TODO
        // limit stack....
    }

    private void generateVar(ASTVar var) {
        String var_type = var.getType();
        String var_name = var.getName();
        String output_type = "";

        switch (var_type) {
        case "int":
            output_type = "I";
            break;
        case "int[]":
            output_type = "[I";
            break;
        case "boolean":
            output_type = "B";
            break;
        }

        output.println(".field public static " + var_name + " " + output_type);

    }

    private void generateMethods() {

        counter = 0;

        for (int i = 0; i < root.jjtGetNumChildren(); i++) {
            SimpleNode child_root = (SimpleNode) root.jjtGetChild(i);

            if (child_root.getId() == ProgramTreeConstants.JJTMAIN)
                generateMethod(child_root);

            if (child_root.getId() == ProgramTreeConstants.JJTMETHOD)
                generateMethod(child_root);

        }
    }

    private void generateMethod(SimpleNode method) {

        if (method.getId() == ProgramTreeConstants.JJTMAIN)
            generateMainMethodHeader(method);
        else {
            generateMethodHeader((ASTMethod) method);
        }

        generateBodyStub(method);

        generateMethodFooter(method);

    }

    private void generateMainMethodHeader(SimpleNode function_node) {
        output.println("\n.method public static main([Ljava/lang/String;)V");

    }

    private void generateMethodHeader(ASTMethod method) {
        output.print("\n.method public static " + method.getName());

        if (method.jjtGetNumChildren() == 0)
            output.print("()");
        else {
            output.print("(");
            for (int i = 0; i < method.jjtGetNumChildren(); i++) {
                SimpleNode child = (SimpleNode) method.jjtGetChild(i);

                if (child.getId() == ProgramTreeConstants.JJTARGUMENT)
                    generateArgument((ASTArgument) child);
            }
            output.print(")");
        }

        switch (method.getType()) {
        case "int":
            output.print("I\n");
            break;
        case "int[]":
            output.print("[I\n");
            break;
        case "boolean":
            output.print("B\n");
            break;
        default:
            output.print("V\n");
        }
    }

    private void generateArgument(ASTArgument argument) {

        switch (argument.getType()) {
        case "int":
            output.print("I");
            break;
        case "int[]":
            output.print("[I");
            break;
        case "boolean":
            output.print("B");
            break;
        }
    }

    private void generateMethodFooter(SimpleNode method) {
        String return_type = "";

        if (method.getId() != ProgramTreeConstants.JJTMAIN) {
            switch (method.getType()) {
            case "int":
                return_type = "i";
                break;
            case "boolean":
                return_type = "i"; // TODO: NOT CORRECT, DIFERENCIAR AQUI RETURNS BOOLEANOS
                break;
            case "int[]":
                return_type = "a";
                break;
            }
        }

        output.println("\t" + return_type + "return");
        output.println(".end method");
    }

    private void generateBodyStub(SimpleNode method) {
        for (int i = 0; i < method.jjtGetNumChildren(); i++) {
            SimpleNode method_child = (SimpleNode) method.jjtGetChild(i);
            generateBody(method_child);
        }

    }

    private void generateBody(SimpleNode node) {

        switch (node.getId()) {
        case ProgramTreeConstants.JJTVAR:
            break;
        case ProgramTreeConstants.JJTASSIGN:
            generateAssign(node);
            break;
        case ProgramTreeConstants.JJTIF:
            generateIfStatement(node);
            break;
        case ProgramTreeConstants.JJTELSE:
            generateElseStatement(node);
            break;
        case ProgramTreeConstants.JJTPERIOD:
            generateCall(node);
            break;
        case ProgramTreeConstants.JJTWHILE:
            break;
        }
    }

    private void generateCall(SimpleNode function_child) {

        String method_class = ((SimpleNode) function_child.jjtGetChild(0)).getNodeValue();
        SimpleNode call_node = (SimpleNode) function_child.jjtGetChild(1);

        generateCallArguments(call_node);
        generateCallInvoke(call_node, method_class);

    }

    private void generateCallArguments(SimpleNode method_node) {

        for (Node arg : method_node.getChildren()) {
            SimpleNode argument = (SimpleNode) arg;

            switch (argument.getReturnType()) {
            case INT:
                this.loadInt(argument.getNodeValue());
                break;
            case BOOLEAN:
                this.loadBoolean(argument.getNodeValue());
                break;
            case VOID:
                String name = argument.getNodeValue();
                if (root.getSymbols().hasSymbolWithNameLocal(name))
                    this.loadGlobalVariable(name);
                else
                    this.loadLocalVariable((SimpleNode) arg, name);
                break;
            case INT_ARRAY:
                break;
            default:
                break;
            }
        }
    }

    private void generateCallInvoke(SimpleNode method, String method_class) {
        String method_name, method_ret, method_arg = "";

        // Add the method path to the method name
        if (method_class == "this")
            method_name = this.root.getName() + "." + method.getName();
        else
            method_name = method_class + "." + method.getName();

        method_name = method_name.replace(".", "/");

        // Process arguments
        Vector<Symbol.Type> arg_types = new Vector<>();

        if (method.getChildren() != null) {
            for (Node arg : method.getChildren()) {
                SimpleNode argument = (SimpleNode) arg;
                switch (argument.getReturnType()) {
                case INT:
                    method_arg += "I";
                    arg_types.add(Symbol.Type.INT);
                    break;
                case BOOLEAN:
                    method_arg += "B";
                    arg_types.add(Symbol.Type.BOOLEAN);
                    break;
                case VOID:
                    // System.out.println(
                    //         argument.getSymbols().getSymbolWithName(argument.getNodeValue()) + " | " + argument.getNodeValue());
                    if (argument.getSymbols().getSymbolWithName(argument.getNodeValue()).getType() == Symbol.Type.INT) {
                        method_arg += "I";
                        arg_types.add(Symbol.Type.INT);
                    } else if (argument.getSymbols().getSymbolWithName(argument.getNodeValue())
                            .getType() == Symbol.Type.INT_ARRAY) {
                        method_arg += "[I";
                        arg_types.add(Symbol.Type.INT_ARRAY);
                    } else if (argument.getSymbols().getSymbolWithName(argument.getNodeValue())
                            .getType() == Symbol.Type.BOOLEAN) {
                        method_arg += "B";
                        arg_types.add(Symbol.Type.INT);
                    }
                    break;
                case INT_ARRAY:
                    method_arg += "[I";
                    arg_types.add(Symbol.Type.INT_ARRAY);
                    break;
                default:
                    break;
                }
            }
        }

        // TODO
        // Does not work then method is from another class. How do we know the type of the method?
        Symbol.Type method_return = root.getMethods().obtainMethod(method.getName(), arg_types).getType();

        switch (method_return) {
        case INT:
        case BOOLEAN:
            method_ret = "B";
            break;
        case INT_ARRAY:
            method_ret = "[I";
            break;
        case VOID:
            method_ret = "V";
            break;
        default:
            return;
        }

        output.println("\t" + "invokestatic " + method_name + "(" + method_arg + ")" + method_ret);

    }

    private void loadBoolean(String bool) {
        this.loadInt(bool);
    }

    private void loadInt(String v) {

        counter++;

        int value = Integer.parseInt(v);

        if ((value >= 0) && (value <= 5)) {
            output.println("iconst_" + value);
        } else if (value == -1) {
            output.println("iconst_m1");
        } else if (value > -129 && value < 128) {
            output.println("bipush " + value);
        } else if (value > -32769 && value < 32768) {
            output.println("sipush " + value);
        } else {
            output.println("ldc " + value);
        }
    }

    private String loadIntString(String v) {

        counter++;
        String generated_code = "";

        int value = Integer.parseInt(v);
        if ((value >= 0) && (value <= 5)) {
            generated_code += "\ticonst_" + value;
        } else if (value == -1) {
            generated_code += "\ticonst_m1";
        } else if (value > -129 && value < 128) {
            generated_code += "\tbipush " + value;
        } else if (value > -32769 && value < 32768) {
            generated_code += "\tsipush " + value;
        } else {
            generated_code += "\tldc " + value;
        }
        return generated_code;

    }

    private void loadLocalVariable(SimpleNode node, String name) {
        int index = node.getSymbolIndex(name);
        Symbol.Type var_t = node.getSymbols().getSymbolWithName(name).getType();
        String type, code;

        if (var_t == Symbol.Type.INT || var_t == Symbol.Type.BOOLEAN)
            type = "i";
        else
            type = "a";

        if (index <= 3)
            code = "load_";
        else
            code = "load ";

        output.println("\t" + type + code + index);
        if (index == 0) {
            output.println();
        }
    }

    private void storeLocalVariable(SimpleNode node, String name) {
        int index = node.getSymbolIndex(name);
        Symbol.Type var_t = node.getSymbols().getSymbolWithName(name).getType();
        String type, code;

        if (var_t == Symbol.Type.INT || var_t == Symbol.Type.BOOLEAN)
            type = "i";
        else
            type = "a";

        if (index <= 3)
            code = "store_";
        else
            code = "store ";

        output.println("\t" + type + code + index);
    }

    private void loadGlobalVariable(String name) {
        String type;
        Symbol symbol = root.getSymbols().getSymbolWithName(name);

        switch (symbol.getType()) {
        case INT:
        case BOOLEAN:
            type = " I";
            break;
        case INT_ARRAY:
            type = "[I";
            break;
        default:
            return;
        }

        output.println("\tgetstatic " + root.getName() + "/" + name + type);
    }

    private void storeGlobalVariable(String name) {
        String type;
        Symbol symbol = root.getSymbols().getSymbolWithName(name);

        switch (symbol.getType()) {
        case INT:
        case BOOLEAN:
            type = " I";
            break;
        case INT_ARRAY:
            type = "[I";
            break;
        default:
            return;
        }

        output.println("\tputstatic " + root.getName() + "/" + name + type);
    }

    private void generateAssign(SimpleNode node) {

        SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);

        SimpleNode rhs = (SimpleNode) node.jjtGetChild(1);
        String generated_code = "";

        if (rhs.getId() == ProgramTreeConstants.JJTNEW) {
            generateNew(rhs, generated_code);
        } else {
            count.set(0);
            generateOperation(rhs, generated_code);
        }

        generateAssignLhs(lhs);
        // TODO: right now always assuming ArrayAccess and ScalarAccess are from static
        // fields
        // output.println("\tputstatic " + lhs.getName());
    }

    private void generateAssignLhs(SimpleNode lhs) {
        String var_name = lhs.getName();

        if (root.getSymbols().hasSymbolWithNameLocal(var_name))
            storeGlobalVariable(var_name);
        else
            storeLocalVariable(lhs, var_name);
    }

    private void generateNew(SimpleNode rhs, String generated_code) {
        if (rhs.getChildren() != null) {
            for (Node child : rhs.getChildren()) {
                SimpleNode child_simplenode = (SimpleNode) child;
                generateNew(child_simplenode, generated_code);
            }
        }

        if (rhs != null) {
            if (rhs.getChildren() != null) {
                if (rhs.getType() == "new") {
                    generated_code += "\tnewarray\t" + rhs.getType().replace("new", "int");
                }
            } else if (rhs.getId() == ProgramTreeConstants.JJTTERM) {
                if (rhs.getType() == "int") {
                    generated_code = loadIntString(rhs.getNodeValue());
                } else {
                    if (this.root.getSymbols().hasSymbolWithNameLocal(rhs.getName()))
                        this.loadGlobalVariable(rhs.getNodeValue());
                    else
                        this.loadLocalVariable(rhs, rhs.getNodeValue());
                }
            }
        }

        output.println(generated_code);
    }

    private void generateOperation(SimpleNode rhs, String generated_code) {

        if (rhs.getChildren() != null) {
            for (Node child : rhs.getChildren()) {
                SimpleNode child_simplenode = (SimpleNode) child;
                generateOperation(child_simplenode, generated_code);
            }
        }

        if (rhs != null) {
            if (rhs.getChildren() != null) {
                if (rhs.getChildren().length > 1) {
                    switch (rhs.getId()) {
                    case ProgramTreeConstants.JJTADD:
                        generated_code += "\tiadd";
                        break;
                    case ProgramTreeConstants.JJTSUB:
                        generated_code += "\tisub";
                        break;
                    case ProgramTreeConstants.JJTMUL:
                        generated_code += "\timul";
                        break;
                    case ProgramTreeConstants.JJTDIV:
                        generated_code += "\tidiv";
                        break;
                    }

                }
            } else if (rhs.getId() == ProgramTreeConstants.JJTTERM && rhs.getType() != "this") {

                if (rhs.getType() == "int") {
                    generated_code += loadIntString(rhs.getNodeValue());
                } else {
                    if (this.root.getSymbols().hasSymbolWithNameLocal(rhs.getName())) {
                        this.loadGlobalVariable(rhs.getNodeValue());
                    } else {
                        this.loadLocalVariable(rhs, rhs.getNodeValue());
                    }
                }
            }

            if (rhs.getId() == ProgramTreeConstants.JJTPERIOD) {
                generated_code += "\tinvokevirtual ComputeFac:(I)I";
            }

            if (generated_code != "")
                output.println(generated_code);

            /*
             * TODO: FAZER ARITMÉTRICA COM DIFERENTE COISAS PARA ALÉM DE INT POR EXEMPLO UMA
             * FUNÇÃO, UM DOUBLE OU FLOAT
             * 
             */
        }
    }

    private void generateIfStatement(SimpleNode node) {

        String generated_code = "";

        SimpleNode exprNode = (SimpleNode) node.jjtGetChild(0);

        generated_code += generateExpr(exprNode);
        output.println(generated_code);

        ifInstruction = counter;

        SimpleNode bodyNode = (SimpleNode) node.jjtGetChild(1);

        generateBody(bodyNode);

        output.println("\tgoto\t" + elseInstruction);

    }

    private String generateExpr(SimpleNode exprNode) {

        String generated_code = "";

        switch (exprNode.getId()) {

        case ProgramTreeConstants.JJTLESS_THAN:
            if (exprNode.getChildren() != null) {
                for (Node child : exprNode.getChildren()) {
                    SimpleNode child_simpleNode = (SimpleNode) child;
                    if (child_simpleNode.getId() == ProgramTreeConstants.JJTTERM) {
                        if (child_simpleNode.getType() == "int") {
                            generated_code += loadIntString(child_simpleNode.getNodeValue());
                        } else {
                            if (this.root.getSymbols().hasSymbolWithNameLocal(child_simpleNode.getName())) {
                                this.loadGlobalVariable(child_simpleNode.getNodeValue());
                            } else {
                                this.loadLocalVariable(child_simpleNode, child_simpleNode.getNodeValue());
                            }
                        }
                    }
                }
            }
            generated_code += "\n\tif_icmplt " + ifInstruction;
            generated_code += "\n\tbipush\t" + 1; // TODO FAZER O VALOR CERTO
            break;

        case ProgramTreeConstants.JJTAND:
            break;
        }

        return generated_code;
    }

    private void generateElseStatement(SimpleNode node) {

        for (Node child : node.getChildren()) {
            SimpleNode child_simpleNode = (SimpleNode) child;
            generateBody(child_simpleNode);
        }

        elseInstruction = counter;
    }

}