package src.semantic;

import java.util.Vector;
import src.semantic.*;
import src.utils.*;

class MethodSymbol extends Symbol {
    
    /**
     * Pair = <name, type>
     */
    protected Vector<Pair> parameters;

    public MethodSymbol(Type return_type, Vector<Pair> params){
        super(return_type, true);
        this.parameters = params;
    }
}