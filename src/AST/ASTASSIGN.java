/* Generated By:JJTree: Do not edit this line. ASTASSIGN.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package src.AST;

import src.semantic.Symbol;

public class ASTASSIGN extends SimpleNode {
    public ASTASSIGN(int id) {
        super(id);
    }

    public ASTASSIGN(Program p, int id) {
        super(p, id);
    }

    public boolean checkSymbolTable() {

        Node[] children = getChildren();

        if (children.length == 1 && isArrayInitialization((SimpleNode) children[1])) {
            symbols.initializeSymbol(((SimpleNode) children[0]).name);
            return true;
        } 
        else if (getChildren().length != 2) {
            printSemanticError("Not valid ASSIGN operands");
            return false;
        }

        // Left hand side
        SimpleNode lhs = (SimpleNode) children[0];

        if (!symbols.hasSymbolWithName(lhs.name)) {
            printSemanticError("Variable '" + lhs.name + "' has not been declared");
            return false;
        }

        Symbol.Type lhs_type = symbols.getSymbolWithName(lhs.name).getType();

        // Right hand side
        SimpleNode rhs = (SimpleNode) getChildren()[1];

        if (!rhs.checkSymbolTable())
            return false;

        Symbol.Type rhs_type = rhs.getReturnType();

        if (!lhs_type.equals(rhs_type)) {
            printSemanticError("Assign has different types on members");
            return false;
        }

        symbols.initializeSymbol(lhs.name);

        return true;
    }

    //TODO
    // Array cases:
    // - left hand side == (array name | array access)
    // - right hand side == (new init | array element assign)

    private boolean isArrayInitialization(SimpleNode child) {

        if (!child.getNodeString().equals("NEW") || !child.getType().equals("int[]"))
            return false;

        Node[] indexNode = child.getChildren();

        if (indexNode.length != 1) {
            printSemanticError("Array must be initialized with a size");
            return false;
        }

        if (!((SimpleNode) indexNode[0]).getReturnType().equals(Symbol.Type.INT)) {
            printSemanticError("Invalid array size. \nrequired: int\n found: "
                    + ((SimpleNode) indexNode[0]).getReturnType().toString());
            return false;
        }

        return true;
    }

}
/*
 * JavaCC - OriginalChecksum=44a56ebc5ea18729019b75dcebd62514 (do not edit this
 * line)
 */
