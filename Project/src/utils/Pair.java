package src.utils;

public class Pair {

    protected Object key;
    protected Object value;

    public Pair(Object k, Object v){
        this.key = k;
        this.value = v;
    }

    public Object getKey(){
        return this.key;
    }

    public void setKey(Object new_key) {
        this.key = new_key;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object new_value) {
        this.value = new_value;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Pair)) return false;

        Pair pair = (Pair) o;
        return this.key.equals(pair.getKey()) && this.value.equals(pair.getValue());
    }

}
