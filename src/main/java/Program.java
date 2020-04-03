

import wrappers.collections.ListWrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Program {
    public static void main(String[] args) throws FileAlreadyExistsException {
        ListWrapper<Person> list = new ListWrapper<>(new ArrayList<>(), new File("./src/main/resources/test"));
        Person first = new Person("first");
        Person second = new Person("second");
        Person third = new Person("third");
        Person fourth = new Person("fourth");
        Person fifth = new Person("fifth");
        Person sixth = new Person("sixth");

        List<Person> list1 = new ArrayList();
        list1.add(fifth);
        list1.add(sixth);

        System.out.println(list.size());

            list.add(second);
//        for (int i = 0; i < 5; i++)
//            list.add(second);

//        for(int i = 0; i< 6; i++)
//            list.add(6,first);
        //list.add(third);


        int i = 0;
        System.out.println(list.size());
        for (Person pa :
                list) {
            System.out.println(pa);
            System.out.println(i++);
        }

    }



}
