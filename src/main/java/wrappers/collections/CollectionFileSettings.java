package wrappers.collections;

import java.io.File;
import java.io.Serializable;

public class CollectionFileSettings implements Serializable {
    private File file;
    private int size = 0;
    public CollectionFileSettings(File file, int size){
        this.file = file;
        this.size = size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public File getFile() {
        return file;
    }

    public int getSize() {
        return size;
    }
}
