package wrappers.collections;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class QueueWrapper<T extends Serializable> implements Queue<T> {
    private Queue<T> queue; // внутренняя реализация очереди
    private CollectionFilesManager<T> manager; // менеджер для обновления файлов коллекции

    public QueueWrapper(Queue<T> queue, File directory, String prefix) {
        this.queue = queue;
        manager = new CollectionFilesManager<>(queue, directory, prefix, 5);
    }

    @Override
    public boolean add(T t) {
        queue.add(t); // добавить во внутр коллекцию
        manager.addInEnd(t);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        List<T> temp = new ArrayList<>(queue);
        int index = temp.indexOf(o);

        queue.remove(o);

        if (index == -1) return false;

        manager.remove(index);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        queue.addAll(c);
        manager.addAllInEnd(c);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        queue.removeAll(c);
        manager.removeDifference(queue);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        queue.retainAll(c);
        manager.removeDifference(queue);
        return true;
    }

    @Override
    public boolean offer(T t) {
        queue.offer(t);
        manager.addInEnd(t);
        return true;
    }

    @Override
    public T remove() {
        T value = queue.remove();
        manager.remove(0);
        return value;
    }

    @Override
    public T poll() {
        T value = queue.poll();
        manager.remove(0);
        return value;
    }

    @Override
    public void clear() {
        queue.clear();
        manager.removeDifference(queue);
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }


    private class Itr implements Iterator<T> {
        Iterator<T> iterator = queue.iterator();
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
            //manager.checkDifference(queue);
        }
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        boolean b = queue.removeIf(filter);
        if (b) manager.removeDifference(queue);
        return b;
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
