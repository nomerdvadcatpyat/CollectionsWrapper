package wrappers.collections;

import streams.ObjectStreamCreator;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionFilesManager<T extends Serializable> {
    private List<File> collectionFiles = new ArrayList<>();
    private File fileWithCollectionFilesPaths;
    private String collectionMainFilePath;
    private int collectionLastSize = 0;
    private int currentSize = 0;
    private int fileObjectCapacity = 20;
    private int counter = 1;


    public CollectionFilesManager(String collectionMainFilePath) {
        this.collectionMainFilePath = collectionMainFilePath;
        fileWithCollectionFilesPaths = new File(collectionMainFilePath + "-files_storage");
        if (!fileWithCollectionFilesPaths.exists()) {
            collectionFiles.add(new File(collectionMainFilePath));
            addPath();
        } else readPaths();
    }

    public void loadCollection(Collection<T> c) {
        List<File> files = getCollectionFiles();
        for (File file:
             files) {
            System.out.println(file.getAbsolutePath());
        }
        for (File file :
                files) {
            try (
                    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))
            ) {
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
    }

    public List<File> getCollectionFiles() {
        return collectionFiles;
    }

    public File getFileWithElement(int index) {
        // номер файла с нуля
        int fileNumber = index / fileObjectCapacity;
        return collectionFiles.get(fileNumber);
    }


// старая реализация апдейт
/*
    public void update(Collection collection) {
        int currentSize = collection.size();
        boolean isChanged = false;
        // не обязательно добавлять в конец
        while (currentSize >= lastCollectionSize + fileObjectCapacity) {
            collectionFiles.add(new File(collectionMainFilePath + counter));
            isChanged = true;
            counter++;
            lastCollectionSize += fileObjectCapacity;
        }
        int elementsInLastFile = currentSize - lastCollectionSize;
        lastCollectionSize = collection.size();

        if (elementsInLastFile == 0) {
            collectionFiles.remove(collectionFiles.size() - 1);
            isChanged = true;
            counter--;
        }

        if (isChanged) {
            for (File file :
                    collectionFiles) {
                try (
                        BufferedWriter fw = new BufferedWriter(new FileWriter(fileWithCollectionFilesPaths, true))
                ) {
                    fw.write(file.getAbsolutePath() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/


    public void addInEnd(Collection<? extends T> collection, T value) {
        currentSize = collection.size();
        if (collectionLastSize % fileObjectCapacity == fileObjectCapacity - 1) {
            collectionFiles.add(new File(collectionMainFilePath + counter));
            addPath();
            counter++;
        }
        collectionLastSize = currentSize;
        File file = getFileWithElement(currentSize - 1);
        try (
                ObjectOutputStream oos = ObjectStreamCreator.createStream(file)
        ) {
            oos.writeObject(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAllInEnd(Collection<? extends T> collection) {
        currentSize = collection.size();
        int countNewElements = currentSize - collectionLastSize;
        // неправильно считаю, если freeSpaceInLastFile == fileObjectCapacity то последний файл полон, либо пуст (может ли такое быть? - наверн нет)
        // сделать: если freeSpaceInLastFile == fileObjectCapacity то создать новый файл и туда писать
        int freeSpaceInLastFile = fileObjectCapacity - (collectionLastSize % fileObjectCapacity);
        freeSpaceInLastFile = freeSpaceInLastFile == fileObjectCapacity ? 0 : freeSpaceInLastFile;

        // Если свободного места в последнем файле больше, чем новых элементов в коллекции
        if (freeSpaceInLastFile >= countNewElements) {
            File file = getFileWithElement(collectionLastSize - 1);
            try (ObjectOutputStream oos = ObjectStreamCreator.createStream(file)) {
                for (T value : collection) {
                    oos.writeObject(value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            /*
             Тогда заполнить оставшийся файл, обновить ластКолекшнСайз и ПОКА (currentSize > lastCollectionSize + fileObjectCapacity) заполнять новые файлы,
             в конце цикла прибавляя lastCollectionSize += fileObjectCapacity.
             После того, как условие не выполнится, остатки нужно записать либо в последний файл, либо в последний и еще один
            */
            Iterator<? extends T> iterator = collection.iterator();
            // Заполняем первый файл
            File firstFile = getFileWithElement(collectionLastSize - 1);
            try (ObjectOutputStream oos = ObjectStreamCreator.createStream(firstFile)) {
                for (int i = 0; i < freeSpaceInLastFile; i++) {
                    oos.writeObject(iterator.next());
                }
                collectionLastSize += freeSpaceInLastFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Заполняем промежуточные файлы
            while (currentSize > collectionLastSize + fileObjectCapacity) {
                // создать новый файл
                // записать в него fileObjectCapacity элементов
                // обновить lastCollectionSize+= fileObjectCapacity
                File file = new File(collectionMainFilePath + counter);
                collectionFiles.add(file);
                addPath();
                counter++;
                try (ObjectOutputStream oos = ObjectStreamCreator.createStream(file)) {
                    for (int i = 0; i < fileObjectCapacity; i++) {
                        oos.writeObject(iterator.next());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                collectionLastSize += fileObjectCapacity;
            }

            // Заполняем остатки в последний файл
            // остатки это currentSize - collectionLastSize
            countNewElements = currentSize - collectionLastSize;
            File file = new File(collectionMainFilePath + counter);
            collectionFiles.add(file);
            addPath();
            counter++;
            try (ObjectOutputStream oos = ObjectStreamCreator.createStream(file)) {
                for (int i = 0; i < countNewElements; i++) {
                    oos.writeObject(iterator.next());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        collectionLastSize = currentSize;
    }

    private void addFromIndex() {

    }

    private void insertFromIndex(int index) {

    }

    private void remove(int index) {

    }

    private void removeAll() {
        // тут нужно будет перезаписывать все файлы
    }

    public void addPath() {
        try (
                BufferedWriter fw = new BufferedWriter(new FileWriter(fileWithCollectionFilesPaths,true))
        ){
            fw.write(collectionMainFilePath + counter + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readPaths(){
        try(
                BufferedReader fr = new BufferedReader(new FileReader(fileWithCollectionFilesPaths))
                ){
            String path;
            while ((path = fr.readLine()) != null)
            collectionFiles.add(new File(path));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
