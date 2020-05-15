package wrappers.collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wrappers.TestClass;

import java.util.*;

import static org.junit.Assert.*;
import static wrappers.WrappersTestsConstants.*;

public class SetWrapperTest {

    private Set<TestClass> setWrapper;
    private Set<TestClass> equalsSet = new HashSet<>();

    @Before
    public void prepare() {
        setWrapper = new SetWrapper<>(new HashSet<>(), DIRECTORY, PREFIX, 50, 20);
        simpleFillCollections(equalsSet, setWrapper);
    }

    @Test
    public void add() {
        for (int i = 0; i < 55; i++) {
            equalsSet.add(TEST_CLASS_LIST.get(0));
            equalsSet.add(new TestClass(i + ""));
            equalsSet.add(new TestClass(i + "123"));
            equalsSet.add(new TestClass(i + "432"));

            setWrapper.add(TEST_CLASS_LIST.get(0));
            setWrapper.add(new TestClass(i + ""));
            setWrapper.add(new TestClass(i + "123"));
            setWrapper.add(new TestClass(i + "432"));
        }

        assertEquals(equalsSet.toString(), setWrapper.toString());
    }

    @Test
    public void remove() {
        for (int i = 0; i < 20; i++) {
            equalsSet.remove(TEST_CLASS_LIST.get(0));
            setWrapper.remove(TEST_CLASS_LIST.get(0));
        }

        assertEquals(equalsSet.toString(), setWrapper.toString());
    }

    @Test
    public void addAll() { // Вообще чето не понятное, при выгрузке файл из 33 элементов один в коллекции
        for (int i = 0; i < 55; i++) {
            equalsSet.addAll(FIRST_THREE_PEOPLE);
            setWrapper.addAll(FIRST_THREE_PEOPLE);
            equalsSet.addAll(Arrays.asList(new TestClass(i + ""), new TestClass(i + "1"), new TestClass(i + "2")));
            setWrapper.addAll(Arrays.asList(new TestClass(i + ""), new TestClass(i + "1"), new TestClass(i + "2")));
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

    @Test
    public void combineTest() {
        List<TestClass> otherTestClassList = Arrays.asList(new TestClass("asdas"), new TestClass("askdas"), new TestClass("sadads123"),
                new TestClass("yhsaqiudyq"), new TestClass("18273sjaddas"), new TestClass("a123dasd"));

        for (int i = 0; i < 2; i++) {
            equalsSet.removeIf(p -> p.getTitle().equals("first") || p.getTitle().equals("second"));
            equalsSet.add(otherTestClassList.get(i));
            equalsSet.addAll(TEST_CLASS_LIST);
            equalsSet.remove(TEST_CLASS_LIST.get(i));
            equalsSet.removeAll(FIRST_THREE_PEOPLE);
            equalsSet.retainAll(Arrays.asList(otherTestClassList.get(3), otherTestClassList.get(4)));

            setWrapper.removeIf(p -> p.getTitle().equals("first") || p.getTitle().equals("second"));
            setWrapper.add(otherTestClassList.get(i));
            setWrapper.addAll(TEST_CLASS_LIST);
            setWrapper.remove(TEST_CLASS_LIST.get(i));
            setWrapper.removeAll(FIRST_THREE_PEOPLE);
            setWrapper.retainAll(Arrays.asList(otherTestClassList.get(3), otherTestClassList.get(4)));
        }

        assertEquals(equalsSet.toString(), setWrapper.toString());
    }

    @Test
    public void testIterator() {
        Iterator<TestClass> eqIter = equalsSet.iterator();
        Iterator<TestClass> wrapIter = setWrapper.iterator();

        for (int i = 0; i < 4; i++) {
            eqIter.next();
            wrapIter.next();
        }

        eqIter.remove();
        wrapIter.remove();

        eqIter.next();
        wrapIter.next();

        assertEquals(equalsSet.toString(), setWrapper.toString());
    }

    @Test
    public void removeIf() {
        equalsSet.removeIf(p -> p.getTitle().equals("first") || p.getTitle().equals("second"));
        setWrapper.removeIf(p -> p.getTitle().equals("first") || p.getTitle().equals("second"));

        assertEquals(equalsSet.toString(), setWrapper.toString());
    }

    @After
    public void checkLoad() {
        try {
            setWrapper = new SetWrapper<>(new HashSet<>(), DIRECTORY, PREFIX, 50, 20);
            assertEquals(equalsSet.toString(), setWrapper.toString());
        } finally {
            equalsSet = new HashSet<>();
            deleteFiles();
        }
    }
}