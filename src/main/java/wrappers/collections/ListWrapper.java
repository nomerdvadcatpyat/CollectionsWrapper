package wrappers.collections;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import wrappers.ContainerIO;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class ListWrapper<T> implements List<T> {
    private List<T> list;
    private File file;
    private Gson gson;

    public ListWrapper(List<T> list, File file) throws IOException {
        this.list = list;
        this.file = file;
        gson = new Gson();
        Type itemsListType = new TypeToken<List<T>>() {}.getType();

        if(file.exists())
                this.list = gson.fromJson(ContainerIO.read(file), itemsListType);
    }

    private void write() {
        try(  FileWriter fileWriter = new FileWriter(file) ) {
            /* Наверное плохо обрабатывать трайкетчем здесь и нужно прокидывать иоексепшн, но тогда в add компилятор будет ругаться
             * потому что метод не такой же как и в арейлисте */
            String s = gson.toJson(list);
            fileWriter.write(s);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Методы, работающие с файлами
    @Override
    public boolean add(T t) {
        list.add(t);
        write();
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
}
