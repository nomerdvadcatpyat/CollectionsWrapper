package wrappers.collections;

import java.io.*;
import java.util.*;

public class CollectionFilesManager<T extends Serializable> {

    private List<CollectionFileSettings> filesSettingsCollection = new ArrayList<>();
    private String prefix;
    private File directory;
    private File fileWithCollectionFiles;
    private int fileObjectCapacity;

    public CollectionFilesManager(Collection<T> collection, File directory, String prefix, int fileObjectCapacity) {
        this.fileObjectCapacity = fileObjectCapacity;
        this.prefix = prefix;
        this.directory = directory;
        fileWithCollectionFiles = new File(directory + File.separator + prefix + "-files_storage");
        if (fileWithCollectionFiles.length() != 0) {
            readFilesSettingsCollection();
            loadCollection(collection);
        } else addCFS();

        // отладка
        int i = 0;
        for (CollectionFileSettings cfs :
                filesSettingsCollection) {
            System.out.println("В fileSettingsCollection " + i++ + " " + cfs.getFile().getAbsolutePath() + " size " + cfs.getSize());
        }
    }

    public void addInEnd(Collection<T> collection, int previousCollectionSize) {
        CollectionFileSettings lastCFS = filesSettingsCollection.get(filesSettingsCollection.size() - 1);
        if (lastCFS.getSize() == fileObjectCapacity) {
            addCFS();
            lastCFS = filesSettingsCollection.get(filesSettingsCollection.size() - 1);
        }

        Iterator<T> iterator = collection.iterator();
        for (int i = 0; i < previousCollectionSize - lastCFS.getSize(); i++)
            iterator.next(); // скип элементов до начала нужного файла

        fillFile(lastCFS, iterator, fileObjectCapacity);
        while (iterator.hasNext()) {
            addCFS();
            lastCFS = filesSettingsCollection.get(filesSettingsCollection.size() - 1);
            fillFile(lastCFS, iterator, fileObjectCapacity);
        }
        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }

    public void remove(int index, Collection<T> collection) {
        // получить cfs по индексу, перезаписать файл от getSumOfPreviousFilesSizes до cfs.getSize - 1
        CollectionFileSettings cfs = getCFSWithElement(index);
        Iterator<T> iterator = collection.iterator();
        int sumOfPreviousFilesSizes = getSumOfPreviousFilesSizes(cfs);
        for (int i = 0; i < sumOfPreviousFilesSizes; i++)
            iterator.next();
        fillFile(cfs, iterator, cfs.getSize() - 1);
        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }

    public void add(int index, Collection<T> collection, Collection<? extends T> insertedCollection) { // НАПИСАТЬ remove(int index) и вернуться
//        System.out.println("\nВставляем начиная с " + getCFSWithElement(index).getFile().getAbsolutePath());

        CollectionFileSettings cfs = getCFSWithElement(index);
        Iterator<T> iterator = collection.iterator();
        int sumOfPreviousFilesSizes = getSumOfPreviousFilesSizes(cfs);

        for (int i = 0; i < sumOfPreviousFilesSizes; i++)
            iterator.next(); // скип элементов до начала нужного файла

        int remains = insertedCollection.size() - (fileObjectCapacity - cfs.getSize()); // остаток, который не залез в файл

        fillFile(cfs, iterator, fileObjectCapacity);
        index += cfs.getSize();
        while (remains > 0 && iterator.hasNext()) {
            cfs = getCFSWithElement(index);
            if (cfs == null) {
                /* Если пришел null, значит не нашли файла по индексу.
                 *  Если последний файл полон, то добавляем новый, если не полный, то просто присваеваем последний файл */
                if (filesSettingsCollection.get(filesSettingsCollection.size() - 1).getSize() == fileObjectCapacity)
                    addCFS();
                cfs = filesSettingsCollection.get(filesSettingsCollection.size() - 1);
            }
            remains = remains - (fileObjectCapacity - cfs.getSize());
            fillFile(cfs, iterator, fileObjectCapacity);
            index += cfs.getSize();
        }

        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }

    public void clear() {
        for (File file :
                Objects.requireNonNull(directory.listFiles())) {
            file.delete();
        }

        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }

    public void set(int index, Collection<T> collection) {
        CollectionFileSettings cfs = getCFSWithElement(index);
        Iterator<T> iterator = collection.iterator();
        int sumOfPreviousFilesSizes = getSumOfPreviousFilesSizes(cfs);

        for (int i = 0; i < sumOfPreviousFilesSizes; i++)
            iterator.next();

        fillFile(cfs, iterator, cfs.getSize() + 1);
    }

    // тут нужно добавить хэш коды кусков в CFS

    private void removeAll(Collection<T> currentCollection, Collection<?> deletedCollection) {
        
    }

    private void retainAll() {

    }


    // ВНУТРЕННИЕ МЕТОДЫ ДЛЯ РАБОТЫ С ФАЙЛАМИ КОЛЛЕКЦИИ

    private void loadCollection(Collection<T> collection) {
        for (CollectionFileSettings fileSettings :
                filesSettingsCollection) {
            File file = fileSettings.getFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                while (true) {
                    try {
                        collection.add((T) ois.readObject());
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private int getSumOfPreviousFilesSizes(CollectionFileSettings cfs) {
        if (filesSettingsCollection.size() == 1) return 0;
        int sum = 0;
        for (CollectionFileSettings c : filesSettingsCollection) {
            if (c.getFile().getAbsolutePath().equals(cfs.getFile().getAbsolutePath())) break;
            sum += c.getSize();
        }
        return sum;
    }

    private CollectionFileSettings getCFSWithElement(int index) {
        /* Получить нужный объект по индексу.
         *  Не возвращает последний файл, если индекс выходит за границы коллекции, это нужно проверять отдельно */
        int sumOfCfsSizes = 0;
        if (index == 0) return filesSettingsCollection.get(0);
        CollectionFileSettings res = null;
        for (CollectionFileSettings cfs : filesSettingsCollection) {
            sumOfCfsSizes += cfs.getSize();
            if (sumOfCfsSizes > index) {
                res = cfs;
                break;
            }
        }
        return res;
    }

    private void writeFilesSettingsCollection() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileWithCollectionFiles))) {
            oos.writeObject(filesSettingsCollection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void optimizeFilesSettingsCollection() {
        for (CollectionFileSettings cfs :
                filesSettingsCollection) {
            if (cfs.getSize() == 0) {
                filesSettingsCollection.remove(cfs);
                new File(cfs.getFile().getAbsolutePath()).delete();
            }
        }
    }

    private void readFilesSettingsCollection() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileWithCollectionFiles))) {
            filesSettingsCollection = (List<CollectionFileSettings>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCFS() {
        try {
            filesSettingsCollection.add(new CollectionFileSettings(File.createTempFile(prefix, "", directory), 0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeFilesSettingsCollection();
    }

    private void fillFile(CollectionFileSettings cfs, Iterator<T> iterator, int upperBound) {
        int size = 0;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cfs.getFile()))) {
            for (int i = 0; i < upperBound && iterator.hasNext(); i++) {
                oos.writeObject(iterator.next());
                size++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        cfs.setSize(size);
    }
}


