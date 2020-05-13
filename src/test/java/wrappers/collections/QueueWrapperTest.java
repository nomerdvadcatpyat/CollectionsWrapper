package wrappers.collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wrappers.TestClass;

import java.util.*;

import static org.junit.Assert.*;
import static wrappers.WrappersTestsConstants.*;

public class QueueWrapperTest {

    private Queue<TestClass> queueWrapper;

    private Queue<TestClass> equalsQueue = new LinkedList<>();

    @Before
    public void prepare() {
        queueWrapper = new QueueWrapper<>(new LinkedList<>(), DIRECTORY, PREFIX, 50);
        simpleFillCollections(equalsQueue, queueWrapper);
    }

    @Test
    public void add() {
        for (int i = 0; i < 20; i++) {
            equalsQueue.add(TEST_CLASS_LIST.get(0));
            queueWrapper.add(TEST_CLASS_LIST.get(0));
        }
        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void removeByObject() {
        for (int i = 0; i < 6; i++) {
            equalsQueue.remove(TEST_CLASS_LIST.get(i));
            queueWrapper.remove(TEST_CLASS_LIST.get(i));
        }

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void remove() {
        for (int i = 0; i < 20; i++) {
            equalsQueue.remove();
            queueWrapper.remove();
        }

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }


    @Test
    public void addAll() {
        for (int i = 0; i < 20; i++) {
            equalsQueue.addAll(TEST_CLASS_LIST);
            queueWrapper.addAll(TEST_CLASS_LIST);
        }

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void removeAll() {
        for (int i = 0; i < 20; i++) {
            equalsQueue.removeAll(FIRST_THREE_PEOPLE);
            queueWrapper.removeAll(FIRST_THREE_PEOPLE);
        }

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void retainAll() {
        for (int i = 0; i < 20; i++) {
            equalsQueue.retainAll(FIRST_THREE_PEOPLE);
            queueWrapper.retainAll(FIRST_THREE_PEOPLE);
        }

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void offer() {
        for (int i = 0; i < 6; i++) {
            equalsQueue.offer(TEST_CLASS_LIST.get(i));
            queueWrapper.offer(TEST_CLASS_LIST.get(i));
        }

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void poll() {
        for (int i = 0; i < 20; i++) {
            equalsQueue.poll();
            queueWrapper.poll();
        }

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void clear() {
        equalsQueue.clear();
        queueWrapper.clear();

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void testIterator() {
        Iterator<TestClass> eqIter = equalsQueue.iterator();
        Iterator<TestClass> wrapIter = queueWrapper.iterator();

        for (int i = 0; i < 11; i++) {
            eqIter.next();
            wrapIter.next();
        }

        while (wrapIter.hasNext()) {
            eqIter.remove();
            wrapIter.remove();

            eqIter.next();
            wrapIter.next();
        }

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void removeIf() {
        equalsQueue.removeIf(p -> p.getTitle().equals("first") || p.getTitle().equals("second"));
        queueWrapper.removeIf(p -> p.getTitle().equals("first") || p.getTitle().equals("second"));

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void combineTest() {
        for (int i = 0; i < 20; i++) {
            equalsQueue.add(TEST_CLASS_LIST.get(0));
            equalsQueue.addAll(TEST_CLASS_LIST);
            equalsQueue.remove(new TestClass("third"));
            equalsQueue.poll();
            equalsQueue.offer(new TestClass("asdasdwqeq"));
            equalsQueue.removeAll(FIRST_THREE_PEOPLE);
            equalsQueue.retainAll(Arrays.asList(TEST_CLASS_LIST.get(3), TEST_CLASS_LIST.get(4)));
            equalsQueue.removeIf(p -> p.getTitle().equals("first") || p.getTitle().equals("second"));

            queueWrapper.add(TEST_CLASS_LIST.get(0));
            queueWrapper.addAll(TEST_CLASS_LIST);
            queueWrapper.remove(new TestClass("third"));
            queueWrapper.poll();
            queueWrapper.offer(new TestClass("asdasdwqeq"));
            queueWrapper.removeAll(FIRST_THREE_PEOPLE);
            queueWrapper.retainAll(Arrays.asList(TEST_CLASS_LIST.get(3), TEST_CLASS_LIST.get(4)));
            queueWrapper.removeIf(p -> p.getTitle().equals("first") || p.getTitle().equals("second"));

        }

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @After
    public void checkLoad() {
        try {
            queueWrapper = new QueueWrapper<>(new LinkedList<>(), DIRECTORY, PREFIX, 50);
            assertEquals(equalsQueue.toString(), queueWrapper.toString());
        } finally {
            equalsQueue = new LinkedList<>();
            deleteFiles();
        }
    }

}