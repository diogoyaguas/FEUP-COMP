/* Generated By:JJTree: Do not edit this line. ASTPERIOD.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package src.AST;

import java.util.Vector;
import src.semantic.*;
import src.semantic.Symbol.Type;

public class ASTPERIOD extends SimpleNode {

    Symbol.Type return_type = Symbol.Type.UNDEFINED;

    public ASTPERIOD(int id) {
        super(id);
    }

    public ASTPERIOD(Program p, int id) {
        super(p, id);
    }

    public Symbol.Type getReturnType() {
        return this.return_type;
    }

    public boolean checkSymbolTable() {

        if (getChildren().length != 2) {
            printSemanticError("Not enough PERIOD operands");
            return false;
        }

        Node[] children = getChildren();
        SimpleNode left_node = (SimpleNode) children[0];
        SimpleNode right_node = (SimpleNode) children[1];

        // TODO
        // Verify if the first node is a new node

        // DEBUG
        // System.out.println("lhs value " + left_node.getNodeValue() + " | " +
        // left_node.getName());

        // Verify if second node is a "length" node
        if (right_node.getName().equals("length")) {

            // DEBUG
            // System.out.println("value " + right_node.getNodeValue() + " | " +
            // right_node.getName());

            if (!left_node.getVarType(left_node.getName()).equals(Symbol.Type.INT_ARRAY)) {
                printSemanticError("Length can only be applied to int[]. Given: " + left_node.getReturnType());
                return false;
            }
            this.return_type = Symbol.Type.INT;
            // Verify if left is initialized

            return true;
        }

        // Don't verify what is ahead if it isn't from the current class, assuming it is
        // valid

        Symbol left_node_symbol = getSymbols().getSymbolWithName(left_node.getName());
        // System.out.println("symbol type " + left_node.getName() + " " + left_node_symbol);

        if (left_node_symbol == null && !(left_node.getType().equals("this"))) {
            if ((right_node.getName()).equals("printResult"))
                this.return_type = Type.STRING_ARRAY;
            else
                this.return_type = Type.VOID;
            return true;
        }

        SimpleNode method_call = (SimpleNode) children[1];
        String method_name = method_call.getName();

        Node[] method_arguments = method_call.getChildren();
        Vector<Symbol.Type> arg_types = new Vector<>();

        if (method_arguments != null)
            for (Node argument : method_arguments) {
                Symbol.Type aux_type = ((SimpleNode) argument).getReturnType();

                if (aux_type == Type.VOID) {
                    aux_type = getSymbols().getSymbolWithName(((SimpleNode) argument).getNodeValue()).getType();
                }

                arg_types.add(aux_type);
            }

        MethodSignature new_signature = new MethodSignature(method_name, arg_types);
        if (!this.methods.hasMethod(new_signature)) {
            printSemanticError("Method " + method_name + " not declared");
            return false;
        }

        this.return_type = this.methods.obtainMethod(method_name, arg_types).getType();

        return true;
    }

}
/*
 * JavaCC - OriginalChecksum=ceda7f0b7d22d82a4a9eea22a05d4678 (do not edit this
 * line)
 */