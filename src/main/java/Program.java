

import com.google.gson.Gson;
import json.Person;
import wrappers.ArrayListWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Date;

public class Program {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>();


        Gson gson = new Gson();
        ArrayList<Person> petrRelatives = new ArrayList<>();
        Person a = new Person("First",new Date(786317162378L));
        petrRelatives.add(a);
        ArrayList<Person> bRelatives = new ArrayList<>();
        bRelatives.add(a);
        Person b = new Person("B",new Date(7146237211L*1000),bRelatives);
        petrRelatives.add(b);
        Person p = new Person("Петр   ы", new Date(798508800L*1000),petrRelatives);

        System.out.println(gson.toJson(p));

        String s = gson.toJson(p);

        Person result = gson.fromJson(s,Person.class);
        System.out.println(result.getRelatives());

    }

}
