package ru.ifmo.rain.kuznetsov.bank.server;

import ru.ifmo.rain.kuznetsov.bank.common.Account;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Local account
 */
public class LocalAccount implements Account, Serializable {
    /**
     * Person's data
     */
    private final String id, name, lastName;
    /**
     * Person's data
     */
    private final int passport;
    /**
     * Map of amounts person
     */
    private final Map<String, Amount> amounts;

    /**
     * Constructor
     *
     * @param name     person's name
     * @param lastName person's lastName
     * @param id       person's id
     * @param passport person's passport
     */
    public LocalAccount(final String name, final String lastName, final String id, final int passport) {
        this.amounts = new HashMap<>();
        this.id = id;
        this.passport = passport;
        this.name = name;
        this.lastName = lastName;
    }

    @Override
    public synchronized String getId() {
        log("Getting id for account with id: " + id);
        return id;
    }

    @Override
    public Integer getAmount(String id) throws RemoteException {
        log("Getting amount with id: " + id + " for account with id: " + id);
        return amounts.get(id).getMoney();
    }

    @Override
    public synchronized String getName() {
        log("Getting name for account with id: " + id);
        return name;
    }

    @Override
    public synchronized String getLastName() {
        log("Getting lastName for account with id: " + id);
        return lastName;
    }

    @Override
    public synchronized int getPassport() {
        log("Getting passport for account with id: " + id);
        return passport;
    }

    @Override
    public synchronized void setAmount(final int amount, final String subId) throws RemoteException {
        log("Set amount to" + amount + "for account with id: " + id);
        amounts.computeIfAbsent(subId, key -> new Amount(subId));
        amounts.get(subId).setMoney(amount);
    }

    @Override
    public synchronized void addAmount(final int amount, final String subId) throws RemoteException {
        log("Add amount " + amount + " id for account with id: " + id);
        amounts.computeIfAbsent(subId, key -> new Amount(subId));
        amounts.get(subId).addMoney(amount);
    }

    @Override
    public boolean isAmount(String id) {
        return amounts.containsKey(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalAccount that = (LocalAccount) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Logger for LocalAccount
     *
     * @param s string which we write to System.out
     */
    private void log(String s) {
        System.out.println(s);
    }
}
