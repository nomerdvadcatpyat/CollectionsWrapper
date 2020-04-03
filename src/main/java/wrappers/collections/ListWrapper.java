package wrappers.collections;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

public class ListWrapper<T extends Serializable> implements List<T> {
    private List<T> list; // внутренняя реализация листа
    private static Set<String> firstPathsInProgram = new HashSet<>(); /* пути к файлам, к которым уже присоеденены листы в текущей сессии
    скорее всего нужно обобщить для всех коллекций и перенести в CFM*/
    private CollectionFilesManager<T> manager; // менеджер для обновления файлов коллекции

    public ListWrapper(List<T> list, File firstFile) throws FileAlreadyExistsException {
        this.list = list;
        String firstPath = firstFile.getAbsolutePath();

        // Если с этим файлом уже связан какой-то лист, то выкинуть ошибку
        if (firstPathsInProgram.contains(firstPath)) {
            throw new FileAlreadyExistsException(firstPath, "", "This file is already connect with other collection");
        } else firstPathsInProgram.add(firstPath);

        manager = new CollectionFilesManager<>(firstPath);
        if (firstFile.exists()) // если существует главный файл коллекции, то загрузить коллекцию
            manager.loadCollection(list);
    }

    // Методы, работающие с файлами
    @Override
    public boolean add(T t) {
        list.add(t); // добавить во внутр коллекцию
        manager.addInEnd(list, t); //
        return true;
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
        manager.addFromIndex(list, index);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        list.addAll(c);
        manager.addAllInEnd(list, c);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        list.addAll(index, c);
        // write();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        list.remove(o);
        return true;
    }

    @Override
    public T remove(int index) {
        T value = list.remove(index);
        return value;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        list.removeAll(c);
        // write();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        list.retainAll(c);
        //write();
        return true;
    }

    @Override
    public void clear() {
        list.clear();
        //write();
    }

    @Override
    public T set(int index, T element) {
        T value = list.set(index, element);
        // write();
        return value;
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
