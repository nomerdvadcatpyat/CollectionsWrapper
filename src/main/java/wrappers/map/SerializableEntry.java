package wrappers.map;

import java.io.Serializable;
import java.util.Objects;

/// \brief Сериализумая обертка над вхождением в Map
public class SerializableEntry<K extends Serializable,V extends Serializable> implements Serializable {
    private K key;
    private V value;

    SerializableEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializableEntry<?, ?> that = (SerializableEntry<?, ?>) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
