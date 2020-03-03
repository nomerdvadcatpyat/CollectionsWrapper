package wrappers.collections;

import streams.ObjectStreamCreator;

import java.io.*;
import java.util.*;

public class ListWrapper<T extends Serializable> implements List<T>, AutoCloseable {
    private List<T> list;
    private File file;

    /* Нужно как-то сделать, чтобы при создании нового объекта коллекции на основе этого же файла
    все изменения из этой коллекции сохранялись туда */
    public ListWrapper(List<T> list, File file) {
        // Добавить проверку буфера, что в нем ничего не лежит, если лежит, то вытащить все оттуда
        this.list = list;
        this.file = file;

        if(file.exists()) {
            try (
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))
                    ){
                while (ois.available() > 0) {
                    int e = ois.readInt();
                    list.add((T) ois.readObject());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void write() {
        try(
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                //PrintWriter pw = new PrintWriter(new FileWriter(file))
        ) {
            //pw.println(list.size());
            for (int i = 0; i < list.size(); i++) {
                oos.writeInt(i);
                oos.writeObject(list.get(i));
                //pw.println();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Методы, работающие с файлами
    @Override
    public boolean add(T t) {
        list.add(t);
        // В файл добавить индекс list.size() и последний элемент через oos
        try (
                ObjectOutputStream oos = ObjectStreamCreator.createStream(file)
                ){
            oos.writeInt(list.size());
            oos.writeObject(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        list.remove(o);
        write();
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        list.addAll(c);
        write();
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        list.addAll(index,c);
        write();
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        list.removeAll(c);
        write();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        list.retainAll(c);
        write();
        return true;
    }

    @Override
    public void clear() {
        list.clear();
        write();
    }

    @Override
    public T set(int index, T element) {
        T value = list.set(index,element);
        write();
        return value;
    }

    @Override
    public void add(int index, T element) {
        list.add(index,element);
        write();
    }

    @Override
    public T remove(int index) {
        T value = list.remove(index);
        write();
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
        return list.subList(fromIndex,toIndex);
    }

    @Override
    public void close() throws Exception {

    }
}
