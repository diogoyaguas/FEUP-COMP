package src.semantic;

import java.util.Vector;

public class MethodSignature {

    protected String method_name;
    protected Vector<Symbol.Type> argument_types;

    public MethodSignature(String method_name, Vector<Symbol.Type> arg_types) {

        this.method_name = method_name;
        this.argument_types = arg_types;
        
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof MethodSignature)) return false;

        MethodSignature method = (MethodSignature) o;
        
        return this.method_name.equals(method.getMethodName()) && this.argument_types.equals(method.getArgumentTypes());
    }

    public String getMethodName(){
        return this.method_name;
    }

    public void setMethodName(String name) {
        this.method_name = name;
    }

    public Vector<Symbol.Type> getArgumentTypes(){
        return this.argument_types;
    }

    public void setArgumentTypes(Vector<Symbol.Type> types) {
        this.argument_types = types;
    }
}