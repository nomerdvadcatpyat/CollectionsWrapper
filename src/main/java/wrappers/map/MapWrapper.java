package wrappers.map;

import wrappers.collections.CollectionFilesManager;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class MapWrapper<K extends Serializable, V extends Serializable> implements Map<K, V> {


    private Map<K, V> map;
    private List<SerializableEntry<K, V>> entries;


    private CollectionFilesManager<SerializableEntry<K, V>> mapManager;

    public MapWrapper(Map<K, V> map, File directory, String prefix) {
        entries = new ArrayList<>();
        this.map = map;
        mapManager = new CollectionFilesManager<>(entries, directory, prefix, 5);

        entries.forEach(e -> map.put(e.getKey(), e.getValue()));
    }


    @Override
    public V put(K key, V value) {
        SerializableEntry<K, V> newEntry = new SerializableEntry<>(key, value);

        if (map.put(key, value) == null) {
            entries.add(newEntry);
            mapManager.addInEnd(newEntry);
        } else {
            int index = entries.indexOf(newEntry);
            entries.set(index, newEntry); // equals у SerializableEntry сравнивает только по ключам
            mapManager.set(index, newEntry);
        }


        return map.get(key);
    }


    @Override
    public V remove(Object key) {
        int index = entries.indexOf(new SerializableEntry<K, V>((K) key, null));
        V t = map.remove(key);
        if (t != null) {
            entries.remove(index);
            mapManager.remove(index);
        }
        return t;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        // изменить entries пробежавшись по m форичем и дальше отдельно добавить коллекцию новых и изменить коллекцию старых хз
        Collection<SerializableEntry<K, V>> newEntries = new ArrayList<>();

        m.forEach((k, v) -> {
            SerializableEntry<K, V> newEntry = new SerializableEntry<>(k, v);

            if (map.containsKey(k)) {
                int index = entries.indexOf(newEntry);
                entries.set(index, newEntry);
                mapManager.set(index, newEntry);
            } else newEntries.add(newEntry);
        });

        map.putAll(m);

        entries.addAll(newEntries);
        mapManager.addAllInEnd(newEntries);
    }

    @Override
    public void clear() {
        map.clear();
        entries.clear();

        mapManager.removeDifference(entries);
    }


    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
