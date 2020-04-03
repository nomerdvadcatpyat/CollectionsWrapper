package wrappers.collections;

import streams.ObjectStreamCreator;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionFilesManager<T extends Serializable> {
    private List<File> collectionFiles = new ArrayList<>(); // коллекция файлов
    private File fileWithCollectionFilesPaths; // путь к файлу с коллекциями
    private String collectionMainFilePath; // первый файл коллекции (с этим файлом коллекция изначально связана)

    private int collectionLastSize = 0; // последний размер коллекции до текущего изменения, должен обновляться в конце каждого изменения
    private int currentSize = 0; // текущий размер коллекции, берется в начале каждого метода из пришедшей коллекции
    private File lastFile;
    private int counter = 0; // указывает на номер след файла, который нужно создать

    private int fileObjectCapacity = 5; // максимальное количество элементов в файле


    public CollectionFilesManager(String collectionMainFilePath) { // создание менеджера, передача в конструктор пути к первому файлу коллекции
        this.collectionMainFilePath = collectionMainFilePath;
        fileWithCollectionFilesPaths = new File(collectionMainFilePath + "-files_storage"); // создание файла с путями к файлам коллекции
        if (!fileWithCollectionFilesPaths.exists()) { // если файла с путями к файлам не существовало, то создать и записать первый файл
            lastFile = new File(collectionMainFilePath);
            collectionFiles.add(lastFile);
            writePaths();
        } else loadCFM();
    }

    public void loadCollection(Collection<T> c) { // Загрузить коллекцию и обновить lastSize = размеру загруженной коллекции
        for (File file : collectionFiles) {  // считать элементы из всех файлов коллекции и добавлять в эту коллекцию
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                while (true) {
                    try {
                        c.add((T) ois.readObject());
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        collectionLastSize = c.size();
    }

    private File getFileWithElement(int index) { // получить id файла, в котором лежит элемент
        // номер файла с нуля
        int fileNumber = index / fileObjectCapacity; // int fileNumber = (index / fileObjectCapacity) + 1;
        return collectionFiles.get(fileNumber);
    }

    private int getFreeSpaceInLastFile() {
        int res = fileObjectCapacity;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(lastFile))){
            while (true) {
                try {
                     ois.readObject();
                     res--;
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }


    public void addInEnd(Collection<? extends T> collection, T value) { // добавить элемент в конец последнего файла коллекции
        currentSize = collection.size(); // текущая длинна коллекции с новым элементом
        if (getFreeSpaceInLastFile() == 0) {
            collectionFiles.add(new File(collectionMainFilePath + counter));//
            writePaths();                                                             // ВОЗМОЖНО СТОИТ ВЫНЕСТИ В МЕТОД
            counter++;                                                                //
        }
        File file = getFileWithElement(currentSize - 1); // получить файл с элементом (индексация с 0)
        try (ObjectOutputStream oos = ObjectStreamCreator.createStream(file)) {
            oos.writeObject(value); // записать значение в файл
        } catch (IOException e) {
            e.printStackTrace();
        }
        collectionLastSize = currentSize; // обновить размер коллекции до изменения
    }

    public void addAllInEnd(Collection<? extends T> collection, Collection<? extends T> newCollection) { /* Добавление новой коллекции
     в конец старой. */
        currentSize = collectionLastSize + newCollection.size(); // обновление текущего размера коллекции после изменения
        int countNewElements = currentSize - collectionLastSize; // количество новых элементов
        int freeSpaceInLastFile = getFreeSpaceInLastFile();//fileObjectCapacity - (collectionLastSize % fileObjectCapacity); // получение свободного места в файле
        if (new File(collectionMainFilePath + (counter-1)).length() != 0) freeSpaceInLastFile = 0; // если длина последнего файла не 0 то файл не пустой а полный
        File firstFile = getFileWithElement(collectionLastSize - 1); // первый файл куда дописываем
        if (freeSpaceInLastFile >= countNewElements) { /* Если свободного места в последнем файле больше, чем новых элементов в коллекции,
         то просто записать все элементы в этот файл и обновить lastSize*/
            try (ObjectOutputStream oos = ObjectStreamCreator.createStream(firstFile)) {
                for (T value : newCollection) {
                    oos.writeObject(value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            /* Тогда заполнить оставшийся файл, обновить ластКолекшнСайз
             и ПОКА (currentSize > lastCollectionSize + fileObjectCapacity) заполнять новые файлы,
             в конце цикла прибавляя lastCollectionSize += fileObjectCapacity.
             После того, как условие не выполнится, остатки нужно записать либо в последний файл */
            Iterator<? extends T> iterator = newCollection.iterator(); // получить итератор новой коллекции
            try (ObjectOutputStream oos = ObjectStreamCreator.createStream(firstFile)) {// Заполняем первый файл
                for (int i = 0; i < freeSpaceInLastFile; i++) oos.writeObject(iterator.next());
                collectionLastSize += freeSpaceInLastFile; // обновляем размер коллекции до изменения
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (currentSize >= collectionLastSize + fileObjectCapacity) { // Заполняем промежуточные файлы
                // создать новый файл
                // записать в него fileObjectCapacity элементов
                // обновить lastCollectionSize+= fileObjectCapacity
                File file = new File(collectionMainFilePath + counter);
                collectionFiles.add(file);
                writePaths();
                counter++;
                try (ObjectOutputStream oos = ObjectStreamCreator.createStream(file)) {
                    for (int i = 0; i < fileObjectCapacity; i++) oos.writeObject(iterator.next());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                collectionLastSize += fileObjectCapacity;
            }

            // Заполняем остатки в последний файл
            // остатки это currentSize - collectionLastSize
            countNewElements = currentSize - collectionLastSize;
            if (countNewElements > 0) {
                File file = new File(collectionMainFilePath + counter);
                collectionFiles.add(file);
                writePaths();
                counter++;
                try (ObjectOutputStream oos = ObjectStreamCreator.createStream(file)) {
                    for (int i = 0; i < countNewElements; i++) {
                        oos.writeObject(iterator.next());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        collectionLastSize = currentSize;
    }

    public void addFromIndex(Collection<? extends T> collection, int index) {
        // найти первый измененный файл и начиная с него все перезаписывать
        int positionInFile = index % fileObjectCapacity; // позиция элемента в файле с 0
        int fileNumber = index / fileObjectCapacity; // номер файла с 0
        System.out.println("fn " + fileNumber);
        System.out.println("counter " + counter);
        Iterator<? extends T> iterator = collection.iterator();
        for (int i = 0; i < index - positionInFile; i++)
            iterator.next(); // скипнуть элементы, которые не нужно перезаписывать
        Collection<T> res = new ArrayList<>();
        while (iterator.hasNext()) res.add(iterator.next()); // тут все правильно считает
        for (int i = fileNumber; i <= counter; i++) {
            File file = collectionFiles.get(i);
            System.out.println(file.getAbsolutePath());
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                pw.write("");
                System.out.println(file.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        addAllInEnd(collection, res);
    }

    public void addAllFromIndex(Collection<? extends T> collection, int index) {

    }

    public void insertFromIndex(int index) {

    }

    public void remove(int index) {

    }

    public void removeAll() {
        // тут нужно будет перезаписывать все файлы
    }

    private void writePaths() {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileWithCollectionFilesPaths));
        ) {
            oos.writeObject(collectionFiles);
            System.out.println("write " + counter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCFM() {
        try (
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileWithCollectionFilesPaths))
        ) {
            collectionFiles = (List<File>) ois.readObject();
            counter = collectionFiles.size() - 1;
            collectionLastSize = collectionFiles.size();
            currentSize = collectionFiles.size();
            System.out.println("counter in load " + counter);
            if(counter != 0) lastFile = new File(collectionMainFilePath + "" + (counter-1));
            else lastFile = new File(collectionMainFilePath);
            System.out.println("lastFile " + lastFile.getAbsolutePath());
            System.out.println("read " + collectionFiles.size() + " counter " + counter);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
