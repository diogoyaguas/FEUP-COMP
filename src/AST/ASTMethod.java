/* Generated By:JJTree: Do not edit this line. ASTMethod.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package src.AST;

import java.util.Vector;

import src.semantic.*;
import src.utils.*;

public class ASTMethod extends SimpleNode {
    public ASTMethod(int id) {
        super(id, true, false);
    }

    public ASTMethod(Program p, int id) {
        super(p, id, true, false);
    }

    public boolean addNewMethod() {

        this.symbols = getNodeSymbolTable();
        this.methods = getNodeMethodTable();

        Symbol.Type return_type = Symbol.Type.VOID;
        Vector<Symbol.Type> argument_types = new Vector<>();
        Vector<Pair> parameters = new Vector<>();

        Node[] children = getChildren();
        int i = 0;
        String method_name = this.getName();

        return_type = this.getReturnType();
        if(return_type.equals(Symbol.Type.UNDEFINED)){
            printSemanticError("Method " + method_name + "has invalid return type");
            return false;
        }

        while(((SimpleNode) children[i]).getNodeString().equals("Argument")){
            
            ASTArgument arg = (ASTArgument) children[i];
            String arg_name = arg.getName();
            Symbol.Type aux_type = arg.getArgumentType();

            if(aux_type.equals(Symbol.Type.VOID) && aux_type.equals(Symbol.Type.UNDEFINED)){
                printSemanticError("Argument " + arg_name + " has invalid type");
                return false;
            }

            Pair param_pair = new Pair(arg_name, aux_type);
            
            argument_types.add(aux_type);
            parameters.add(param_pair);

            symbols.addSymbol(arg_name, aux_type, true);

            i++;
        }

        MethodSignature signature = new MethodSignature(method_name, argument_types);

        if(this.methods.hasMethod(signature)){
            printSemanticError("Method " + method_name + " is already defined with the same arguments");
            return false;
        }

        this.methods.addMethod(method_name, argument_types, return_type, parameters);

        return true;
    }

    public boolean analyse() {

        Node[] children = getChildren();

        boolean success = true;
        
        if (children == null)
            return false;

        for(Node child : children){
            success = ((SimpleNode) child).analyse();
        }

        return success;
    }

}
/*
 * JavaCC - OriginalChecksum=41c3eccd1d265e4c7c0a6c88f3575598 (do not edit this
 * line)
 */
