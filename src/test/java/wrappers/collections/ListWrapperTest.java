package wrappers.collections;

import org.junit.*;
import wrappers.TestClass;

import java.util.*;
import java.util.List;

import static org.junit.Assert.*;
import static wrappers.WrappersTestsConstants.*;

public class ListWrapperTest {

    private List<TestClass> listWrapper;

    private List<TestClass> equalsList = new ArrayList<>();

    @Before
    public void prepare() {
        listWrapper = new ListWrapper<>(new ArrayList<>(), DIRECTORY, PREFIX, 50, 20);
        simpleFillCollections(equalsList, listWrapper);
    }

    @Test
    public void add() {
        simpleFillCollections(equalsList, listWrapper);

        equalsList.add(TEST_CLASS_LIST.get(0));
        listWrapper.add(TEST_CLASS_LIST.get(0));

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void addByIndex() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                equalsList.add(0, TEST_CLASS_LIST.get(0));
                listWrapper.add(0, TEST_CLASS_LIST.get(0));
            }

            for (int j = 0; j < 5; j++) {
                equalsList.add(1, TEST_CLASS_LIST.get(1));
                listWrapper.add(1, TEST_CLASS_LIST.get(1));
            }

        }

        TEST_CLASS_LIST.forEach(p -> {
            equalsList.add(0, p);
            listWrapper.add(0, p);
        });

        TEST_CLASS_LIST.forEach(p -> {
            equalsList.add(equalsList.size() - 1, p);
            listWrapper.add(listWrapper.size() - 1, p);
        });


        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void addAll() {
        for (int i = 0; i < 55; i++) {
            equalsList.addAll(TEST_CLASS_LIST);
            listWrapper.addAll(TEST_CLASS_LIST);
        }

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void addAllByIndex() {
        for (int i = 0; i < 20; i++) {
            equalsList.addAll(0, TEST_CLASS_LIST);
            listWrapper.addAll(0, TEST_CLASS_LIST);

            equalsList.addAll((equalsList.size() - 1) / 2, TEST_CLASS_LIST);
            listWrapper.addAll((listWrapper.size() - 1) / 2, TEST_CLASS_LIST);

            equalsList.addAll(equalsList.size() - 1, TEST_CLASS_LIST);
            listWrapper.addAll(listWrapper.size() - 1, TEST_CLASS_LIST);
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

        TestClass p = new TestClass("first");
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
            equalsList.set(5 - i, TEST_CLASS_LIST.get(i));
            listWrapper.set(5 - i, TEST_CLASS_LIST.get(i));
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
        Iterator<TestClass> eqIter = equalsList.iterator();
        Iterator<TestClass> wrapIter = listWrapper.iterator();

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
        ListIterator<TestClass> eqIter = equalsList.listIterator(5);
        ListIterator<TestClass> wrapIter = listWrapper.listIterator(5);

        for (int i = 0; i < 11; i++) {
            eqIter.next();
            wrapIter.next();
        }

        for (int i = 0; i < 5; i++) {
            eqIter.previous();
            wrapIter.previous();
        }

        for (int i = 0; i < 5; i++) {
            eqIter.add(new TestClass("asdasdasd123"));
            wrapIter.add(new TestClass("asdasdasd123"));
        }

        while (wrapIter.hasNext()) {
            eqIter.next();
            wrapIter.next();

            eqIter.set(new TestClass("x æ a-12"));
            wrapIter.set(new TestClass("x æ a-12"));

        }

        assertEquals(equalsList.toString() , listWrapper.toString());

    }

    @Test
    public void removeIf() {
        equalsList.removeIf(p -> p.getTitle().equals("first") || p.getTitle().equals("second"));
        listWrapper.removeIf(p -> p.getTitle().equals("first") || p.getTitle().equals("second"));

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void replaceAll() {
        equalsList.replaceAll(p -> new TestClass("AAAAAAA"));
        listWrapper.replaceAll(p -> new TestClass("AAAAAAA"));

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void sort() {
        equalsList.sort(Comparator.comparingInt(TestClass::hashCode));
        listWrapper.sort(Comparator.comparingInt(TestClass::hashCode));

        assertEquals(equalsList.toString() , listWrapper.toString());
    }

    @Test
    public void combineTest() {

        equalsList.replaceAll(x -> new TestClass("SSSSSSSSSSSSSSS"));
        listWrapper.replaceAll(x -> new TestClass("SSSSSSSSSSSSSSS"));

        for (int i = 0; i < 20; i++) {
            equalsList.add(TEST_CLASS_LIST.get(0));
            equalsList.addAll(TEST_CLASS_LIST);
            equalsList.remove(0);
            equalsList.removeIf(x -> x.getTitle().equals("second"));
            equalsList.add(6, TEST_CLASS_LIST.get(2));
            equalsList.removeAll(FIRST_THREE_PEOPLE);
            equalsList.retainAll(Arrays.asList(TEST_CLASS_LIST.get(3), TEST_CLASS_LIST.get(4)));
            equalsList.addAll(2, TEST_CLASS_LIST);


            listWrapper.add(TEST_CLASS_LIST.get(0));
            listWrapper.addAll(TEST_CLASS_LIST);
            listWrapper.remove(0);
            listWrapper.removeIf(x -> x.getTitle().equals("second"));
            listWrapper.add(6, TEST_CLASS_LIST.get(2));
            listWrapper.removeAll(FIRST_THREE_PEOPLE);
            listWrapper.retainAll(Arrays.asList(TEST_CLASS_LIST.get(3), TEST_CLASS_LIST.get(4)));
            listWrapper.addAll(2, TEST_CLASS_LIST);

        }

        assertEquals(equalsList.toString() , listWrapper.toString());

    }

    @After
    public void checkLoad() {
        try {
            listWrapper = new ListWrapper<>(new ArrayList<>(), DIRECTORY, PREFIX, 50, 20);
            assertEquals(equalsList.toString() , listWrapper.toString());
        } finally {
            equalsList = new ArrayList<>();
            deleteFiles();
        }
    }
}