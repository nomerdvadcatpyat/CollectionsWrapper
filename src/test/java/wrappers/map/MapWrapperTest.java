package wrappers.map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wrappers.Person;
import wrappers.collections.SetWrapper;

import java.util.*;

import static org.junit.Assert.*;
import static wrappers.WrappersTestsConstants.*;

public class MapWrapperTest {

    private Map<Person, String> mapWrapper;
    private Map<Person, String> equalsMap = new HashMap<>();

    @Before
    public void prepare() {
        mapWrapper = new MapWrapper<>(new HashMap<>(), DIRECTORY, PREFIX);
        simpleFillMaps(equalsMap, mapWrapper);
    }

    private void simpleFillMaps(Map<Person, String> equalsMap, Map<Person, String> wrapperMap) {
        for (int i = 0; i < 5; i++) {
            PERSON_LIST.forEach(p -> {
                equalsMap.put(p, p.getName());
                wrapperMap.put(p, p.getName());
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
            equalsMap.remove(PERSON_LIST.get(0));
            mapWrapper.remove(PERSON_LIST.get(0));
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
    public void combineTest() {
        for (int i = 0; i < 6; i++) {
            equalsMap.put(PERSON_LIST.get(i), i + "");
            equalsMap.putAll(PERSON_MAP);
            equalsMap.remove(PERSON_LIST.get(0));

            mapWrapper.put(PERSON_LIST.get(i), i + "");
            mapWrapper.putAll(PERSON_MAP);
            mapWrapper.remove(PERSON_LIST.get(0));
        }

        assertEquals(equalsMap.toString(), mapWrapper.toString());
    }


    @After
    public void checkLoad() {
        try {
            mapWrapper = new MapWrapper<>(new HashMap<>(), DIRECTORY, PREFIX);
            assertEquals(equalsMap.toString(), mapWrapper.toString());
        } finally {
            equalsMap = new HashMap<>();
            deleteFiles();
        }
    }
}