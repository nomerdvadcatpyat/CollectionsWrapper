

import wrappers.Person;
import wrappers.collections.ListWrapper;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        list1.add(first);
        list1.add(second);
        list1.add(third);
        list1.add(fourth);
        list1.add(fifth);
        list1.add(sixth);




//        list.removeAll(list1);


//        list.add(10,fourth);

//        for (int i = 0; i < 5; i++)
//            list.remove(5);


        // пока что пустые файлы не удаляются, нужно будет сделать
        // нужно подумать о том че будет при удалении мейн файла
        System.out.println("\nFinally:");
        list.forEach(System.out::println);


    }


}

