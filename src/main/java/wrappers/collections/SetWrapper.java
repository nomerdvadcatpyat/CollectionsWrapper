package wrappers.collections;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class SetWrapper<T extends Serializable> implements Set<T> {
    private Set<T> set; // внутренняя реализация set
    private CollectionFilesManager<T> manager; // менеджер для обновления файлов коллекции

    public SetWrapper(Set<T> set, File directory, String prefix) {
        this.set = set;
        manager = new CollectionFilesManager<>(set, directory, prefix, 5);
    }

    @Override
    public boolean add(T t) {
        int lastSize = set.size();
        set.add(t); // добавить во внутр коллекцию
        manager.addInEnd(set, lastSize);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        set.remove(o);
        manager.checkDifference(set);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        int lastSize = set.size();
        set.addAll(c);
        manager.addInEnd(set, lastSize);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        set.retainAll(c);
        manager.checkDifference(set);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        set.removeAll(c);
        manager.checkDifference(set);
        return true;
    }

    @Override
    public void clear() {
        set.clear();
        manager.checkDifference(set);
    }



    @Override
    public int size() {
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
}
