import wrappers.TestClass;
import wrappers.collections.ListWrapper;
import wrappers.collections.QueueWrapper;
import wrappers.collections.SetWrapper;
import wrappers.map.MapWrapper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        List<String> list = new ListWrapper<>(new ArrayList<>(), new File("src/main/resources"),
                "list", 50, 20);

        Queue<String> queue = new QueueWrapper<>(new LinkedList<>(), new File("src/main/resources"),
                "queue", 50, 20);

        Set<String> set = new SetWrapper<>(new HashSet<>(), new File("src/main/resources"),
                "set", 50, 20);

        Map<Integer, String> map = new MapWrapper<>(new HashMap<>(), new File("src/main/resources"),
                "map", 50, 20);


        for (int i = 0; i < 20; i++) {
            set.add(i + "Hello ");

            queue.offer("World");

            list.addAll(set);
            list.addAll(queue);

            map.put(i, "Iteration " + i);

        }

    }
}
