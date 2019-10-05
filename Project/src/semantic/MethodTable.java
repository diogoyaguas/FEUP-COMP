package src.semantic;

import java.util.HashMap;
import java.util.Vector;
import src.utils.*;

public class MethodTable {

    protected String class_name;
    protected HashMap<MethodSignature, MethodSymbol> methods;

    public MethodTable(String class_name) {
        this.class_name = class_name;
        this.methods = new HashMap<>();
    }

    public boolean addMethod(String method_name, Vector<Symbol.Type> arg_types, Symbol.Type return_type,
            Vector<Pair> paramenters) {

        MethodSignature signature = new MethodSignature(method_name, arg_types);

        if (this.hasMethod(signature))
            return false;

        MethodSymbol method_entry = new MethodSymbol(return_type, paramenters);

        this.methods.put(signature, method_entry);
        return true;
    }

    public boolean hasMethod(MethodSignature signature) {
        return this.methods.containsKey(signature);
    }

    public MethodSymbol obtainMethod(String method_name, Vector<Symbol.Type> arg_types) {
        MethodSignature signature = new MethodSignature(method_name, arg_types);

        if (this.hasMethod(signature)){
            return this.methods.get(signature);
        }

        return null;
    }

    public void printMethodsTable() {

        MethodSymbol method;

        for (MethodSignature signature : this.methods.keySet()) {
            method = this.methods.get(signature);
            System.out.print(signature.getMethodName() + " | Parameters:");
            method.printParameters();
            System.out.print("Returns " + method.getType() + "\n");
        }
    }

    public HashMap<MethodSignature, MethodSymbol> getMethods() {
        return this.methods;
    }

    public String getClassName() {
        return this.class_name;
    }

}