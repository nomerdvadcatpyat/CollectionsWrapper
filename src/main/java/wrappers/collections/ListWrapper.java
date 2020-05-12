package wrappers.collections;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ListWrapper<T extends Serializable> extends AbstractList<T> implements List<T> {
    private List<T> list; // внутренняя реализация листа
    private CollectionFilesManager<T> manager; // менеджер для обновления файлов коллекции

    public ListWrapper(List<T> list, File directory, String prefix) {
        this.list = list;
        manager = new CollectionFilesManager<>(list, directory, prefix, 5);
    }

    // Методы, работающие с файлами
    @Override
    public boolean add(T t) {
        list.add(t); // добавить во внутр коллекцию
        manager.addInEnd(t);
        return true;
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
        manager.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        list.addAll(c);
        manager.addAllInEnd(c);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        list.addAll(index, c);
        manager.addAll(index, c);
        return true;
    }

    @Override
    public T remove(int index) {
        T value = list.remove(index);
        manager.remove(index);
        return value;
    }

    @Override
    public boolean remove(Object o) {
        int index = list.indexOf(o);
        if (index == -1) return false;
        remove(index);
        return true;
    }

    @Override
    public void clear() {
        list.clear();
        manager.removeDifference(list);
    }

    @Override
    public T set(int index, T element) {
        T value = list.set(index, element);
        manager.set(index, element);
        return value;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        list.removeAll(c);
        manager.removeDifference(list);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        list.retainAll(c);
        manager.removeDifference(list);
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }


    private class Itr implements Iterator<T> {
        Iterator<T> iterator = list.iterator();
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
            //manager.checkDifference(list);
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListItr();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListItr(index);
    }

    private class ListItr implements ListIterator<T> {
        ListIterator<T> listIterator;
        int c;
        int lastRet;

        ListItr() {
            listIterator = list.listIterator();
            c = -1;
        }

        ListItr(int index) {
            listIterator = list.listIterator(index);
            c = index;
        }

        @Override
        public void set(T t) {
            listIterator.set(t);
            manager.set(lastRet, t);
        }

        @Override
        public void add(T t) {
            listIterator.add(t);
            lastRet = c;
            manager.add(c++, t);
        }

        @Override
        public void remove() {
            listIterator.remove();
            lastRet = c;
            manager.remove(c--);
        }

        @Override
        public boolean hasNext() {
            return listIterator.hasNext();
        }

        @Override
        public T next() {
            lastRet = c;
            c++;
            return listIterator.next();
        }

        @Override
        public boolean hasPrevious() {
            return listIterator.hasPrevious();
        }

        @Override
        public T previous() {
            lastRet = c;
            c--;
            return listIterator.previous();
        }

        @Override
        public int nextIndex() {
            return listIterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return listIterator.previousIndex();
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            listIterator.forEachRemaining(action);
            //manager.checkDifference(list);
        }
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        boolean b = list.removeIf(filter);
        if (b) manager.removeDifference(list);
        return b;
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        list.replaceAll(operator);
        manager.replaceAll(list);
        //manager.checkDifference(list); // Не подходит, он только чекает диференсы, а не реплейсит, нужно либо писать новый метод либо сетами делать
    }

    @Override
    public void sort(Comparator<? super T> c) {
        list.sort(c);
        manager.replaceAll(list);
    }

    // Методы, не работающие с файлами
    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

}
