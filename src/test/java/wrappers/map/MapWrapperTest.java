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
//        for (int i = 0; i < 5; i++) {
            PERSON_LIST.forEach(p -> {
                equalsMap.put(p, p.getName());
                wrapperMap.put(p, p.getName());
            });
//        }
    }

    @Test
    public void put() {
       assertEquals(mapWrapper.toString(), equalsMap.toString());
    }

    @Test
    public void remove() {
    }

    @Test
    public void putAll() {
    }

    @Test
    public void clear() {
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