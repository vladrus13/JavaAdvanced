# Тесты к курсу «Технологии Java»

[Условия домашних заданий](http://www.kgeorgiy.info/courses/java-advanced/homeworks.html)


## Домашнее задание 10. HelloUDP

Интерфейсы

 * `HelloUDPClient` должен реализовывать интерфейс
    [HelloClient](modules/info.kgeorgiy.java.advanced.hello/info/kgeorgiy/java/advanced/hello/HelloClient.java)
 * `HelloUDPServer` должен реализовывать интерфейс
    [HelloServer](modules/info.kgeorgiy.java.advanced.hello/info/kgeorgiy/java/advanced/hello/HelloServer.java)

Тестирование

 * простой вариант:
    * клиент:
        ```info.kgeorgiy.java.advanced.hello client <полное имя класса>```
    * сервер:
        ```info.kgeorgiy.java.advanced.hello server <полное имя класса>```
 * сложный вариант:
    * на противоположной стороне находится система, дающая ответы на различных языках
    * клиент:
        ```info.kgeorgiy.java.advanced.hello client-i18n <полное имя класса>```
    * сервер:
        ```info.kgeorgiy.java.advanced.hello server-i18n <полное имя класса>```
 * продвинутый вариант:
    * на противоположной стороне находится старая система,
      не полностью соответствующая последней спецификации
    * клиент:
        ```info.kgeorgiy.java.advanced.hello client-evil <полное имя класса>```
    * сервер:
        ```info.kgeorgiy.java.advanced.hello server-evil <полное имя класса>```

Исходный код тестов:

* [Клиент](modules/info.kgeorgiy.java.advanced.hello/info/kgeorgiy/java/advanced/hello/HelloClientTest.java)
* [Сервер](modules/info.kgeorgiy.java.advanced.hello/info/kgeorgiy/java/advanced/hello/HelloServerTest.java)


## Домашнее задание 9. Web Crawler

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.crawler easy <полное имя класса>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.crawler hard <полное имя класса>```

Исходный код тестов:

* [интерфейсы и вспомогательные классы](modules/info.kgeorgiy.java.advanced.crawler/info/kgeorgiy/java/advanced/crawler/)
* [простой вариант](modules/info.kgeorgiy.java.advanced.crawler/info/kgeorgiy/java/advanced/crawler/CrawlerEasyTest.java)
* [сложный вариант](modules/info.kgeorgiy.java.advanced.crawler/info/kgeorgiy/java/advanced/crawler/CrawlerHardTest.java)


## Домашнее задание 8. Параллельный запуск

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.mapper scalar <полное имя класса>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.mapper list <полное имя класса>```
 * продвинутый вариант:
    ```info.kgeorgiy.java.advanced.mapper advanced <полное имя класса>```

Исходный код тестов:

* [простой вариант](modules/info.kgeorgiy.java.advanced.mapper/info/kgeorgiy/java/advanced/mapper/ScalarMapperTest.java)
* [сложный вариант](modules/info.kgeorgiy.java.advanced.mapper/info/kgeorgiy/java/advanced/mapper/ListMapperTest.java)
* [продвинутый вариант](modules/info.kgeorgiy.java.advanced.mapper/info/kgeorgiy/java/advanced/mapper/AdvancedMapperTest.java)


## Домашнее задание 7. Итеративный параллелизм

Тестирование

 * простой вариант:
   ```info.kgeorgiy.java.advanced.concurrent scalar <полное имя класса>```

   Класс должен реализовывать интерфейс
   [ScalarIP](modules/info.kgeorgiy.java.advanced.concurrent/info/kgeorgiy/java/advanced/concurrent/ScalarIP.java).

 * сложный вариант:
   ```info.kgeorgiy.java.advanced.concurrent list <полное имя класса>```

   Класс должен реализовывать интерфейс
   [ListIP](modules/info.kgeorgiy.java.advanced.concurrent/info/kgeorgiy/java/advanced/concurrent/ListIP.java).

 * продвинутый вариант:
   ```info.kgeorgiy.java.advanced.concurrent advanced <полное имя класса>```

   Класс должен реализовывать интерфейс
   [AdvancedIP](modules/info.kgeorgiy.java.advanced.concurrent/info/kgeorgiy/java/advanced/concurrent/AdvancedIP.java).

Исходный код тестов:

* [простой вариант](modules/info.kgeorgiy.java.advanced.concurrent/info/kgeorgiy/java/advanced/concurrent/ScalarIPTest.java)
* [сложный вариант](modules/info.kgeorgiy.java.advanced.concurrent/info/kgeorgiy/java/advanced/concurrent/ListIPTest.java)
* [продвинутый вариант](modules/info.kgeorgiy.java.advanced.concurrent/info/kgeorgiy/java/advanced/concurrent/AdvancedIPTest.java)


## Домашнее задание 5. JarImplementor

Класс должен реализовывать интерфейс
[JarImpler](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/JarImpler.java).

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.implementor jar-interface <полное имя класса>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.implementor jar-class <полное имя класса>```
 * продвинутый вариант:
    ```info.kgeorgiy.java.advanced.implementor jar-advanced <полное имя класса>```

Исходный код тестов:

* [простой вариант](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/InterfaceJarImplementorTest.java)
* [сложный вариант](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/ClassJarImplementorTest.java)
* [продвинутый вариант](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/AdvancedJarImplementorTest.java)


## Домашнее задание 4. Implementor

Класс должен реализовывать интерфейс
[Impler](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/Impler.java).

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.implementor interface <полное имя класса>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.implementor class <полное имя класса>```
 * продвинутый вариант:
    ```info.kgeorgiy.java.advanced.implementor advanced <полное имя класса>```

Исходный код тестов:

* [простой вариант](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/InterfaceImplementorTest.java)
* [сложный вариант](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/ClassImplementorTest.java)
* [продвинутый вариант](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/AdvancedImplementorTest.java)


## Домашнее задание 3. Студенты

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.student StudentQuery <полное имя класса>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.student StudentGroupQuery <полное имя класса>```

Исходный код

 * простой вариант:
    [интерфейс](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/StudentQuery.java),
    [тесты](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/StudentQueryTest.java)
 * сложный вариант:
    [интерфейс](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/StudentGroupQuery.java),
    [тесты](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/StudentGroupQueryTest.java)
 * продвинутый вариант:
    [интерфейс](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/AdvancedStudentGroupQuery.java),
    [тесты](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/AdvancedStudentGroupQueryTest.java)


## Домашнее задание 2. ArraySortedSet

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.arrayset SortedSet <полное имя класса>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.arrayset NavigableSet <полное имя класса>```

Исходный код тестов:

 * [простой вариант](modules/info.kgeorgiy.java.advanced.arrayset/info/kgeorgiy/java/advanced/arrayset/SortedSetTest.java)
 * [сложный вариант](modules/info.kgeorgiy.java.advanced.arrayset/info/kgeorgiy/java/advanced/arrayset/NavigableSetTest.java)


## Домашнее задание 1. Обход файлов

Для того, чтобы протестировать программу:

 * Скачайте
    * тесты
        * [info.kgeorgiy.java.advanced.base.jar](artifacts/info.kgeorgiy.java.advanced.base.jar)
        * [info.kgeorgiy.java.advanced.walk.jar](artifacts/info.kgeorgiy.java.advanced.walk.jar)
    * и библиотеки к ним:
        * [junit-4.11.jar](lib/junit-4.11.jar)
        * [hamcrest-core-1.3.jar](lib/hamcrest-core-1.3.jar)
 * Откомпилируйте решение домашнего задания
 * Протестируйте домашнее задание
    * Текущая директория должна:
       * содержать все скачанные `.jar` файлы;
       * содержать скомпилированное решение;
       * __не__ содержать скомпилированные самостоятельно тесты.
    * простой вариант:
        ```java -cp . -p . -m info.kgeorgiy.java.advanced.walk Walk <полное имя класса>```
    * сложный вариант:
        ```java -cp . -p . -m info.kgeorgiy.java.advanced.walk RecursiveWalk <полное имя класса>```

Исходный код тестов:

 * [простой вариант](modules/info.kgeorgiy.java.advanced.walk/info/kgeorgiy/java/advanced/walk/WalkTest.java)
 * [сложный вариант](modules/info.kgeorgiy.java.advanced.walk/info/kgeorgiy/java/advanced/walk/RecursiveWalkTest.java)
