package ru.ifmo.rain.kuznetsov.bank;

import java.io.Serializable;

public class LocalAccount implements Account, Serializable {
    private final String id;
    private final int passport;
    private int amount;

    LocalAccount(final String id, final int passport) {
        this.amount = 0;
        this.id = id;
        this.passport = passport;
    }

    public synchronized String getId() {
        return id;
    }

    public synchronized int getAmount() {
        System.out.println("Getting amount of money for account " + id);
        return amount;
    }

    public synchronized int getPassport() {
        System.out.println("Getting passport for account " + id);
        return passport;
    }

    public synchronized void setAmount(int amount) {
        System.out.println("Setting amount of money for account " + id);
        this.amount = amount;
    }
}
