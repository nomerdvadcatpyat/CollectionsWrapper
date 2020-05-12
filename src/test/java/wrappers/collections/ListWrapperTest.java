package wrappers.collections;

import org.junit.*;
import wrappers.Person;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;
import static wrappers.WrappersTestsConstants.*;

public class ListWrapperTest {

    private List<Person> listWrapper;

    private List<Person> equalsList = new ArrayList<>();

    @Before
    public void prepare() {
        listWrapper = new ListWrapper<>(new ArrayList<>(), DIRECTORY, PREFIX);
        simpleFillCollections(equalsList, listWrapper);
    }

    @Test
    public void add() {
        simpleFillCollections(equalsList, listWrapper);

        equalsList.add(PERSON_LIST.get(0));
        listWrapper.add(PERSON_LIST.get(0));

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void addByIndex() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                equalsList.add(0,PERSON_LIST.get(0));
                listWrapper.add(0,PERSON_LIST.get(0));
            }

            for (int j = 0; j < 5; j++) {
                equalsList.add(1,PERSON_LIST.get(1));
                listWrapper.add(1,PERSON_LIST.get(1));
            }

            PERSON_LIST.forEach(p -> {
                equalsList.add(0, p);
                listWrapper.add(0, p);
            });

            PERSON_LIST.forEach(p -> {
                equalsList.add(equalsList.size() - 1, p);
                listWrapper.add(listWrapper.size() - 1, p);
            });
        }


        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void addAll() {
        for (int i = 0; i < 55; i++) {
            equalsList.addAll(PERSON_LIST);
            listWrapper.addAll(PERSON_LIST);
        }

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void addAllByIndex() {
        for (int i = 0; i < 20; i++) {
            equalsList.addAll(0, PERSON_LIST);
            listWrapper.addAll(0, PERSON_LIST);

            equalsList.addAll((equalsList.size() - 1) / 2, PERSON_LIST);
            listWrapper.addAll((listWrapper.size() - 1) / 2, PERSON_LIST);

            equalsList.addAll(equalsList.size() - 1, PERSON_LIST);
            listWrapper.addAll(listWrapper.size() - 1, PERSON_LIST);
        }

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void removeByIndex() {
        for (int i = 0; i < 6; i++) {
            equalsList.remove(i);
            listWrapper.remove(i);
        }

        for (int i = 0; i < 10; i++) {
            equalsList.remove(0);
            listWrapper.remove(0);
        }

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void removeByObject() {

        Person p = new Person("first");
        for (int i = 0; i < 5; i++) {
            equalsList.remove(p);
            listWrapper.remove(p);
        }

        assertEquals(equalsList.toString() , listWrapper.toString());
    }


    @Test
    public void clear() {

        equalsList.clear();
        listWrapper.clear();

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void set() {
        for (int i = 0; i < 6; i++) {
            equalsList.set(5 - i, PERSON_LIST.get(i));
            listWrapper.set(5 - i, PERSON_LIST.get(i));
        }

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void removeAll() {

        for (int i = 0; i < 2; i++) {
            equalsList.removeAll(FIRST_THREE_PEOPLE);
            listWrapper.removeAll(FIRST_THREE_PEOPLE);
        }

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void retainAll() {

        for (int i = 0; i < 2; i++) {
            equalsList.retainAll(FIRST_THREE_PEOPLE);
            listWrapper.retainAll(FIRST_THREE_PEOPLE);
        }

        assertEquals(equalsList.toString() , listWrapper.toString());

    }

    @Test
    public void testIterator() {
        Iterator<Person> eqIter = equalsList.iterator();
        Iterator<Person> wrapIter = listWrapper.iterator();

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

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void testListIterator() {
        ListIterator<Person> eqIter = equalsList.listIterator(5);
        ListIterator<Person> wrapIter = listWrapper.listIterator(5);

        for (int i = 0; i < 11; i++) {
            eqIter.next();
            wrapIter.next();
        }

        for (int i = 0; i < 5; i++) {
            eqIter.previous();
            wrapIter.previous();
        }

        for (int i = 0; i < 5; i++) {
            eqIter.add(new Person("asdasdasd123"));
            wrapIter.add(new Person("asdasdasd123"));
        }

        while (wrapIter.hasNext()) {
            eqIter.next();
            wrapIter.next();

            eqIter.set(new Person("x æ a-12"));
            wrapIter.set(new Person("x æ a-12"));

        }

        assertEquals(equalsList.toString() , listWrapper.toString());

    }

    @Test
    public void removeIf() {
        equalsList.removeIf(p -> p.getName().equals("first") || p.getName().equals("second"));
        listWrapper.removeIf(p -> p.getName().equals("first") || p.getName().equals("second"));

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void replaceAll() {
        equalsList.replaceAll(p -> new Person("AAAAAAA"));
        listWrapper.replaceAll(p -> new Person("AAAAAAA"));

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void sort() {
        equalsList.sort(Comparator.comparingInt(Person::hashCode));
        listWrapper.sort(Comparator.comparingInt(Person::hashCode));

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void combineTest() {

        for (int i = 0; i < 20; i++) {
            equalsList.add(PERSON_LIST.get(0));
            equalsList.addAll(PERSON_LIST);
            equalsList.remove(0);
            equalsList.add(6, PERSON_LIST.get(2));
            equalsList.removeAll(FIRST_THREE_PEOPLE);
            equalsList.retainAll(Arrays.asList(PERSON_LIST.get(3), PERSON_LIST.get(4)));
            equalsList.addAll(4, PERSON_LIST);


            listWrapper.add(PERSON_LIST.get(0));
            listWrapper.addAll(PERSON_LIST);
            listWrapper.remove(0);
            listWrapper.add(6, PERSON_LIST.get(2));
            listWrapper.removeAll(FIRST_THREE_PEOPLE);
            listWrapper.retainAll(Arrays.asList(PERSON_LIST.get(3), PERSON_LIST.get(4)));
            listWrapper.addAll(4, PERSON_LIST);

        }

        assertEquals(equalsList.toString() , listWrapper.toString());

    }

    @After
    public void checkLoad() {
        try {
            listWrapper = new ListWrapper<>(new ArrayList<>(), DIRECTORY, PREFIX);
            assertEquals(equalsList.toString() , listWrapper.toString());
        } finally {
            equalsList = new ArrayList<>();
            deleteFiles();
        }
    }
}