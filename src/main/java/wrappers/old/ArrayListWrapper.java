package wrappers.old;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import wrappers.ContainerIO;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class ArrayListWrapper<T> extends ArrayList<T>{
    private ArrayList<T> arrayList;
    private File file;
    private Gson gson;
    private Type itemsListType;

    public ArrayListWrapper(ArrayList<T> arrayList, File file) throws IOException{
        this.arrayList = arrayList;
        this.file = file;
        gson = new Gson();
        itemsListType = new TypeToken<List<T>>() {}.getType();

        if(file.exists()) {
                String json = ContainerIO.read(file);
                this.arrayList = gson.fromJson(json, itemsListType);
            }
    }

    private void write() {
        try(  FileWriter fileWriter = new FileWriter(file) ) {
        /* Наверное плохо обрабатывать трайкетчем здесь и нужно прокидывать иоексепшн, но тогда в add компилятор будет ругаться
        * потому что метод не такой же как и в арейлисте */
            String s = gson.toJson(arrayList);
            fileWriter.write(s);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    // методы, где нужна работа с файлом
    @Override
    public boolean add(T value){
        arrayList.add(value);
        write();
        return true;
    }

    @Override
    public void add(int index, T element) {
        arrayList.add(index,element);
    }

    @Override
    public boolean remove(Object o) {
        return arrayList.remove(o);
    }

    @Override
    public T remove(int index) {
        return arrayList.remove(index);
    }

    @Override
    public boolean contains(Object o) {
        return arrayList.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return arrayList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return arrayList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return arrayList.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return arrayList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return arrayList.retainAll(c);
    }

    @Override
    public void clear() {
        arrayList.clear();
    }

    @Override
    public T get(int index) {
        return arrayList.get(index);
    }



    // методы без работы с файлом
    @Override
    public int size() {
        return arrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return arrayList.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return arrayList.iterator();
    }

    @Override
    public Object[] toArray() {
        return arrayList.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return arrayList.toArray(a);
    }

    @Override
    public int indexOf(Object o) {
        return arrayList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return arrayList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return arrayList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return arrayList.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return arrayList.subList(fromIndex,toIndex);
    }


}
