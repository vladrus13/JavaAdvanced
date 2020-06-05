package ru.ifmo.rain.kuznetsov.bank.server;

import ru.ifmo.rain.kuznetsov.bank.common.Account;
import ru.ifmo.rain.kuznetsov.bank.common.Bank;

import java.rmi.RemoteException;
import java.util.Objects;

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
     *
     * @param name     person's name
     * @param lastName person's lastName
     * @param id       person's id
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
        log("Getting id for account with id: " + id);
        return id;
    }

    @Override
    public synchronized Integer getAmount(String id) throws RemoteException {
        log("Getting amount with id: " + id + " for account with id: " + id);
        return bank.getRemoteAmount(id).getMoney();
    }

    @Override
    public synchronized String getName() throws RemoteException {
        log("Getting name for account with id: " + id);
        return name;
    }

    @Override
    public synchronized String getLastName() throws RemoteException {
        log("Getting lastName for account with id: " + id);
        return lastName;
    }

    @Override
    public synchronized int getPassport() throws RemoteException {
        log("Getting passport for account with id: " + id);
        return passport;
    }

    @Override
    public synchronized void setAmount(final int amount, final String subId) throws RemoteException {
        log("Set amount to" + amount + "for account with id: " + subId);
        bank.getRemoteAmount(subId).setMoney(amount);
    }

    @Override
    public synchronized void addAmount(final int amount, final String subId) throws RemoteException {
        log("Add amount " + amount + " id for account with id: " + subId);
        bank.getRemoteAmount(subId).addMoney(amount);
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteAccount that = (RemoteAccount) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public synchronized boolean isAmount(String id) throws RemoteException {
        return bank.containsAmount(id);
    }

    /**
     * Logger for RemoteAccount
     *
     * @param s string which we write to System.out
     */
    private synchronized void log(String s) {
        System.out.println(s);
    }
}
