package src.semantic;

class Symbol {

    public static enum Type {
        INT, INT_ARRAY, BOOLEAN, STRING
    }

    private Type type;
    private String value;
    private boolean initialized;
    
    public Symbol(Type type, String value, boolean initialized) {
        this.type = type;
        this.value = value;
        this.initialized = initialized;
    }

    public Type getType() {
        return this.type;
    }    

    public void setType(Type type) {
        this.type= type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getInitialized() {
        return this.initialized;
    }

    public void setInitialized(boolean init) {
        this.initialized = init;
    }

    public boolean equals(Object symbol) {
        Symbol s = (Symbol) symbol;

        return this.type.equals(s.getType()) && this.initialized == s.getInitialized();
    }


}