package src.semantic;

import java.util.HashMap;

public class MethodTable {

    protected HashMap<MethodSignature, MethodSymbol> methods;

    public MethodTable() {
        this.methods = new HashMap<>();
    }

    public HashMap<MethodSignature, MethodSymbol> getMethods(){
        return this.methods;
    }

}