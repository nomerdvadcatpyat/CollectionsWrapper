package wrappers.collections;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class QueueWrapper<T extends Serializable> implements Queue<T> {
    private Queue<T> queue; // внутренняя реализация очереди
    private CollectionFilesManager<T> manager; // менеджер для обновления файлов коллекции

    public QueueWrapper(Queue<T> queue, File directory, String prefix) {
        this.queue = queue;
        manager = new CollectionFilesManager<>(queue, directory, prefix, 5);
    }

    @Override
    public boolean add(T t) {
        int lastSize = queue.size();
        queue.add(t); // добавить во внутр коллекцию
        manager.addInEnd(queue, lastSize);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        List<T> temp = new ArrayList<>(queue);
        int index = temp.indexOf(o);

        queue.remove(o);

        if (index == -1) return false;

        manager.remove(index, queue);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        int lastSize = queue.size();
        queue.addAll(c);
        manager.addInEnd(queue, lastSize);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        queue.removeAll(c);
        manager.checkDifference(queue);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        queue.retainAll(c);
        manager.checkDifference(queue);
        return true;
    }

    @Override
    public boolean offer(T t) {
        queue.offer(t);
        manager.addInEnd(queue, queue.size() - 1);
        return true;
    }

    @Override
    public T remove() {
        T value = queue.remove();
        manager.remove(0, queue);
        return value;
    }

    @Override
    public T poll() {
        T value = queue.poll();
        manager.remove(0, queue);
        return value;
    }

    @Override
    public void clear() {
        queue.clear();
        manager.checkDifference(queue);
    }


    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return queue.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }

    @Override
    public T element() {
        return queue.element();
    }

    @Override
    public T peek() {
        return queue.peek();
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
