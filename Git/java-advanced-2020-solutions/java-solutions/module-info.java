module java.solutions {
    requires java.compiler;

    requires info.kgeorgiy.java.advanced.implementor;
    requires info.kgeorgiy.java.advanced.concurrent;
    requires info.kgeorgiy.java.advanced.mapper;
    requires info.kgeorgiy.java.advanced.crawler;

    exports ru.ifmo.rain.kuznetsov.implementor;
    opens ru.ifmo.rain.kuznetsov.implementor;
}
