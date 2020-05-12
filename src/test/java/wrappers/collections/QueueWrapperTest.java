package wrappers.collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wrappers.Person;

import java.util.*;

import static org.junit.Assert.*;
import static wrappers.WrappersTestsConstants.*;

public class QueueWrapperTest {

    private Queue<Person> queueWrapper;

    private Queue<Person> equalsQueue = new LinkedList<>();

    @Before
    public void prepare() {
        queueWrapper = new QueueWrapper<>(new LinkedList<>(), DIRECTORY, PREFIX);
        simpleFillCollections(equalsQueue, queueWrapper);
    }

    @Test
    public void add() {
        for (int i = 0; i < 20; i++) {
            equalsQueue.add(PERSON_LIST.get(0));
            queueWrapper.add(PERSON_LIST.get(0));
        }
        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void removeByObject() {
        for (int i = 0; i < 6; i++) {
            equalsQueue.remove(PERSON_LIST.get(i));
            queueWrapper.remove(PERSON_LIST.get(i));
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
            equalsQueue.addAll(PERSON_LIST);
            queueWrapper.addAll(PERSON_LIST);
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
            equalsQueue.offer(PERSON_LIST.get(i));
            queueWrapper.offer(PERSON_LIST.get(i));
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
        Iterator<Person> eqIter = equalsQueue.iterator();
        Iterator<Person> wrapIter = queueWrapper.iterator();

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
        equalsQueue.removeIf(p -> p.getName().equals("first") || p.getName().equals("second"));
        queueWrapper.removeIf(p -> p.getName().equals("first") || p.getName().equals("second"));

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @Test
    public void combineTest() {
        for (int i = 0; i < 20; i++) {
            equalsQueue.add(PERSON_LIST.get(0));
            equalsQueue.addAll(PERSON_LIST);
            equalsQueue.remove(0);
            equalsQueue.removeAll(FIRST_THREE_PEOPLE);
            equalsQueue.retainAll(Arrays.asList(PERSON_LIST.get(3), PERSON_LIST.get(4)));

            queueWrapper.add(PERSON_LIST.get(0));
            queueWrapper.addAll(PERSON_LIST);
            queueWrapper.remove(0);
            queueWrapper.removeAll(FIRST_THREE_PEOPLE);
            queueWrapper.retainAll(Arrays.asList(PERSON_LIST.get(3), PERSON_LIST.get(4)));
        }

        assertEquals(equalsQueue.toString() , queueWrapper.toString());
    }

    @After
    public void checkLoad() {
        try {
            queueWrapper = new QueueWrapper<>(new LinkedList<>(), DIRECTORY, PREFIX);
            assertEquals(equalsQueue.toString(), queueWrapper.toString());
        } finally {
            equalsQueue = new LinkedList<>();
            deleteFiles();
        }
    }

}