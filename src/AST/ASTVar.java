/* Generated By:JJTree: Do not edit this line. ASTVar.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package src.AST;

import src.semantic.*;

public class ASTVar extends SimpleNode {
    public ASTVar(int id) {
        super(id, false);
    }

    public ASTVar(Program p, int id) {
        super(p, id, false);
    }

    public boolean analyse() {

        Symbol.Type var_type = this.getReturnType();

        symbols = getNodeSymbolTable();

        //TODO
        // - Verify if variable with same name already exists (both local and globaly)
        // - Verify if type is valid isTypeValidForVar()
        // - Store new variable in symbol table

        if(symbols.hasSymbolWithName(this.getName())){
            printSemanticError("Variable with the same name already declared");
            return false;
        }

        if(!this.isTypeValidForVar(var_type)){
            printSemanticError("Variable declared with an invalid type");
            return false;
        }

        symbols.addSymbol(this.getName(), var_type, false);

        return true;
    }

}
/*
 * JavaCC - OriginalChecksum=60c42965936a814821d35643cdb9ea6d (do not edit this
 * line)
 */
