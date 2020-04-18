

import wrappers.Person;
import wrappers.collections.ListWrapper;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

public class Program {
    public static void main(String[] args) throws FileAlreadyExistsException {
        List<Person> list = new ListWrapper<>(new ArrayList<>(), new File("./src/main/resources"), "test");
        Person first = new Person("first");
        Person second = new Person("second");
        Person third = new Person("third");
        Person fourth = new Person("fourth");
        Person fifth = new Person("fifth");
        Person sixth = new Person("sixth");


//        List<String> list2 = new ArrayList<>();
//        list2.add("asdasd");
//        list2.add("123");
//        list2.add("321");

//        for(int i = 0; i< 3;i++)
//        list.addAll(list2);



        List<Person> list1 = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list1.add(fifth);
        }

        long start = System.currentTimeMillis();

//        list.addAll(list1);

        list.clear();

       // list1.subList(0,5).clear();



        long finish = System.currentTimeMillis();
        System.out.println(finish - start);



        int c = 0;
        for (Person p:
             list) {
            System.out.println(p + " " + c++);
        }
    }


}

