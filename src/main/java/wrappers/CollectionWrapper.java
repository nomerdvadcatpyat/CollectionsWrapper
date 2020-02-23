/*package wrappers;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;

public class CollectionWrapper<T> implements Collection<T>{
    private Collection<T> collection;
    private File file;
    //private FileReader fr = new FileReader(file);;

    public CollectionWrapper(Collection<T> collection, File file){
        this.collection = collection;
        this.file = file;
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return collection.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return collection.iterator();
    }

    @Override
    public Object[] toArray() {
        return collection.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return collection.toArray(a);
    }

    public boolean add(T value){
        write(value);
        return collection.add(value);
    }

    @Override
    public boolean remove(Object o) {
        return collection.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return collection.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return collection.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return collection.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return collection.retainAll(c);
    }

    @Override
    public void clear() {
        file.delete();
        collection.clear();
    }


    private void write(T value){
        FileWriter fw = null;
        try {
            fw = new FileWriter(file,true);
            fw.append(value.toString()).append("\n");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            try {
                if(fw != null) {
                    fw.flush();
                    fw.close();
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
*/