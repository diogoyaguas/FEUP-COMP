package src.semantic;

import java.util.Vector;
import src.semantic.Symbol;
import src.utils.Pair;

public class MethodSymbol extends Symbol {
    
    /**
     * Pair = <name, type>
     */
    protected Vector<Pair> parameters;

    public MethodSymbol(Type return_type, Vector<Pair> params){
        super(return_type, true);
        this.parameters = params;
    }

    public Vector<Pair> getParameters() {
        return this.parameters;
    }

    public void printParameters() {

        for(Pair param : this.parameters) {
            System.out.print(" <" + param.getKey() + ", " + param.getValue() + "> | ");
        }
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof MethodSymbol)) return false;

        MethodSymbol method = (MethodSymbol) o;
        return this.getType().equals(method.getType()) && this.getParameters().equals(method.getParameters());
    }
} 