package src.semantic;

import java.util.HashMap;

public class SymbolTable {

    private SymbolTable parent_table;
    private HashMap<String, Symbol> symbols;

    public SymbolTable(SymbolTable parent_table) {
        this.parent_table = parent_table;
        this.symbols = new HashMap<>();
    }

    public boolean addSymbol(String name, Symbol.Type type, String value, boolean init) {
        Symbol new_s = new Symbol(type, value, init);

        if(!this.symbols.containsKey(name)){
            this.symbols.put(name, new_s);
            return true;
        }
        return false;
    }

    public Symbol getSymbolWithName(String name) {
        if(hasSymbolWithName(name))
            return symbols.get(name);

        return null;
    }

    public boolean hasSymbolWithName(String name){
        return symbols.containsKey(name);
    }


    /* GETTERS AND SETTERS*/    
    public SymbolTable getParentTable() {
        return this.parent_table;
    }

    public HashMap<String, Symbol> getSymbols() {
        return this.symbols;
    }
}