package streams;

import streams.AppendingObjectOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectStreamCreator {
    public static ObjectOutputStream createStream(File file) throws IOException {
        return file.exists() && file.length() != 0
                ? new AppendingObjectOutputStream(new FileOutputStream(file, true))
                : new ObjectOutputStream(new FileOutputStream(file, true));
    }
}
