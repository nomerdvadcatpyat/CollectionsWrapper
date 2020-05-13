package wrappers;

import java.io.File;
import java.util.*;

public class WrappersTestsConstants {

    public static final List<TestClass> TEST_CLASS_LIST = Arrays.asList(new TestClass("first"), new TestClass("second"), new TestClass("third"), new TestClass("fourth"),
            new TestClass("fifth"), new TestClass("sixth"));

    public static final List<TestClass> FIRST_THREE_PEOPLE = Arrays.asList(WrappersTestsConstants.TEST_CLASS_LIST.get(0), WrappersTestsConstants.TEST_CLASS_LIST.get(1), WrappersTestsConstants.TEST_CLASS_LIST.get(2));

    public static final Map<TestClass, String> PERSON_MAP = new HashMap<>() {
        {
            put(new TestClass("Person1"), "Person1");
            put(new TestClass("Person2"), "Person2");
            put(new TestClass("Person3"), "Person3");
            put(new TestClass("Person4"), "Person4");
        }
    };

    public static final File DIRECTORY = new File("./src/main/resources");

    public static final String PREFIX = "test";

    public static void simpleFillCollections(Collection<TestClass> equalsCollection, Collection<TestClass> wrapperCollection) {
        for (int i = 0; i < 5; i++) {
            TEST_CLASS_LIST.forEach(p -> {
                equalsCollection.add(p);
                wrapperCollection.add(p);
            });
        }
    }

    public static void deleteFiles() {
        for (File file :
                Objects.requireNonNull(DIRECTORY.listFiles())) {
            file.delete();
        }
    }
}
