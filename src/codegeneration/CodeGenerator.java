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
    private int loop_counter = 0;

    private int counter;

    private int ifLoop = 0;

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
        output.print("\n.method public " + method.getName());

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
        case ProgramTreeConstants.JJTASSIGN:
            generateAssign(node);
            break;
        case ProgramTreeConstants.JJTIF:
            generateIfStatement(node);
            break;
        case ProgramTreeConstants.JJTELSE:
            generateElseStatement(node);
            break;
        case ProgramTreeConstants.JJTWHILE:
            generateWhile(node);
            break;
        case ProgramTreeConstants.JJTPERIOD:
            generateCall(node);
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

        for (int i = 0; i < method_node.jjtGetNumChildren(); i++) {
            SimpleNode argument = (SimpleNode) method_node.getChildren()[i];

            if (isArrayAccess(argument)) {
                generateArrayAccess();
                // TODO
                // Method above
            } else if (argument.jjtGetNumChildren() == 2) {
                generateOperation(argument);
            } else {
                switch (argument.getId()) {
                case ProgramTreeConstants.JJTTERM:
                    generateTerm(argument);
                    break;
                case ProgramTreeConstants.JJTPERIOD:
                    generateCall(argument);
                    break;
                default:
                    break;
                }
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
        // Does not work then method is from another class. How do we know the type of
        // the method?
        Symbol.Type method_return = root.getMethods().obtainMethod(method.getName(), arg_types).getType();

        switch (method_return) {
        case INT:
            method_ret = "I";
            break;
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

        if (method_class == "this")
            output.println("\t" + "invokevirtual " + method_name + "(" + method_arg + ")" + method_ret);
        else
            output.println("\t" + "invokespecial " + method_name + "(" + method_arg + ")" + method_ret);

    }

    private void loadBoolean(String bool) {
        this.loadInt(bool);
    }

    private void loadInt(String v) {

        int value = Integer.parseInt(v);

        if ((value >= 0) && (value <= 5)) {
            output.println("\ticonst_" + value);
        } else if (value == -1) {
            output.println("\ticonst_m1");
        } else if (value > -129 && value < 128) {
            output.println("\tbipush " + value);
        } else if (value > -32769 && value < 32768) {
            output.println("\tsipush " + value);
        } else {
            output.println("\tldc " + value);
        }
    }

    private String loadIntString(String v) {

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

        output.print("\t" + type + code + index);
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

    private boolean isArrayAccess(SimpleNode node) {
        if (node.jjtGetNumChildren() == 1 && node.getType() == "int[]")
            return true;
        return false;
    }

    private boolean isArrayInitialization(SimpleNode lhs_node, SimpleNode rhs_node) {
        return lhs_node.jjtGetNumChildren() == 0 && rhs_node.getType() == "int[]";
    }

    private void generateArrayAccess() {
        // TODO
    }

    private void generateAssign(SimpleNode node) {

        // TODO
        // - Verify if we are handling arrays
        // + left side is the name of an array ONLY, so left side will be a
        // initialization
        // + left side is an array access (list[1]), so right side must return an
        // integer
        // - If not, do has if it is a normal assign

        SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);
        SimpleNode rhs = (SimpleNode) node.jjtGetChild(1);

        if (isArrayAccess(lhs)) {
            generateArrayAccess();
            // TODO
            // Generate right side that returns an integer
            // Store integer in array

        } else if (isArrayInitialization(lhs, rhs)) {
            generateArrayInitilization(rhs);
        } else if (rhs.jjtGetNumChildren() == 2) {
            generateOperation(rhs);
            generateAssignLhs(lhs);
        } else {
            switch (rhs.getId()) {
            case ProgramTreeConstants.JJTTERM:
                generateTerm(rhs);
                break;
            case ProgramTreeConstants.JJTPERIOD:
                generateCall(rhs);
                break;
            default:
                break;
            }
            generateAssignLhs(lhs);
        }
    }

    private void generateAssignLhs(SimpleNode lhs) {
        String var_name = lhs.getName();

        if (root.getSymbols().hasSymbolWithNameLocal(var_name))
            storeGlobalVariable(var_name);
        else
            storeLocalVariable(lhs, var_name);
    }

    private void loadVariable(SimpleNode node) {
        if (this.root.getSymbols().hasSymbolWithNameLocal(node.getName())) {
            this.loadGlobalVariable(node.getNodeValue());
        } else {
            this.loadLocalVariable(node, node.getNodeValue());
        }
    }

    private void generateTerm(SimpleNode term_node) {

        if (term_node.getChildren() == null) {

            switch (term_node.getReturnType()) {
            case INT:
                loadInt(term_node.getNodeValue());
                break;
            case BOOLEAN:
                loadBoolean(term_node.getNodeValue());
                break;
            case VOID:
                loadVariable(term_node);
                break;
            case INT_ARRAY:
                break;
            default:
                break;
            }
        }
    }

    private void generateArrayInitilization(SimpleNode new_node) {

        SimpleNode child = (SimpleNode) new_node.jjtGetChild(0);

        if (child.jjtGetNumChildren() == 2)
            generateOperation(child);
        else {
            switch (child.getId()) {
            case ProgramTreeConstants.JJTTERM:
                generateTerm(child);
                break;
            case ProgramTreeConstants.JJTPERIOD:
                generateCall(child);
                break;
            default:
                break;
            }
        }

        output.println("\tnewarray int");

    }

    private void generateOperation(SimpleNode operation_node) {

        String generated_code = "";
        SimpleNode lhs = (SimpleNode) operation_node.jjtGetChild(0);
        SimpleNode rhs = (SimpleNode) operation_node.jjtGetChild(1);

        if (lhs.jjtGetNumChildren() == 2 && lhs.getId() != ProgramTreeConstants.JJTPERIOD) {
            generateOperation(lhs);
        } else {
            switch (lhs.getId()) {
            case ProgramTreeConstants.JJTTERM:
                generateTerm(lhs);
                break;
            case ProgramTreeConstants.JJTPERIOD:
                generateCall(lhs);
                break;
            default:
                break;
            }
        }

        if (rhs.jjtGetNumChildren() == 2 && rhs.getId() != ProgramTreeConstants.JJTPERIOD) {
            generateOperation(rhs);
        } else {
            switch (rhs.getId()) {
            case ProgramTreeConstants.JJTTERM:
                generateTerm(rhs);
                break;
            case ProgramTreeConstants.JJTPERIOD:
                generateCall(rhs);
                break;
            default:
                break;
            }
        }

        switch (operation_node.getId()) {
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
        // TODO
        // LESS_THAN and AND operations missing
        }
        output.println(generated_code);

    }

    private void generateWhile(SimpleNode while_node) {
        int loop_number = loop_counter;
        loop_counter++;
        String generated_code = "";

        output.println("\tloop" + loop_number + ":");

        SimpleNode exprNode = (SimpleNode) while_node.jjtGetChild(0);

        generated_code += generateExpr(exprNode, false);
        output.println(generated_code);

        SimpleNode bodyNode = (SimpleNode) while_node.jjtGetChild(1);

        generateBody(bodyNode);

        output.println("\tgoto loop" + loop_number);
        output.println("\tloop" + loop_counter + "_end:");

    }

    private void generateIfStatement(SimpleNode node) {

        String generated_code = "";

        SimpleNode exprNode = (SimpleNode) node.jjtGetChild(0);

        generated_code += generateExpr(exprNode, true);
        output.println(generated_code);

        ifLoop = counter++;

        SimpleNode bodyNode = (SimpleNode) node.jjtGetChild(1);

        generateBody(bodyNode);

        output.println("\tgoto begin_else_" + ifLoop);

    }

    private String generateExpr(SimpleNode exprNode, boolean ifStatement) {

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

            generated_code += "\n\tif_icmplt";

            if (ifStatement) {
                generated_code += " begin_ifElse_" + ifLoop;
            } else
                generated_code += " loop" + loop_counter + "_end";

            SimpleNode conditionValue = (SimpleNode) exprNode.jjtGetChild(1);
            generated_code += "\n\tbipush " + conditionValue.getNodeValue();

            break;

        case ProgramTreeConstants.JJTAND:
            break;
        }

        return generated_code;
    }

    private void generateElseStatement(SimpleNode node) {

        output.println("\tbegin_ifElse_" + ifLoop + ":");

        for (Node child : node.getChildren()) {
            SimpleNode child_simpleNode = (SimpleNode) child;
            generateBody(child_simpleNode);
        }

        output.println("\tbegin_else_" + ifLoop + ":");

    }

}