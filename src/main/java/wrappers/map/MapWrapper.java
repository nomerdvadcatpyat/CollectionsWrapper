package wrappers.map;

import wrappers.collections.CollectionFilesManager;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MapWrapper<K extends Serializable, V extends Serializable> implements Map<K, V> {


    private Map<K, V> map;
    private List<SerializableEntry<K, V>> entries; // SerializableEntry сравниваются только по ключам


    private CollectionFilesManager<SerializableEntry<K, V>> mapManager;

    public MapWrapper(Map<K, V> map, File directory, String prefix) {
        entries = new ArrayList<>();
        this.map = map;
        mapManager = new CollectionFilesManager<>(entries, directory, prefix, 50, 20);

        entries.forEach(e -> map.put(e.getKey(), e.getValue()));
    }

    public MapWrapper(Map<K, V> map, File directory, String prefix, int fileObjectCapacity, int changesCounter) {
        entries = new ArrayList<>();
        this.map = map;
        mapManager = new CollectionFilesManager<>(entries, directory, prefix, fileObjectCapacity, changesCounter);

        entries.forEach(e -> map.put(e.getKey(), e.getValue()));
    }


    @Override
    public V put(K key, V value) {
        if (map.put(key, value) == null) {
            SerializableEntry<K, V> newEntry = new SerializableEntry<>(key, value);
            entries.add(newEntry);
            mapManager.addInEnd(newEntry);
        } else {
            setInManager(key, value);
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
        Collection<SerializableEntry<K, V>> newEntries = new ArrayList<>();

        m.forEach((k, v) -> {
            if (map.containsKey(k)) {
                setInManager(k,v);
            } else newEntries.add(new SerializableEntry<>(k, v));
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
    public boolean replace(K key, V oldValue, V newValue) {
        if (map.replace(key, oldValue, newValue)) {
            setInManager(key, newValue);
            return true;
        }

        return false;
    }

    @Override
    public V replace(K key, V value) {
        V v;
        if ((v = map.replace(key, value)) != null) {
            setInManager(key, value);
        }
        return v;
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        map.replaceAll(function);

        entries.clear();
        map.forEach((k, v) -> entries.add(new SerializableEntry<>(k, v)));

        mapManager.replaceAll(entries);
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (!map.containsKey(key) || map.get(key) == null) {
            return put(key, value);
        } else {
            if (map.merge(key, value, remappingFunction) != null) {
                setInManager(key, map.get(key));
                return map.get(key);
            } else {
                SerializableEntry<K, V> newEntry = new SerializableEntry<>(key, map.get(key));
                int index = entries.indexOf(newEntry);

                entries.remove(index);
                mapManager.remove(index);

                return null;
            }
        }
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if(map.compute(key, remappingFunction) != null) {
            setInManager(key, map.get(key));
            return map.get(key);
        }
        return null;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        if(map.get(key) == null)
            put(key, mappingFunction.apply(key));
        return map.get(key);
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if(map.get(key) != null)
            compute(key, remappingFunction);
        return map.get(key);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        if(map.get(key) == null)
            put(key, value);
        return map.get(key);
    }

    private void setInManager(K key, V value) {
        SerializableEntry<K, V> newEntry = new SerializableEntry<>(key,value);
        int index = entries.indexOf(newEntry);

        entries.set(index, newEntry);
        mapManager.set(index, newEntry);
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
