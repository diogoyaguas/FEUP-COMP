package src.semantic;

import java.util.HashMap;

public class SymbolTable {

    private SymbolTable parent_table;
    private HashMap<String, Symbol> symbols;

    public SymbolTable(SymbolTable parent_table) {
        this.parent_table = parent_table;
        this.symbols = new HashMap<>();
    }

    public boolean addSymbol(String name, Symbol.Type type, boolean init) {
        Symbol new_s = new Symbol(type, init);

        if (!this.symbols.containsKey(name)) {
            this.symbols.put(name, new_s);
            return true;
        }
        return false;
    }

    public boolean addSymbol(String name, Symbol.Type type, String value, boolean init) {
        Symbol new_s = new Symbol(type, value, init);

        if (!this.symbols.containsKey(name)) {
            this.symbols.put(name, new_s);
            return true;
        }
        return false;
    }

    public Symbol getSymbolWithName(String name) {

        if (hasSymbolWithNameLocal(name))
            return symbols.get(name);

        if (parent_table == null)
            return null;
        else
            return parent_table.getSymbolWithName(name);
    }

    public boolean hasSymbolWithNameLocal(String name) {

        return symbols.containsKey(name);
    }

    public boolean hasSymbolWithName(String name) {

        if (symbols.containsKey(name))
            return true;

        if (parent_table == null)
            return false;
        else
            return parent_table.hasSymbolWithName(name);

    }

    public boolean checkIfInitialized(String name) {

        if (this.hasSymbolWithName(name))
            return this.getSymbolWithName(name).getInitialized();

        if (parent_table == null)
            return false;
        else
            return parent_table.checkIfInitialized(name);
    }

    public boolean initializeSymbol(String name) {

        if (this.hasSymbolWithNameLocal(name)) {
            Symbol new_symbol = this.symbols.get(name);
            new_symbol.setInitialized(true);
            this.symbols.put(name, new_symbol);
            return true;
        }

        if (parent_table == null)
            return false;
        else
            return parent_table.initializeSymbol(name);

    }

    public void printSymbolTable() {

        Symbol symbol;

        for (String symbol_name : this.symbols.keySet()) {
            symbol = this.symbols.get(symbol_name);
            System.out.println("  " + symbol_name + " | " + symbol.getType());
        }
    }

    /* GETTERS AND SETTERS */
    public SymbolTable getParentTable() {
        return this.parent_table;
    }

    public HashMap<String, Symbol> getSymbols() {
        return this.symbols;
    }
}