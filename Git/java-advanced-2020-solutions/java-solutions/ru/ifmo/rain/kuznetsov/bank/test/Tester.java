package ru.ifmo.rain.kuznetsov.bank.test;

import info.kgeorgiy.java.advanced.base.BaseTester;

/**
 * Class with main for testing program
 */
public class Tester extends BaseTester {
    public static void main(String[] args) {
        new Tester().add("test", BankTests.class).run(args);
    }
}
