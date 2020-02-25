

import java.io.*;
import java.util.*;

public class Program {
    public static void main(String[] args) {
        try {


            Person a = new Person("First",new Date(786317162378L));
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File("./src/main/resources/file.txt"))));

            oos.writeObject(a);
            oos.close();



            /*Gson gson = new Gson();
            ArrayList<Person> petrRelatives = new ArrayList<>();
            Person a = new Person("First",new Date(786317162378L));
            ArrayList<Person> aRelatives = new ArrayList<>();
            Person qwe = new Person("asdasd",new Date(7318731827L));
            a.addRelatives(qwe);
            petrRelatives.add(a);
            ArrayList<Person> bRelatives = new ArrayList<>();
            bRelatives.add(a);
            bRelatives.add(a);
            Person b = new Person("B",new Date(7146237211L*1000),bRelatives);
            petrRelatives.add(b);
            Person p = new Person("Петр   ы", new Date(798508800L*1000),petrRelatives);


            ArrayList<Person> arrayList1 = new ArrayList<>();
            arrayList1.add(a);
            arrayList1.add(qwe);
            arrayList1.add(b);
            arrayList1.add(p);

            String aasd = gson.toJson(arrayList1);
            Type itemsListType = new TypeToken<ArrayList<Person>>() {}.getType();
            ArrayList<Person> res = gson.fromJson(aasd,itemsListType);

            res.forEach(System.out::println);
            */

        } catch (Exception e){
           e.printStackTrace();
        }
    }

}
