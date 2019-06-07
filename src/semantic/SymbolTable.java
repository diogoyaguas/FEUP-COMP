package src.semantic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import src.semantic.Symbol.Type;

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

    public boolean addSymbol(String name, Type type, boolean init, String object_type) {

        Symbol new_s = new Symbol(type, init, object_type);

        if (!this.symbols.containsKey(name)) {
            this.symbols.put(name, new_s);
            return true;
        }
        return false;

	}

    public boolean addSymbol(String name, Symbol.Type type, boolean init, int index) {
        Symbol new_s = new Symbol(type, init, index);

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

    public boolean addSymbol(String name, Symbol.Type type, String value, boolean init, int index) {
        Symbol new_s = new Symbol(type, value, init, index);

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

    public int getSymbolIndex(String name){

        if(hasSymbolWithName(name))
            return symbols.get(name).getIndex();
        else{
            if(parent_table == null)
                return -1;
            else
                return parent_table.getSymbolIndex(name);
        }
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
            System.out.println("  " + symbol_name + " | " + symbol.getType() + " | " + symbol.getIndex());
        }
    }

    public int attributeIndexes(int last_index_attributed) {
        int current_index = last_index_attributed;

        Iterator<Map.Entry<String, Symbol>> iterator = symbols.entrySet().iterator();
        while(iterator.hasNext()) {

            Map.Entry<String,Symbol> entry = iterator.next();

            //String name = entry.getKey();
            Symbol symbol = entry.getValue();

            if(symbol.getIndex() == -1) 
                symbol.setIndex(++current_index);
            
        }

        return current_index;
    }

    /* GETTERS AND SETTERS */
    public SymbolTable getParentTable() {
        return this.parent_table;
    }

    public HashMap<String, Symbol> getSymbols() {
        return this.symbols;
    }
}