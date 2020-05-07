package ru.ifmo.rain.kuznetsov.bank;

public class RemoteAccount implements Account {
    private final String id;
    private int amount;
    private final int passport;

    public RemoteAccount(final String id, final int passport) {
        this.id = id;
        this.passport = passport;
        amount = 0;
    }

    public String getId() {
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

    public synchronized void setAmount(final int amount) {
        System.out.println("Setting amount of money for account " + id);
        this.amount = amount;
    }
}
