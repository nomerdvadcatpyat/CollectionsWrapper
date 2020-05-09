package wrappers;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

public class WrappersTestsConstants {

    public static final List<Person> PERSON_LIST = Arrays.asList(new wrappers.Person("first"), new wrappers.Person("second"), new wrappers.Person("third"), new wrappers.Person("fourth"),
            new wrappers.Person("fifth"), new wrappers.Person("sixth"));

    public static final List<Person> FIRST_THREE_PEOPLE = Arrays.asList(WrappersTestsConstants.PERSON_LIST.get(0), WrappersTestsConstants.PERSON_LIST.get(1), WrappersTestsConstants.PERSON_LIST.get(2));

    public static final Map<Person, String> PERSON_MAP = new HashMap<>() {
        {
            put(new Person("Person1"), "Person1");
            put(new Person("Person2"), "Person2");
            put(new Person("Person3"), "Person3");
            put(new Person("Person4"), "Person4");
        }
    };

    public static final File DIRECTORY = new File("./src/main/resources");

    public static final String PREFIX = "test";

    public static void simpleFillCollections(Collection<Person> equalsCollection, Collection<Person> wrapperCollection) {
        for (int i = 0; i < 5; i++) {
            PERSON_LIST.forEach(p -> {
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
