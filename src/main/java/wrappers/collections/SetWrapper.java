package wrappers.collections;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SetWrapper<T extends Serializable> implements Set<T> {
    private Set<T> set;
    private int lastSize;

    private CollectionFilesManager<T> manager;

    public SetWrapper(Set<T> set, File directory, String prefix, int fileObjectCapacity) {
        this.set = set;
        manager = new CollectionFilesManager<>(set, directory, prefix, fileObjectCapacity);
    }

    @Override
    public boolean add(T t) {
        if (set.add(t)) {
            int index = getSetPos(t);
            if(index == set.size() - 1) manager.addInEnd(t);
            else manager.add(index, t);
        }
        return true;
    }

    private int getSetPos(T t) {
        int c = 0;
        Iterator<T> iterator = set.iterator();
        while (!iterator.next().equals(t)) {
            if (!iterator.hasNext()) return -1;
            c++;
        }
        return c;
    }

    @Override
    public boolean remove(Object o) {
        int index = getSetPos((T) o);

        if (set.remove(o)) {
            manager.remove(index);
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Collection<T> copyCol = new ArrayList<>(c);
        copyCol.removeAll(set);

        for (T x : copyCol) add(x);

        return set.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (set.retainAll(c)) {
            manager.removeDifference(set);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lastSize = set.size();
        set.removeAll(c);
        if (set.size() != lastSize) {
            manager.removeDifference(set);
        }
        return true;
    }

    @Override
    public void clear() {
        set.clear();
        manager.removeDifference(set);
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }


    private class Itr implements Iterator<T> {
        Iterator<T> iterator = set.iterator();
        int c = -1;

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            c++;
            return iterator.next();
        }

        @Override
        public void remove() {
            iterator.remove();
            manager.remove(c--);
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            iterator.forEachRemaining(action);
            //manager.checkDifference(queue);
        }
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        boolean b = set.removeIf(filter);
        if (b) manager.removeDifference(set);
        return b;
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
