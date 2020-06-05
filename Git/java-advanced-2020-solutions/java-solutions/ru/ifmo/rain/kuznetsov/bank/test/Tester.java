package ru.ifmo.rain.kuznetsov.bank.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * Class with main for testing program
 */
public class Tester {
    /**
     * Main class runner for testing BankTests
     * @param args ignored
     */
    public static void main(String[] args) {
        //new Tester().add("Bank", BankTests.class).run(args);
        Result tests = new JUnitCore().run(BankTests.class);
        if (tests.wasSuccessful()) {
            System.out.println("1");
            System.exit(0);
        } else {
            System.out.println("0");
            System.exit(1);
        }

    }
}
