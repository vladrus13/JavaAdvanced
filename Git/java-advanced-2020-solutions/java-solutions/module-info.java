module java.solutions {
    requires java.compiler;
    requires java.rmi;
    requires junit;

    requires info.kgeorgiy.java.advanced.base;
    requires info.kgeorgiy.java.advanced.walk;
    requires info.kgeorgiy.java.advanced.arrayset;
    requires info.kgeorgiy.java.advanced.student;
    requires info.kgeorgiy.java.advanced.implementor;
    requires info.kgeorgiy.java.advanced.concurrent;
    requires info.kgeorgiy.java.advanced.crawler;
    requires info.kgeorgiy.java.advanced.mapper;
    requires info.kgeorgiy.java.advanced.hello;

    exports ru.ifmo.rain.kuznetsov.walk;
    opens ru.ifmo.rain.kuznetsov.walk;
    exports ru.ifmo.rain.kuznetsov.arrayset;
    opens ru.ifmo.rain.kuznetsov.arrayset;
    exports ru.ifmo.rain.kuznetsov.student;
    opens ru.ifmo.rain.kuznetsov.student;
    exports ru.ifmo.rain.kuznetsov.implementor;
    opens ru.ifmo.rain.kuznetsov.implementor;
    exports ru.ifmo.rain.kuznetsov.concurrent;
    opens ru.ifmo.rain.kuznetsov.concurrent;
    exports ru.ifmo.rain.kuznetsov.crawler;
    opens ru.ifmo.rain.kuznetsov.crawler;
    exports ru.ifmo.rain.kuznetsov.hello;
    opens ru.ifmo.rain.kuznetsov.hello;
    exports ru.ifmo.rain.kuznetsov.bank;
    opens ru.ifmo.rain.kuznetsov.bank;
    exports ru.ifmo.rain.kuznetsov.bank.test;
    opens ru.ifmo.rain.kuznetsov.bank.test;
}
