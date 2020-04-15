

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
        ListWrapper<Person> list = new ListWrapper<>(new ArrayList<>(), new File("./src/main/resources"), "test");
        Person first = new Person("first");
        Person second = new Person("second");
        Person third = new Person("third");
        Person fourth = new Person("fourth");
        Person fifth = new Person("fifth");
        Person sixth = new Person("sixth");

        List<Person> list1 = new ArrayList();
        list1.add(first);
        list1.add(second);
        list1.add(third);
        list1.add(fourth);
        list1.add(fifth);
        list1.add(sixth);

        //list.set(6,third);

//        list.addAll(0,list1);

//        list.add(second);
//        for (int i = 0; i < 2; i++)
//            list.add(4,new wrappers.Person("test"));

//        for(int i = 0; i< 5; i++)
//            list.add(first);
//        list.add(third);
//        list.add(second);
//
//        list.remove(6);

//        list.add(10,fourth);

//        for (int i = 0; i < 5; i++)
//            list.remove(5);



        // пока что пустые файлы не удаляются, нужно будет сделать
        // нужно подумать о том че будет при удалении мейн файла
        int i = 0;
        for (Person pa :
                list) {
            System.out.println(pa);
            System.out.println(i++);
        }

    }


}
