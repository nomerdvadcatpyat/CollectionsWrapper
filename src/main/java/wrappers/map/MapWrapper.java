package wrappers.map;

import wrappers.collections.CollectionFilesManager;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class MapWrapper<K extends Serializable, V extends Serializable> implements Map<K, V> {


    private Map<K, V> map;
    private Set<K> keySet;
    private Collection<V> valueCol;
    private CollectionFilesManager<K> keyManager;
    private CollectionFilesManager<V> valueManager;

    public MapWrapper(Map<K, V> map, File directory, String prefix) {
        this.map = map;
        // Извлечь отдельно кол ключей и кол вэлью, сбиндить наверн как-то и юзать
        keySet = map.keySet();
        valueCol = map.values();

        keyManager = new CollectionFilesManager<>(keySet, directory, prefix + "-keys", 5);
        valueManager = new CollectionFilesManager<>(valueCol, directory, prefix + "-values", 5);
    }


    @Override
    public V put(K key, V value) {
        V t = map.get(key);

        map.put(key, value);

        keySet = map.keySet();
        valueCol = map.values();

        int position = getPositionInKeySet(key);

        if (t == null) {
            keyManager.add(position, keySet, Collections.singletonList(key));
            valueManager.add(position, valueCol, Collections.singletonList(value));
            t = map.get(key);
        } else {
            // нужно найти позицию ключа и позицию значения, затем перезаписать значение
            valueManager.set(position, valueCol);
        }
        return t;
    }

    private int getPositionInKeySet(K key) {
        int res = 0;
        Iterator<K> iterator = keySet.iterator();
        while (iterator.next() != key) res++;
        return res;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

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
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }
}
