

import wrappers.Person;
import wrappers.collections.ListWrapper;
import wrappers.collections.QueueWrapper;
import wrappers.collections.SetWrapper;
import wrappers.map.MapWrapper;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

public class Program {
    public static void main(String[] args) throws FileAlreadyExistsException {
        List<String> list = new ListWrapper<>(new ArrayList<>(), new File("./src/main/resources"), "list");

        Queue<String> queue = new QueueWrapper<>(new LinkedList<>(), new File("./src/main/resources"), "queue");

//        Set<String> set = new SetWrapper<>(new HashSet<>(),new File("./src/main/resources"), "set");
//
//        set.add("aaa");
//
//        set.remove("aaa");
//



//        ТЕСТ ОЧЕРЕДИ
//        for (int i = 0; i < 20; i++)
//            queue.add("asdasd");

//        for (int i = 0; i < 19; i++)
//            queue.remove("asdasd"); // не удаляет последний оставшийся элемент.
//
//        System.out.println(queue.size());
//        queue.forEach(System.out::println);


        // ТЕСТ ЛИСТА
//        List<String> list1 = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            list1.add("asjdasd");
//            list1.add("asd");
//            list1.add("aaa");
//        }

//        list.add(0,"asdadsadsa");
//        list.removeAll(list1);

//        System.out.println(list.size());
//        list.forEach(System.out::println);



        // ТЕСТ МАПЫ

        Map<String,Integer> a = new MapWrapper<>(new HashMap<>(),new File("./src/main/resources"), "map");

//        a.put("asdasd",1);
//        a.put("a",2);

        for (int i = 0; i < 2; i++) {
            a.put(i + "",i);
        }



        a.keySet().forEach(System.out::println);
        a.values().forEach(System.out::println);

    }
}

