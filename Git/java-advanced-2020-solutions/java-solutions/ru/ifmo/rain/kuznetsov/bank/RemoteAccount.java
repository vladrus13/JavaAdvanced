package ru.ifmo.rain.kuznetsov.bank;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * Remote account
 */
public class RemoteAccount implements Account {
    /**
     * Person's data
     */
    private final String id, name, family;
    /**
     * Map of amounts person
     */
    private final Map<String, Amount> amounts;
    /**
     * Person's data
     */
    private final int passport;

    /**
     * Constructor
     * @param name person's name
     * @param family person's family
     * @param id person's id
     * @param passport person's passport
     */
    public RemoteAccount(final String name, final String family, final String id, final int passport) {
        this.amounts = new HashMap<>();
        this.id = id;
        this.passport = passport;
        this.name = name;
        this.family = family;
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
    public synchronized String getFamily() {
        System.out.println("Getting family for account with id: " + id);
        return family;
    }

    @Override
    public synchronized int getPassport() {
        System.out.println("Getting passport for account with id: " + id);
        return passport;
    }

    @Override
    public synchronized void setAmount(final int amount, final String subId) {
        System.out.println("Set amount to" + amount + "for account with id: " + id);
        if (amounts.containsKey(subId)) {
            amounts.get(subId).setMoney(amount);
        } else {
            amounts.put(subId, new Amount(subId, amount));
        }
    }

    @Override
    public synchronized void addAmount(final int amount, final String subId) {
        System.out.println("Add amount " + amount + " id for account with id: " + id);
        if (amounts.containsKey(subId)) {
            amounts.get(subId).addMoney(amount);
        } else {
            amounts.put(subId, new Amount(subId, amount));
        }
    }
}
