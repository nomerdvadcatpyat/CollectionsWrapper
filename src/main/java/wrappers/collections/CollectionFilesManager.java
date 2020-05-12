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


    /* Добавление 1 элемента в конец коллекции в файлах.
     * Получам последний неполный файл. Если он не пустой, то загружаем из него коллекцию и добавляем к ней элемент.
     * Если пустой, то только добавляем новый элемент.
     * Записываем коллекцию в файл.*/
    public void addInEnd(T element) {
        CollectionFileSettings lastCFS = getLastCFS();
        Collection<T> fileCollection = new ArrayList<>();

        if (lastCFS.getSize() == fileObjectCapacity) {
            addCFS();
            lastCFS = getLastCFS();
        }

        if (lastCFS.getSize() != 0) fileCollection = loadSubCollection(lastCFS);
        fileCollection.add(element);

        fillFile(lastCFS, fileCollection.iterator(), fileObjectCapacity);

        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }

    /* Добавление коллекции в конец коллекции в файлах.
     * Получаем последний неполный файл.
     * Если коллекция в файле не пустая, то загрузить ее.
     * Добавляем к текущей коллекции в файле вставляемую и заполняем файл.
     * Пока остались элементы, заполняем новые файлы.*/
    public void addAllInEnd(Collection<? extends T> insertedCollection) {
        CollectionFileSettings lastCFS = getLastCFS();
        if (lastCFS.getSize() == fileObjectCapacity) {
            addCFS();
            lastCFS = getLastCFS();
        }

        Collection<T> fileCollection = new ArrayList<>();
        if (lastCFS.getSize() != 0)
            fileCollection = loadSubCollection(lastCFS);
        fileCollection.addAll(insertedCollection);

        Iterator<T> iterator = fileCollection.iterator();

        fillFile(lastCFS, iterator, fileObjectCapacity);
        while (iterator.hasNext()) {
            addCFS();
            lastCFS = getLastCFS();
            fillFile(lastCFS, iterator, fileObjectCapacity);
        }

        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }


    /* Удалить элемент по индексу.
     * Получаем файл, в котором лежит удаляемый элемент.
     * Загружаем из него коллекцию, получаем индекс относительно этой коллекции.
     * Удаляем элемент из этой коллекции и записываем ее в файл.*/
    public void remove(int index) {
        CollectionFileSettings cfs = getCFSWithElement(index);
        Collection<T> fileCollection = loadSubCollection(cfs);
        index = index - getSumOfPreviousFilesSizes(cfs);

        Iterator<T> iterator = fileCollection.iterator();
        iterator.next();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        iterator.remove();

        iterator = fileCollection.iterator();
        fillFile(cfs, iterator, cfs.getSize() - 1);

        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }

    /* Вставить коллекцию по индексу.
     * Получаем файл по индексу. Если он не пустой, загружаем из него коллекцию.
     * Получаем индекс относительно этого файла и добавляем вставленную коллекцию в коллекцию из файла.
     * Вычисляем сколько элементов не влезло в файл, заполняем файл.
     * Пока остаток > 0:
     *   1) Если остаток залезет в следующий файл, то засовываем его туда.
     *   2) Если не залезет в след файл, то создадим между ними еще один (чтобы не переписывать много файлов) и заполним его.
     *   3) обновляем остаток и текущий файл = следующий файл.
     * */
    public void addAll(int index, Collection<? extends T> insertedCollection) {
        if (index == getSumOfAllFilesSizes())
            addAllInEnd(insertedCollection);
        else {
            CollectionFileSettings cfs = getCFSWithElement(index);

            Collection<T> fileCollection = new ArrayList<>();
            if (cfs.getSize() != 0)
                fileCollection = loadSubCollection(cfs);

            index = index - getSumOfPreviousFilesSizes(cfs);
            ((List<T>) fileCollection).addAll(index, insertedCollection);

            Iterator<T> iterator = fileCollection.iterator();

            int remains = insertedCollection.size() - (fileObjectCapacity - cfs.getSize());

            fillFile(cfs, iterator, fileObjectCapacity);

            while (remains > 0 && iterator.hasNext()) {
                CollectionFileSettings nextCfs = null;
                if (!cfs.equals(getLastCFS()))
                    nextCfs = filesSettingsCollection.get(filesSettingsCollection.indexOf(cfs) + 1);

                if (nextCfs != null && fileObjectCapacity - nextCfs.getSize() >= remains) {
                    Collection<T> resFileCollection = new ArrayList<>();
                    Collection<T> oldFileCollection = loadSubCollection(nextCfs);
                    while (iterator.hasNext()) resFileCollection.add(iterator.next());
                    resFileCollection.addAll(oldFileCollection);

                    fillFile(nextCfs, resFileCollection.iterator(), fileObjectCapacity);
                } else {
                    addCFS(filesSettingsCollection.indexOf(cfs) + 1);
                    nextCfs = filesSettingsCollection.get(filesSettingsCollection.indexOf(cfs) + 1);

                    fillFile(nextCfs, iterator, Math.min(fileObjectCapacity, remains));
                }
                remains = remains - cfs.getSize();
                cfs = nextCfs;
            }
        }
        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }

    /*Вставка 1 элемента по индексу.
     * Все то же самое, что и со вставкой коллекции */
    public void add(int index, T element) {
        addAll(index, Collections.singletonList(element));
    }


    /* Очистить все.
     * Выставляем объектам файлов размер 0 и в optimizeFilesSettingsCollection они вычищаются. */
    private void clearAll() {
        for (CollectionFileSettings cfs : filesSettingsCollection) cfs.setSize(0);

        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }


    /* Вставка коллекции по индексу
     * Получаем коллекцию из файла, где лежит элемент по индексу.
     * Кастим ее к листу, делаем сет и загружаем обратно.
     * */
    public void set(int index, T element) {
        CollectionFileSettings cfs = getCFSWithElement(index);

        List<T> fileCollection = (List<T>) loadSubCollection(cfs);
        index = index - getSumOfPreviousFilesSizes(cfs);
        fileCollection.set(index, element);

        fillFile(cfs, fileCollection.iterator(), cfs.getSize());
    }

    /* Заменить все вхождения.
    * Очищаем коллекцию в файлах и засовываем новую.
    * */
    public void replaceAll(Collection<T> newCollection) {
        clearAll();
        addAllInEnd(newCollection);
    }


    /* Удаление различий у коллекции из файлов и измененной коллекции. */
    public void removeDifference(Collection<T> collection) {
        List<T> copyCollection = new ArrayList<>(collection);

        if (copyCollection.isEmpty()) {
            clearAll();
            return;
        }

        // Здесь мы не перезаписываем все файлы коллекции. Только те, элементы в которых были изменены.
        for (CollectionFileSettings cfs : filesSettingsCollection) {
            removeDifference(copyCollection, cfs);
        }

        optimizeFilesSettingsCollection();
        writeFilesSettingsCollection();
    }


    // ВНУТРЕННИЕ МЕТОДЫ ДЛЯ РАБОТЫ С ФАЙЛАМИ КОЛЛЕКЦИИ

    /* Удаление различий у коллекции из файла и измененной коллекции.
    * subCollection - подколлекция измененной коллекции
    * если хэши у старой коллекции из файла и подколлекции измененной коллекции не совпали, то записываем в файл измененную подколлекцию
    * */
    private void removeDifference(List<T> copyCollection, CollectionFileSettings cfs) {
        List<T> subCollection = new ArrayList<>();
        for (int i = 0; i < Math.min(cfs.getSize(), copyCollection.size()); i++) //subcol = copyCol(0..min(copyCol.size, cfs.getSize)
            subCollection.add(copyCollection.get(i)); // заполняем подколлекцию, размером либо с коллекцию в файле, либо с остаток в copyCol

        if (cfs.getCollectionHash() != subCollection.hashCode()) {
            List<T> oldFileCollection = (List<T>) loadSubCollection(cfs);
            List<T> newFileCollection = new ArrayList<>();

            if (subCollection.size() == 0)
                cfs.setSize(0);

            for (int i = 0; i < subCollection.size(); i++) {
                while (!oldFileCollection.isEmpty()) {
                    if (subCollection.get(i).equals(oldFileCollection.get(0))) {
                        newFileCollection.add(oldFileCollection.get(0));
                        oldFileCollection.remove(0);
                        break;
                    } else oldFileCollection.remove(0);
                }

                if (oldFileCollection.isEmpty() || i == subCollection.size() - 1) {
                    fillFile(cfs, newFileCollection.iterator(), newFileCollection.size());
                    copyCollection.subList(0, newFileCollection.size()).clear();
                    break;
                }
            }
        } else copyCollection.subList(0, subCollection.size()).clear();
    }

    private CollectionFileSettings getLastCFS() {
        if (filesSettingsCollection.isEmpty()) addCFS();
        return filesSettingsCollection.get(filesSettingsCollection.size() - 1);
    }

    private void loadFullCollection(Collection<T> collection) {
        for (CollectionFileSettings cfs : filesSettingsCollection) {
            collection.addAll(loadSubCollection(cfs));
        }
    }

    private Collection<T> loadSubCollection(CollectionFileSettings cfs) {
        File file = cfs.getFile();
        Collection<T> collection = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            collection = (Collection<T>) ois.readObject();
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

    private int getSumOfAllFilesSizes() {
        int sum = 0;
        for (CollectionFileSettings c : filesSettingsCollection)
            sum += c.getSize();
        return sum;
    }

    private CollectionFileSettings getCFSWithElement(int index) {
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

    private void addCFS(int index) {
        try {
            filesSettingsCollection.add(index, new CollectionFileSettings(File.createTempFile(prefix, "", directory), 0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeFilesSettingsCollection();
    }

    private void fillFile(CollectionFileSettings cfs, Iterator<T> iterator, int upperBound) {
        Collection<T> fileCollection = new ArrayList<>();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cfs.getFile()))) {
            for (int i = 0; i < upperBound && iterator.hasNext(); i++)
                fileCollection.add(iterator.next());
            oos.writeObject(fileCollection);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        cfs.setSize(fileCollection.size());
        cfs.setCollectionHash(fileCollection.hashCode());
    }
}


