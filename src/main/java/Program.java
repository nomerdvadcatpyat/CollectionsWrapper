

import wrappers.collections.ListWrapper;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

public class Program {
    public static void add(List<Person> l, Person p) {
        l.add(p);
    }

    public static void shuffle(List<Person> list) {
        Collections.shuffle(list);
    }

    public static void main(String[] args) throws FileAlreadyExistsException {
        ArrayList<Person> set1 = new ArrayList<>();
        List<Person> set2 = new LinkedList<>();
        Queue<Person> queue = new PriorityQueue<>();

        ListWrapper<Person> list = new ListWrapper<>(new ArrayList<>(), new File("./src/main/resources/test"));
        //list.clear();
        // норм)
        ArrayList<Person> petrRelatives = new ArrayList<>();
        Person a = new Person("First", new Date(786317162378L));
        ArrayList<Person> aRelatives = new ArrayList<>();
        Person qwe = new Person("asdasd", new Date(7318731827L));
        a.addRelatives(qwe);
        petrRelatives.add(a);
        ArrayList<Person> bRelatives = new ArrayList<>();
        bRelatives.add(a);
        bRelatives.add(a);
        Person b = new Person("B", new Date(7146237211L * 1000), bRelatives);
        petrRelatives.add(b);
        Person p = new Person("Gtnh   ы", new Date(798508800L * 1000), petrRelatives);


/*        List<Person> as = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            list.add(p);
        }
        list.addAll(as);*/

        int i = 0;
        for (Person pa:
             list) {
            System.out.println(pa);
            System.out.println(++i);
        }

    }

}
