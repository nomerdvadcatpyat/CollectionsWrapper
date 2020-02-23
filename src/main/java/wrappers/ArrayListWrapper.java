package wrappers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ArrayListWrapper<T> extends ArrayList<T> {
    private ArrayList<T> arrayList;
    private File file;

    public ArrayListWrapper(ArrayList<T> arrayList, File file){
        this.arrayList = arrayList;
        this.file = file;
        if(file.exists())
            try (FileReader fr = new FileReader(file)){

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public int size() {
        return arrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return arrayList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return arrayList.contains(o);
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

    public boolean add(T value){
        return arrayList.add(value);
    }

    @Override
    public boolean remove(Object o) {
        return arrayList.remove(o);
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

    @Override
    public T set(int index, T element) {
        return arrayList.set(index,element);
    }

    @Override
    public void add(int index, T element) {
        arrayList.add(index,element);
    }

    @Override
    public T remove(int index) {
        return arrayList.remove(index);
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
