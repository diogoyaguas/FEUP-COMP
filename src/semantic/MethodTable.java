package src.semantic;

import java.util.HashMap;
import java.util.Vector;

public class MethodTable {

    protected HashMap<MethodSignature, MethodSymbol> methods;

    public MethodTable() {
        this.methods = new HashMap<>();
    }

    public boolean addMethod(String method_name, Vector<Symbol.Type> arg_types, Symbol.Type return_type, Vector<Pair> paramenters) {
        
        MethodSignature signature = new MethodSignature(method_name, arg_types);

        if(this.hasMethod(signature))
            return false;

        MethodSymbol method_entry = new MethodSymbol(return_type, paramenters);

        this.methods.put(signature, method_entry);
        return true;
    }

    public boolean hasMethod(MethodSignature signature) {
        return this.methods.containsKey(signature);
    }

    public MethodSymbol obtainMethod(String method_name, Vector<Symbol.Type> arg_types){
        MethodSignature signature = new MethodSignature(method_name, arg_types);

        if(this.hasMethod(signature))
            return this.methods.get(signature);

        return null;
    }

    public HashMap<MethodSignature, MethodSymbol> getMethods(){
        return this.methods;
    }



}