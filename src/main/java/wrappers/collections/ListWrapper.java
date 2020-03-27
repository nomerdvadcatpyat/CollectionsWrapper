package wrappers.collections;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

public class ListWrapper<T extends Serializable> implements List<T> {
    private List<T> list;
    private static Set<String> firstPathsInProgram = new HashSet<>();
    private CollectionFilesManager<T> manager;

    public ListWrapper(List<T> list, File firstFile) throws FileAlreadyExistsException {
        this.list = list;
        String firstPath = firstFile.getAbsolutePath();

        // Если с этим файлом уже связан какой-то лист, то выкинуть ошибку
        if (firstPathsInProgram.contains(firstPath)) {
            throw new FileAlreadyExistsException(firstPath, "", "This file is already connect with other collection");
        } else firstPathsInProgram.add(firstPath);

        manager = new CollectionFilesManager<T>(firstPath);
        if(firstFile.exists())
            manager.loadCollection(list);
    }

    // Методы, работающие с файлами
    @Override
    public boolean add(T t) {
        list.add(t);
        manager.addInEnd(list,t);
/*        try (
                ObjectOutputStream oos = ObjectStreamCreator.createStream(file)
        ) {
            oos.writeObject(t);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return true;
    }

    private void write(File file) {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        ) {
            for (T t : list) oos.writeObject(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean remove(Object o) {
        list.remove(o);
        File file = manager.getFileWithElement(list.indexOf(o));
        write(file);
        return true;
    }

    @Override
    public T remove(int index) {
        T value = list.remove(index);
        File file = manager.getFileWithElement(index);
        write(file);
        return value;
    }

    // по аналогии с верхними сделать
    @Override
    public boolean addAll(Collection<? extends T> c) {
        list.addAll(c);
        manager.addAllInEnd(c);
        // при адолл и ремуволл текущий подсчет элементов в ласт файле в CollectionFilesStorage.update хуевый
        // так как тут мы можем закинуть сразу много элементов, сразу на несколько файлов, а не как при адд и ремув по одному
        // потом переделаю
        // В ПРИНЦИПЕ МОЖНО ТУТ ФОРИЧЕМ ВЫЗЫВАТЬ АДД А В РЕМУВОЛЛ ВЫЗЫВАТЬ РЕМУВ НО ХЗ
       // write();

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        list.addAll(index, c);
       // write();
        return true;
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

    @Override
    public void add(int index, T element) {
        list.add(index, element);
       // write();
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
