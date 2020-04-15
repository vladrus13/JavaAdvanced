module java.solutions {
    requires java.compiler;

    requires info.kgeorgiy.java.advanced.concurrent;
    requires info.kgeorgiy.java.advanced.mapper;
    requires info.kgeorgiy.java.advanced.crawler;
    requires info.kgeorgiy.java.advanced.student;
    requires info.kgeorgiy.java.advanced.implementor;

    exports ru.ifmo.rain.kuznetsov.arrayset;
    opens ru.ifmo.rain.kuznetsov.arrayset;
    exports ru.ifmo.rain.kuznetsov.walk;
    opens ru.ifmo.rain.kuznetsov.walk;
    exports ru.ifmo.rain.kuznetsov.student;
    opens ru.ifmo.rain.kuznetsov.student;
    exports ru.ifmo.rain.kuznetsov.implementor;
    opens ru.ifmo.rain.kuznetsov.implementor;
    exports ru.ifmo.rain.kuznetsov.concurrent;
    opens ru.ifmo.rain.kuznetsov.concurrent;
    exports ru.ifmo.rain.kuznetsov.crawler;
    opens ru.ifmo.rain.kuznetsov.crawler;
}
