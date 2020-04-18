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
            loadFullCollection(collection);
        }
    }

    public void addInEnd(Collection<T> collection, int previousCollectionSize) {
        CollectionFileSettings lastCFS = getLastCFS();
        if (lastCFS.getSize() == fileObjectCapacity) {
            addCFS();
            lastCFS = getLastCFS();
        }

        Iterator<T> iterator = collection.iterator();
        for (int i = 0; i < previousCollectionSize - lastCFS.getSize(); i++)
            iterator.next(); // скип элементов до начала нужного файла

        fillFile(lastCFS, iterator, fileObjectCapacity);
        while (iterator.hasNext()) {
            addCFS();
            lastCFS = getLastCFS();
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
                if (getLastCFS().getSize() == fileObjectCapacity)
                    addCFS();

                cfs = getLastCFS();
            }
            remains = remains - (fileObjectCapacity - cfs.getSize());

            fillFile(cfs, iterator, fileObjectCapacity);
            index += cfs.getSize();
        }

        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }

    private void clearAll() {
        for (CollectionFileSettings cfs : filesSettingsCollection) cfs.setSize(0);

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


    public void checkDifference(Collection<T> collection) {
        List<T> copyCollection = new ArrayList<>(collection);

        if (copyCollection.isEmpty()) {
            clearAll();
            return;
        }

        for (CollectionFileSettings cfs : filesSettingsCollection) {

            List<T> subCollection = new ArrayList<>();
            for (int i = 0; i < Math.min(cfs.getSize(), copyCollection.size()); i++) // subcol = copyCol(0..min(copyCol.size, cfs.getSize)
                subCollection.add(copyCollection.get(i)); //subCollection.add(iterator.next()); // заполняем подколлекцию, размером либо с коллекцию в файле, либо с остаток в copyCol

            if (cfs.getCollectionHash() != subCollection.hashCode()) {
                System.out.println("хэши не совпали");
                List<T> ccfs = loadSubCollection(cfs);
                List<T> temp = new ArrayList<>();

                for (int i = 0; i < subCollection.size(); i++) {

                    while (!ccfs.isEmpty()) {
                        if (subCollection.get(i).equals(ccfs.get(0))) {
                            temp.add(ccfs.get(0));
                            ccfs.remove(0);
                            break;
                        } else ccfs.remove(0);
                    }

                    if (ccfs.isEmpty() || i == subCollection.size() - 1) {
                        fillFile(cfs, temp.iterator(), temp.size());
                        copyCollection.subList(0, i).clear(); // тут либо + 1 либо нет залупа какая-то
                        break;
                    }
                }
            } else copyCollection.subList(0, subCollection.size()).clear();
        }

        // вызов этих 2 методов идет во всех методах, которые могут как-то повлиять на размер коллекции с файлами (удалить или добавить новый файл)
        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }

    public void retainAll() {

    }


    // ВНУТРЕННИЕ МЕТОДЫ ДЛЯ РАБОТЫ С ФАЙЛАМИ КОЛЛЕКЦИИ

    private CollectionFileSettings getLastCFS() {
        if (filesSettingsCollection.isEmpty()) addCFS();
        return filesSettingsCollection.get(filesSettingsCollection.size() - 1);
    }

    private void loadFullCollection(Collection<T> collection) {
        for (CollectionFileSettings cfs : filesSettingsCollection) {
           // System.out.println("Load file " + cfs.getFile().getAbsolutePath() + " size " + cfs.getSize() + " hash " + cfs.getCollectionHash());
            collection.addAll(loadSubCollection(cfs));
        }
       // System.out.println();
    }

    private List<T> loadSubCollection(CollectionFileSettings cfs) {
        File file = cfs.getFile();
        List<T> collection = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            collection = (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return collection;
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
        if (index == 0) {
            if (filesSettingsCollection.isEmpty()) addCFS();
            return filesSettingsCollection.get(0);
        }
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
        if (filesSettingsCollection.isEmpty()) {
            try (PrintWriter writer = new PrintWriter(fileWithCollectionFiles)) {
                writer.print("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileWithCollectionFiles))) {
                oos.writeObject(filesSettingsCollection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void optimizeFilesSettingsCollection() {
        Collection<CollectionFileSettings> removeCollection = new ArrayList<>();
        for (CollectionFileSettings cfs : filesSettingsCollection) {
            if (cfs.getSize() == 0) {
                removeCollection.add(cfs);
                new File(cfs.getFile().getAbsolutePath()).delete();
            }
        }
        filesSettingsCollection.removeAll(removeCollection);
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
        List<T> fileCollection = new ArrayList<>();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cfs.getFile()))) {
            for (int i = 0; i < upperBound && iterator.hasNext(); i++) fileCollection.add(iterator.next());
            oos.writeObject(fileCollection);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        cfs.setSize(fileCollection.size());
        cfs.setCollectionHash(fileCollection.hashCode());

       // System.out.println("Filled file " + cfs.getFile().getAbsolutePath() + " size " + cfs.getSize() + " hash " + cfs.getCollectionHash());
    }
}


