package wrappers.collections;

import java.io.*;
import java.util.*;

/// \defgroup cfs Настройки файлов коллекций

/// \ingroup cfs

/// \brief Класс, отвечающий за управление файлами коллекций.
public class CollectionFilesManager<T extends Serializable> {
    private List<CollectionFileSettings> CFSCollection = new ArrayList<>(); ///< Коллекция конфигурацонных объектов, связанных с файлами коллекции
    private String prefix;
    private File directory;
    private File fileWithCollectionFiles; ///< Файл, в котором содержатся пути до файлов с частями коллекции
    private int fileObjectCapacity;
    private int changesCounter;

    /**
     *
     * @param collection Объект стандартной реализации нужной коллекции
     * @param directory Директория, в которую будут размещены файлы
     * @param prefix Префикс генерируемых файлов для переданной коллекции
     * @param fileObjectCapacity Максимальный размер одного файла с объектами коллекции
     * @param changesCounter Количество изменений коллекции, после которых нужно объеденить файлы с малым количеством элементов
     */
    public CollectionFilesManager(Collection<T> collection, File directory, String prefix, int fileObjectCapacity, int changesCounter) {
        this.fileObjectCapacity = fileObjectCapacity;
        this.changesCounter = changesCounter;
        this.prefix = prefix;
        this.directory = directory;
        fileWithCollectionFiles = new File(directory + File.separator + prefix + "-files_storage");
        if (fileWithCollectionFiles.length() != 0) {
            readFilesSettingsCollection();
            compressFiles();
            loadFullCollection(collection);
        }
    }


    /** \brief Добавление 1 элемента в конец коллекции в файлах.
     *
     * @param element Элемент, который нужно добавить в коллекцию
     *
     * Получает последний неполный файл. Если он не пустой, то загружает из него коллекцию и добавляет к ней элемент.
     * Если пустой, то только добавляет новый элемент.
     * Записывает коллекцию в файл.*/
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

