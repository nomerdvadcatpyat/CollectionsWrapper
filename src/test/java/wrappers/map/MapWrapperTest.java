package wrappers.map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wrappers.TestClass;

import java.util.*;

import static org.junit.Assert.*;
import static wrappers.WrappersTestsConstants.*;

public class MapWrapperTest {

    private Map<TestClass, String> mapWrapper;
    private Map<TestClass, String> equalsMap = new HashMap<>();

    @Before
    public void prepare() {
        mapWrapper = new MapWrapper<>(new HashMap<>(), DIRECTORY, PREFIX, 50);
        simpleFillMaps(equalsMap, mapWrapper);
    }

    private void simpleFillMaps(Map<TestClass, String> equalsMap, Map<TestClass, String> wrapperMap) {
        for (int i = 0; i < 5; i++) {
            TEST_CLASS_LIST.forEach(p -> {
                equalsMap.put(p, p.getTitle());
                wrapperMap.put(p, p.getTitle());
            });
        }
    }

    @Test
    public void put() {
        assertEquals(mapWrapper.toString(), equalsMap.toString());
    }

    @Test
    public void remove() {
        for (int i = 0; i < 20; i++) {
            equalsMap.remove(TEST_CLASS_LIST.get(0));
            mapWrapper.remove(TEST_CLASS_LIST.get(0));
        }

        assertEquals(equalsMap.toString(), mapWrapper.toString());
    }

    @Test
    public void putAll() {
        for (int i = 0; i < 20; i++) {
            equalsMap.putAll(PERSON_MAP);
            mapWrapper.putAll(PERSON_MAP);
        }

        assertEquals(equalsMap.toString(), mapWrapper.toString());
    }

    @Test
    public void clear() {
        equalsMap.clear();
        mapWrapper.clear();

        assertEquals(equalsMap.toString(), mapWrapper.toString());
    }

    @Test
    public void replace() {
        equalsMap.replace(new TestClass("second"), "2");
        mapWrapper.replace(new TestClass("second"), "2");

        equalsMap.replace(new TestClass("third"), "2", "123");
        mapWrapper.replace(new TestClass("third"), "2", "123");

        equalsMap.replace(new TestClass("sixth"), "6", null);
        mapWrapper.replace(new TestClass("sixth"), "6", null);

        assertEquals(equalsMap.toString(), mapWrapper.toString());
    }

    @Test
    public void replaceAll() {
        equalsMap.replaceAll((k,v) -> v + " is " + k);
        mapWrapper.replaceAll((k,v) -> v + " is " + k);

        assertEquals(equalsMap.toString(), mapWrapper.toString());
    }

    @Test
    public void merge() {
        equalsMap.merge(new TestClass("second"), "2", (k, v) -> v + " is " + k);
        mapWrapper.merge(new TestClass("second"), "2", (k, v) -> v + " is " + k);

        equalsMap.merge(new TestClass("third"), "2", (k, v) -> v + " is " + k);
        mapWrapper.merge(new TestClass("third"), "2", (k, v) -> v + " is " + k);

        equalsMap.merge(new TestClass("sixth"), "6", (k, v) -> null);
        mapWrapper.merge(new TestClass("sixth"), "6", (k, v) -> null);

        assertEquals(equalsMap.toString(), mapWrapper.toString());
    }

    // написать тесты на реплейсы, путИфАбсент и компьют

    @Test
    public void putIfAbsent() {
        equalsMap.putIfAbsent(new TestClass("first"), "123");
        equalsMap.putIfAbsent(new TestClass("12312first"), "123");

        mapWrapper.putIfAbsent(new TestClass("first"), "123");
        mapWrapper.putIfAbsent(new TestClass("12312first"), "123");

        assertEquals(equalsMap.toString(), mapWrapper.toString());
    }

    @Test
    public void compute() {
        equalsMap.compute(new TestClass("first"), (k, v) -> v + "ABCD");
        mapWrapper.compute(new TestClass("first"), (k, v) -> v + "ABCD");

        assertEquals(equalsMap.toString(), mapWrapper.toString());
    }

    @Test
    public void combineTest() {
        for (int i = 0; i < 6; i++) {
            equalsMap.put(TEST_CLASS_LIST.get(i), i + "");
            equalsMap.putAll(PERSON_MAP);
            equalsMap.remove(TEST_CLASS_LIST.get(0));
            equalsMap.putIfAbsent(new TestClass("first"), "123");
            equalsMap.putIfAbsent(new TestClass("12312first"), "123");
            equalsMap.compute(new TestClass("first"), (k, v) -> v + "ABCD");

            mapWrapper.put(TEST_CLASS_LIST.get(i), i + "");
            mapWrapper.putAll(PERSON_MAP);
            mapWrapper.remove(TEST_CLASS_LIST.get(0));
            mapWrapper.putIfAbsent(new TestClass("first"), "123");
            mapWrapper.putIfAbsent(new TestClass("12312first"), "123");
            mapWrapper.compute(new TestClass("first"), (k, v) -> v + "ABCD");
        }

        assertEquals(equalsMap.toString(), mapWrapper.toString());
    }


    @After
    public void checkLoad() {
        try {
            mapWrapper = new MapWrapper<>(new HashMap<>(), DIRECTORY, PREFIX, 50);
            assertEquals(equalsMap.toString(), mapWrapper.toString());
        } finally {
            equalsMap = new HashMap<>();
            deleteFiles();
        }
    }
}