package wrappers.collections;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class SetWrapper<T extends Serializable> implements Set<T> {
    private Set<T> set; // внутренняя реализация set
    private Collection<T> setCollection = new ArrayList<>();
    private int lastSize;

    private CollectionFilesManager<T> manager; // менеджер для обновления файлов коллекции

    public SetWrapper(Set<T> set, File directory, String prefix) {
        this.set = set;
        manager = new CollectionFilesManager<>(set, directory, prefix, 5);
    }

    @Override
    public boolean add(T t) {
        lastSize = set.size();
        if(set.add(t)) {
            setCollection.add(t);
            manager.addInEnd(setCollection, lastSize);
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if(set.remove(o)) {
            setCollection.remove(o);
            manager.checkDifference(setCollection);
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        lastSize = set.size();
        if(set.addAll(c)) {
            setCollection.addAll(c);
            manager.addInEnd(setCollection, lastSize);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if(set.retainAll(c)) {
            setCollection.retainAll(c);
            manager.checkDifference(setCollection);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lastSize = set.size();
        set.removeAll(c);
        if(set.size() != lastSize) {
            setCollection.removeAll(c);
            manager.checkDifference(setCollection);
        }
        return true;
    }

    @Override
    public void clear() {
        set.clear();
        manager.checkDifference(set);
    }



    @Override
    public int size() {
        lastSize = set.size();
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public String toString() {
        return set.toString();
    }
}
