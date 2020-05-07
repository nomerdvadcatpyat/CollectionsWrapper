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
        V t = map.get(key);

        map.put(key, value);

        int position = getPositionInKeySet(key);
        SerializableEntry<K, V> newEntry = new SerializableEntry<>(key, value);

        if (t == null) {
            entries.add(position, newEntry);
            mapManager.add(position, entries, Collections.singletonList(newEntry));
        } else {
            entries.set(position, newEntry);
            mapManager.set(position, entries);
        }

        return map.get(key);
    }


    @Override
    public V remove(Object key) {
        int position = getPositionInKeySet((K) key);
        V t = map.remove(key);

        entries.remove(position);
        mapManager.remove(position, entries);

        return t;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    private int getPositionInKeySet(K key) {
        int res = 0;
        Iterator<K> iterator = map.keySet().iterator();
        while (!iterator.next().equals(key)) {
            res++;
        }
        return res;
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
