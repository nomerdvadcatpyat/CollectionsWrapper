package wrappers.collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wrappers.Person;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static wrappers.WrappersTestsConstants.*;

public class SetWrapperTest {

    private Set<Person> setWrapper;
    private Set<Person> equalsSet = new HashSet<>();

    @Before
    public void prepare() {
        setWrapper = new SetWrapper<>(new HashSet<>(), DIRECTORY, PREFIX);
        simpleFillCollections(equalsSet, setWrapper);
    }

    @Test
    public void add() {
        equalsSet.add(PERSON_LIST.get(0));
        setWrapper.add(PERSON_LIST.get(0));
        assertEquals(equalsSet.toString(), setWrapper.toString());
    }

    @Test
    public void remove() {
        for (int i = 0; i < 20; i++) {
            equalsSet.remove(PERSON_LIST.get(0));
            setWrapper.remove(PERSON_LIST.get(0));
        }

        assertEquals(equalsSet.toString(), setWrapper.toString());
    }

    @Test
    public void addAll() {
        for (int i = 0; i < 20; i++) {
            equalsSet.addAll(PERSON_LIST);
            setWrapper.addAll(PERSON_LIST);
        }

        assertEquals(equalsSet.toString(), setWrapper.toString());
    }

    @Test
    public void retainAll() {
        for (int i = 0; i < 2; i++) {
            equalsSet.retainAll(FIRST_THREE_PEOPLE);
            setWrapper.retainAll(FIRST_THREE_PEOPLE);
        }

        assertEquals(equalsSet.toString(), setWrapper.toString());
    }

    @Test
    public void removeAll() {
        for (int i = 0; i < 2; i++) {
            equalsSet.removeAll(FIRST_THREE_PEOPLE);
            setWrapper.removeAll(FIRST_THREE_PEOPLE);
        }

        assertEquals(equalsSet.toString(), setWrapper.toString());
    }

    @Test
    public void clear() {
        equalsSet.clear();
        setWrapper.clear();
        assertEquals(equalsSet.toString(), setWrapper.toString());
    }

    @After
    public void checkLoad() {
        try {
            setWrapper = new SetWrapper<>(new HashSet<>(), DIRECTORY, PREFIX);
            assertEquals(equalsSet.toString(), setWrapper.toString());
        } finally {
            equalsSet = new HashSet<>();
            deleteFiles();
        }
    }
}