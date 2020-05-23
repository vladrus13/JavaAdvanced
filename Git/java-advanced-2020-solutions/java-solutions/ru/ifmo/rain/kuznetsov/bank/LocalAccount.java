package ru.ifmo.rain.kuznetsov.bank;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

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
     * @param name person's name
     * @param lastName person's lastName
     * @param id person's id
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
        System.out.println("Getting id for account with id: " + id);
        return id;
    }

    @Override
    public Integer getAmount(String id) throws RemoteException {
        System.out.println("Getting amount with id: " + id + " for account with id: " + id);
        return amounts.get(id).getMoney();
    }

    @Override
    public synchronized String getName() {
        System.out.println("Getting name for account with id: " + id);
        return name;
    }

    @Override
    public synchronized String getLastName() {
        System.out.println("Getting lastName for account with id: " + id);
        return lastName;
    }

    @Override
    public synchronized int getPassport() {
        System.out.println("Getting passport for account with id: " + id);
        return passport;
    }

    @Override
    public synchronized void setAmount(final int amount, final String subId) throws RemoteException {
        System.out.println("Set amount to" + amount + "for account with id: " + id);
        amounts.computeIfAbsent(subId, key -> new Amount(subId));
        amounts.get(subId).setMoney(amount);
    }

    @Override
    public synchronized void addAmount(final int amount, final String subId) throws RemoteException {
        System.out.println("Add amount " + amount + " id for account with id: " + id);
        amounts.computeIfAbsent(subId, key -> new Amount(subId));
        amounts.get(subId).addMoney(amount);
    }
}
