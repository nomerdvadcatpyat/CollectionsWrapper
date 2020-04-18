package wrappers;

import junit.framework.TestCase;
import wrappers.collections.ListWrapper;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListWrapperTests extends TestCase {
    private final File directory = new File("./src/main/resources");
    private final String prefix = "test";

    private List<Person> listWrapper = new ListWrapper<>(new ArrayList<>(), directory, prefix);

    private Person[] personList = {new Person("first"), new Person("second"), new Person("third"), new Person("fourth"),
            new Person("fifth"), new Person("sixth")};

    public ListWrapperTests() throws FileAlreadyExistsException {
    }


    public void test_addOneInEnd() {
        listWrapper.add(personList[0]);
        // кароче сделать метод клоз у листа чтобы проверять что в файлы там все записалось и с load потом сверять
        // когда нибудь потом
    }

    @Override
    protected void tearDown() throws Exception {
        deleteFiles();
    }

    private void deleteFiles() {
        for (File file :
                Objects.requireNonNull(directory.listFiles())) {
            file.delete();
        }
    }
}
