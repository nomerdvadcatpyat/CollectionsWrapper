package wrappers.map;

import java.io.Serializable;
import java.util.Map;

public class SerializableEntry<K extends Serializable,V extends Serializable> implements Serializable, Map.Entry<K, V> {
    private Map.Entry<K, V> entry;

    SerializableEntry(Map.Entry<K, V> entry) {
        this.entry = entry;
    }

    @Override
    public K getKey() {
        return entry.getKey();
    }

    @Override
    public V getValue() {
        return entry.getValue();
    }

    @Override
    public V setValue(V value) {
        return entry.setValue(value);
    }

    @Override
    public boolean equals(Object o) {
        return entry.equals(o);
    }

    @Override
    public int hashCode() {
        return entry.hashCode();
    }
}
