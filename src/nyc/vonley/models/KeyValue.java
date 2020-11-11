package nyc.vonley.models;

public class KeyValue<T> {
    String key;
    T value;

    public KeyValue(String key, T value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }
}
