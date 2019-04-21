import java.util.HashMap;

class SymbolTable {

    private SymbolTable parent_table;
    private HashMap<String, Symbol> symbols;

    public SymbolTable(SymbolTable parent_table) {
        this.parent_table = parent_table;
        this.symbols = new HashMap<>();
    }

    public boolean containsSymbol(String name) {
        return this.symbols.containsKey(name);
    }

    public boolean addSymbol(String name, Symbol.Type type, String value, boolean init) {
        Symbol new_s = new Symbol(type, value, init);

        if(!this.symbols.containsKey(name)){
            this.symbols.put(name, new_s);
            return true;
        }
        return false;
    }

    public SymbolTable getParentTable() {
        return this.parent_table;
    }

    public HashMap<String, Symbol> getSymbols() {
        return this.symbols;
    }
}