        changesCounter++;
        optimizeAndWriteInFileCFSCollection();
    }

    /** \brief Добавление коллекции в конец коллекции в файлах.
     *
     * @param insertedCollection Коллекция, которую нужно добавить к существующей
     *
     * Получает последний неполный файл.
     * Если коллекция в файле не пустая, то загружает ее.
     * Добавляет к текущей коллекции в файле вставляемую и заполняет файл.
     * Пока остались элементы, заполняет новые файлы.*/
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

        changesCounter++;
        optimizeAndWriteInFileCFSCollection();
    }


    /** \brief Удаление элемента по индексу.
     *
     * @param index индекс удаляемого элемента
     *
     * Получает файл, в котором лежит удаляемый элемент.
     * Загружает из него коллекцию, получает индекс относительно этой коллекции.
     * Удаляет элемент из этой коллекции и записывает ее в файл.*/
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

        changesCounter++;
        optimizeAndWriteInFileCFSCollection();
    }

    /** \brief Вставка коллекции по индексу.
     *
     * @param index индекс, начиная с которого нужно вставить коллекцию
     * @param insertedCollection вставляемая коллекция
     *
     * Получает файл по индексу. Если он не пустой, загружает из него коллекцию.
     * Получает индекс относительно этого файла и добавляет вставленную коллекцию в коллекцию из файла.
     * Вычисляет сколько элементов не влезло в файл, заполняет файл.
     * Пока остаток > 0:
     *   1) Если остаток залезет в следующий файл, то засовывает его туда.
     *   2) Если не залезет в след файл, то создает между ними еще один (чтобы не переписывать много файлов) и заполняет его.
     *   3) Обновляет остаток и текущий файл = следующий файл.
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
                    nextCfs = CFSCollection.get(CFSCollection.indexOf(cfs) + 1);

                if (nextCfs != null && fileObjectCapacity - nextCfs.getSize() >= remains) {
                    Collection<T> resFileCollection = new ArrayList<>();
                    Collection<T> oldFileCollection = loadSubCollection(nextCfs);
                    while (iterator.hasNext()) resFileCollection.add(iterator.next());
                    resFileCollection.addAll(oldFileCollection);

                    fillFile(nextCfs, resFileCollection.iterator(), fileObjectCapacity);
                } else {
                    addCFS(CFSCollection.indexOf(cfs) + 1);
                    nextCfs = CFSCollection.get(CFSCollection.indexOf(cfs) + 1);

                    fillFile(nextCfs, iterator, Math.min(fileObjectCapacity, remains));
                }
                remains = remains - cfs.getSize();
                cfs = nextCfs;
            }
        }

        changesCounter++;
        optimizeAndWriteInFileCFSCollection();
    }

    /** \brief Вставка 1 элемента по индексу.
     *
     * @param index индекс
     * @param element элемент, который нужно вставить
     *
     * Все то же самое, что и со вставкой коллекции */
    public void add(int index, T element) {
        addAll(index, Collections.singletonList(element));
    }


    /** \brief Удаление всех элементов из коллекции
     * Выставляем объектам файлов размер 0 и в optimizeFilesSettingsCollection они вычищаются. */
    private void clearAll() {
        for (CollectionFileSettings cfs : CFSCollection) cfs.setSize(0);

        changesCounter = 0;
        optimizeAndWriteInFileCFSCollection();
    }


    /** \brief Изменение элемента по индексу
     *
     * @param index индекс элемента, который нужно заменить
     * @param element элемент, которым нужно заменить старый
     *
     * Получает коллекцию из файла, где лежит элемент по индексу.
     * Кастит ее к листу, делает сет и загружает обратно.
     * */
    public void set(int index, T element) {
        CollectionFileSettings cfs = getCFSWithElement(index);

        List<T> fileCollection = (List<T>) loadSubCollection(cfs);
        index = index - getSumOfPreviousFilesSizes(cfs);
        fileCollection.set(index, element);

        fillFile(cfs, fileCollection.iterator(), cfs.getSize());
    }

    /** \brief Замена всех вхождений коллекции.
     *
     * @param newCollection новая коллекция
     *
     * Очищает коллекцию в файлах и засовывает новую.
     * */
    public void replaceAll(Collection<T> newCollection) {
        clearAll();
        addAllInEnd(newCollection);
    }


    /** \brief Удаление различий у коллекции из файлов и измененной коллекции.
     *
     * @param collection измененная коллекция
     *
     * */
    public void removeDifference(Collection<T> collection) {
        List<T> copyCollection = new ArrayList<>(collection);

        if (copyCollection.isEmpty()) {
            clearAll();
            return;
        }

        // Здесь мы не перезаписываем все файлы коллекции. Только те, элементы в которых были изменены.
        for (CollectionFileSettings cfs : CFSCollection) {
            removeDifference(copyCollection, cfs);
        }

        changesCounter++;
        optimizeAndWriteInFileCFSCollection();
    }


    // ВНУТРЕННИЕ МЕТОДЫ ДЛЯ РАБОТЫ С ФАЙЛАМИ КОЛЛЕКЦИИ

    /** \brief Удаление различий у коллекции из файла и измененной коллекции.
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
        if (CFSCollection.isEmpty()) addCFS();
        return CFSCollection.get(CFSCollection.size() - 1);
    }

    private void loadFullCollection(Collection<T> collection) {
        for (CollectionFileSettings cfs : CFSCollection) {
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
        if (CFSCollection.size() == 1) return 0;
        int sum = 0;
        for (CollectionFileSettings c : CFSCollection) {
            if (c.getFile().getAbsolutePath().equals(cfs.getFile().getAbsolutePath())) break;
            sum += c.getSize();
        }
        return sum;
    }

    private int getSumOfAllFilesSizes() {
        int sum = 0;
        for (CollectionFileSettings c : CFSCollection)
            sum += c.getSize();
        return sum;
    }

    private CollectionFileSettings getCFSWithElement(int index) {
        int sumOfCfsSizes = 0;
        if (index == 0) {
            if (CFSCollection.isEmpty()) addCFS();
            return CFSCollection.get(0);
        }

        CollectionFileSettings res = null;
        for (CollectionFileSettings cfs : CFSCollection) {
            sumOfCfsSizes += cfs.getSize();
            if (sumOfCfsSizes > index) {
                res = cfs;
                break;
            }
        }
        return res;
    }

    private void writeFilesSettingsCollection() {
        if (CFSCollection.isEmpty()) {
            try (PrintWriter writer = new PrintWriter(fileWithCollectionFiles)) {
                writer.print("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileWithCollectionFiles))) {
                oos.writeObject(CFSCollection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void optimizeAndWriteInFileCFSCollection() {
        removeEmptyFiles();
        if (changesCounter >= 20)
            compressFiles();
    }

    private void compressFiles() {
        for (int i = 0; i < CFSCollection.size() - 1; i++) {
            // бежать по цфсам и смотреть: если текущий файл может засунуть в себя следующий, то делать это
            CollectionFileSettings cfs = CFSCollection.get(i);
            CollectionFileSettings nextCfs = CFSCollection.get(i + 1);

            if (cfs.getSize() + nextCfs.getSize() <= fileObjectCapacity) {
                Collection<T> fileCollection = loadSubCollection(cfs);
                fileCollection.addAll(loadSubCollection(nextCfs));
                fillFile(cfs, fileCollection.iterator(), fileObjectCapacity);

                CFSCollection.remove(nextCfs);
                new File(nextCfs.getFile().getAbsolutePath()).delete();
            }
        }
        writeFilesSettingsCollection();
    }

    private void removeEmptyFiles() {
        Collection<CollectionFileSettings> removeCollection = new ArrayList<>();
        for (CollectionFileSettings cfs : CFSCollection) {
            if (cfs.getSize() == 0) {
                removeCollection.add(cfs);
                new File(cfs.getFile().getAbsolutePath()).delete();
            }
        }
        CFSCollection.removeAll(removeCollection);
        writeFilesSettingsCollection();
    }

    private void readFilesSettingsCollection() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileWithCollectionFiles))) {
            CFSCollection = (List<CollectionFileSettings>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCFS() {
        try {
            CFSCollection.add(new CollectionFileSettings(File.createTempFile(prefix, "", directory), 0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeFilesSettingsCollection();
    }

    private void addCFS(int index) {
        try {
            CFSCollection.add(index, new CollectionFileSettings(File.createTempFile(prefix, "", directory), 0));
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


