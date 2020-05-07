package wrappers;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class WrappersTestsConstants {

    public static final List<Person> PERSON_LIST = Arrays.asList(new wrappers.Person("first"), new wrappers.Person("second"), new wrappers.Person("third"), new wrappers.Person("fourth"),
            new wrappers.Person("fifth"), new wrappers.Person("sixth"));

    public static final List<Person> FIRST_THREE_PEOPLE = Arrays.asList(WrappersTestsConstants.PERSON_LIST.get(0), WrappersTestsConstants.PERSON_LIST.get(1), WrappersTestsConstants.PERSON_LIST.get(2));

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
