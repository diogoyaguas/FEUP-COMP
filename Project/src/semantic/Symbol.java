package src.semantic;

public class Symbol {

    public static enum Type {
        INT, INT_ARRAY, BOOLEAN, VOID, STRING_ARRAY, OBJECT, UNDEFINED
    }

    protected Type type;
    protected String object_class = "";
    protected String value;
    protected boolean initialized;
    protected int index;

    public Symbol(Type type, boolean initialized){
        this.type = type;
        this.initialized = initialized;
        this.index = -1;
    }

    public Symbol(Type type, boolean initialized, String object_class){
        this.type = type;
        this.object_class = object_class;
        this.initialized = initialized;
        this.index = -1;
    }

    public Symbol(Type type, boolean initialized, int index){
        this.type = type;
        this.initialized = initialized;
        this.index = index;
    }
    
    public Symbol(Type type, String value, boolean initialized) {
        this.type = type;
        this.value = value;
        this.initialized = initialized;
        this.index = -1;
    }

    public Symbol(Type type, String value, boolean initialized, int index) {
        this.type = type;
        this.value = value;
        this.initialized = initialized;
        this.index = index;
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

    public int getIndex(){
        return this.index;
    }

    public void setIndex(int new_index){
        this.index = new_index;
    }

    public String getObjectClass() {
        return this.object_class;
    }

    @Override
    public boolean equals(Object symbol) {
        Symbol s = (Symbol) symbol;

        return this.type.equals(s.getType()) && this.initialized == s.getInitialized();
    }


}