package ru.ifmo.rain.kuznetsov.bank;

import java.rmi.RemoteException;

/**
 * Remote account
 */
public class RemoteAccount implements Account {
    /**
     * Person's data
     */
    private final String id, name, lastName;

    /**
     * Person's data
     */
    private final int passport;

    /**
     * Bank
     */
    private final Bank bank;

    /**
     * Constructor
     * @param name person's name
     * @param lastName person's lastName
     * @param id person's id
     * @param passport person's passport
     */
    public RemoteAccount(final String name, final String lastName, final String id, final int passport, Bank bank) throws RemoteException {
        this.id = id;
        this.passport = passport;
        this.name = name;
        this.lastName = lastName;
        this.bank = bank;
    }

    @Override
    public synchronized String getId() throws RemoteException {
        System.out.println("Getting id for account with id: " + id);
        return id;
    }

    @Override
    public Integer getAmount(String id) throws RemoteException {
        System.out.println("Getting amount with id: " + id + " for account with id: " + id);
        return bank.getRemoteAmount(id).getMoney();
    }

    @Override
    public synchronized String getName() throws RemoteException {
        System.out.println("Getting name for account with id: " + id);
        return name;
    }

    @Override
    public synchronized String getLastName() throws RemoteException {
        System.out.println("Getting lastName for account with id: " + id);
        return lastName;
    }

    @Override
    public synchronized int getPassport() throws RemoteException {
        System.out.println("Getting passport for account with id: " + id);
        return passport;
    }

    @Override
    public synchronized void setAmount(final int amount, final String subId) throws RemoteException {
        System.out.println("Set amount to" + amount + "for account with id: " + subId);
        bank.getRemoteAmount(subId).setMoney(amount);
    }

    @Override
    public synchronized void addAmount(final int amount, final String subId) throws RemoteException {
        System.out.println("Add amount " + amount + " id for account with id: " + subId);
        bank.getRemoteAmount(subId).addMoney(amount);
    }
}
