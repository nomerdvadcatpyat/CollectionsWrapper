package wrappers.collections;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

public class ListWrapper<T extends Serializable> implements List<T> {
    private List<T> list; // внутренняя реализация листа
    private CollectionFilesManager<T> manager; // менеджер для обновления файлов коллекции

    public ListWrapper(List<T> list, File directory, String prefix) throws FileAlreadyExistsException {
        this.list = list;
        manager = new CollectionFilesManager<>(list, directory, prefix, 5);
    }

    // Методы, работающие с файлами
    @Override
    public boolean add(T t) {
        int lastSize = list.size();
        list.add(t); // добавить во внутр коллекцию
        manager.addInEnd(list, lastSize);
        return true;
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
        ArrayList<T> newCollection = new ArrayList<>();
        newCollection.add(element);
        manager.add(index, list, newCollection);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        int lastSize = list.size();
        list.addAll(c);
        manager.addInEnd(list, lastSize);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        list.addAll(index, c);
        manager.add(index, list, c);
        return true;
    }

    @Override
    public T remove(int index) {
        T value = list.remove(index);
        manager.remove(index, list);
        return value;
    }

    @Override
    public boolean remove(Object o) {
        list.remove(o);
        list.forEach(System.out::println);
        //manager.removeAll(list);
        return true;
    }

    @Override
    public void clear() {
        list.clear();
        manager.clear();
    }

    @Override
    public T set(int index, T element) {
        T value = list.set(index, element);
        manager.set(index, list);
        return value;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        list.removeAll(c);
        manager.removeAll(list);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        list.retainAll(c);
        return true;
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
    public Iterator<T> iterator() {
        return list.iterator();
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

    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

}
