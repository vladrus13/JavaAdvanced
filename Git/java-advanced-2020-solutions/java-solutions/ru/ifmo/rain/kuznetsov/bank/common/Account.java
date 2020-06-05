package ru.ifmo.rain.kuznetsov.bank.common;

import java.rmi.*;

/**
 * Interface to account's class
 */
public interface Account extends Remote {
    /**
     * Returns account identifier.
     */
    String getId() throws RemoteException;

    /**
     * Returns amount of money by id
     */
    Integer getAmount(String id) throws RemoteException;

    /**
     * Returns name
     */
    String getName() throws RemoteException;

    /**
     * Returns lastName
     */
    String getLastName() throws RemoteException;

    /**
     * Returns passport
     */
    int getPassport() throws RemoteException;

    /**
     * Sets amount of money at the account.
     */
    void setAmount(int amount, String subId) throws RemoteException;

    /**
     * Adds amount of money at the account.
     */
    void addAmount(int amount, String subId) throws RemoteException;

    /**
     * Return true, if amount exists, else false
     */
    boolean isAmount(String id) throws RemoteException;
}
