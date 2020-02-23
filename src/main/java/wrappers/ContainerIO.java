package wrappers;

import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class ContainerIO {
    private static Gson gson = new Gson();

    public static String read(File file) throws IOException {
        try (BufferedReader fr = new BufferedReader(new FileReader(file))) {
            String s;
            StringBuilder res = new StringBuilder();
            while ((s = fr.readLine()) != null) {
                res.append(s);
            }
            return res.toString();
        }
    }

    public static void write(File file, List list) throws IOException {
        try(  FileWriter fileWriter = new FileWriter(file) ) {
            String s = gson.toJson(list);
            fileWriter.write(s);
        }
    }

}
