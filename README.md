# CollectionsWrapper
## Пример использования:
**List< T extends Serializable > list = new ListWrapper([реализация листа] new ArrayList(),  
[директория, куда будут сохраняться файлы коллекции] directory, [префикс файлов коллекции] prefix,  
[максимальный размер файлов коллекции] fileObjectCapacity );**

в классе T должны быть переопределены методы equals и hashCode.  
Так же он должен реализовывать интерфейс Serializable.
