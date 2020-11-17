package wrappers.collections;

import java.io.File;
import java.io.Serializable;

/// \ingroup cfs

/// \brief Объектное представление файла коллекции с дополнительными полями
public class  CollectionFileSettings implements Serializable {
    private File file;
    private int size;
    private int collectionHash;

    /**
     * Получение хэша коллекции в файле
     * @return хэш коллекции, находящейся в файле
     */
    public int getCollectionHash() {
        return collectionHash;
    }

    public void setCollectionHash(int collectionHash) {
        this.collectionHash = collectionHash;
    }

    /**
     *
     * @param file файл, в котором хранится часть коллекции
     * @param size количество элементов коллекции в этом файле
     */
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
