package wrappers.collections;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SetWrapper<T extends Serializable> implements Set<T> {
    private Set<T> set;

    private List<T> collectionInFiles = new ArrayList<>();
    private CollectionFilesManager<T> manager;

    public SetWrapper(Set<T> set, File directory, String prefix) {
        this.set = set;
        manager = new CollectionFilesManager<>(collectionInFiles, directory, prefix, 50, 20);

        set.addAll(collectionInFiles);
    }

    public SetWrapper(Set<T> set, File directory, String prefix, int fileObjectCapacity, int changesCounter) {
        this.set = set;
        manager = new CollectionFilesManager<>(collectionInFiles, directory, prefix, fileObjectCapacity, changesCounter);

        set.addAll(collectionInFiles);
    }

    @Override
    public boolean add(T t) {
        if (set.add(t)) {
            collectionInFiles.add(t);
            manager.addInEnd(t);
        }
        return true;
    }

//    private int getSetPos(T t) {
//        int c = 0;
//        Iterator<T> iterator = set.iterator();
//        while (!iterator.next().equals(t)) {
//            if (!iterator.hasNext()) return -1;
//            c++;
//        }
//        return c;
//    }

    @Override
    public boolean remove(Object o) {
        if (set.remove(o)) {
            int index = collectionInFiles.indexOf(o);
            collectionInFiles.remove(o);
            manager.remove(index);
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Collection<T> newElements = new ArrayList<>(c);
        newElements.removeAll(set);

        if(!newElements.isEmpty()) {
            set.addAll(c);
            collectionInFiles.addAll(newElements);
            manager.addAllInEnd(newElements);
            return true;
        }

        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (set.retainAll(c)) {
            collectionInFiles.retainAll(c);
            manager.removeDifference(collectionInFiles);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (set.removeAll(c)) {
            collectionInFiles.removeAll(c);
            manager.removeDifference(collectionInFiles);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        set.clear();
        collectionInFiles.clear();
        manager.removeDifference(collectionInFiles);
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }


    private class Itr implements Iterator<T> {
        private Iterator<T> iterator = set.iterator();
        private T current;

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            current = iterator.next();
            return current;
        }

        @Override
        public void remove() {
            iterator.remove();

            int index = collectionInFiles.indexOf(current);
            collectionInFiles.remove(index);
            manager.remove(index);
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            iterator.forEachRemaining(action);
            //manager.checkDifference(queue);
        }
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        if (set.removeIf(filter)) {

            collectionInFiles.removeIf(filter);
            manager.removeDifference(collectionInFiles);

            return true;
        }
        return false;
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
