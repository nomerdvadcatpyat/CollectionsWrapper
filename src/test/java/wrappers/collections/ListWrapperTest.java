package wrappers.collections;

import org.junit.*;
import wrappers.Person;

import java.util.ArrayList;
import java.util.Arrays;
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

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void addByIndex() {
        PERSON_LIST.forEach(p -> {
            equalsList.add(0, p);
            listWrapper.add(0, p);
        });

        PERSON_LIST.forEach(p -> {
            equalsList.add((equalsList.size() - 1) / 2, p);
            listWrapper.add((listWrapper.size() - 1) / 2, p);
        });

        PERSON_LIST.forEach(p -> {
            equalsList.add(equalsList.size() - 1, p);
            listWrapper.add(listWrapper.size() - 1, p);
        });

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
    public void combineTest() {

        for (int i = 0; i < 20; i++) {
            equalsList.addAll(PERSON_LIST);
            equalsList.remove(0);
            equalsList.add(6, PERSON_LIST.get(2));
            equalsList.removeAll(FIRST_THREE_PEOPLE);
            equalsList.retainAll(Arrays.asList(PERSON_LIST.get(3), PERSON_LIST.get(4)));
            equalsList.addAll(4, PERSON_LIST);

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