package wrappers.collections;

import java.io.File;
import java.io.Serializable;

public class CollectionFileSettings implements Serializable {
    private File file;
    private int size = 0;
    private int collectionHash = 0;

    public int getCollectionHash() {
        return collectionHash;
    }

    public void setCollectionHash(int collectionHash) {
        this.collectionHash = collectionHash;
    }

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
