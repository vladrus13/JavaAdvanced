package ru.ifmo.rain.kuznetsov.bank.common;

import ru.ifmo.rain.kuznetsov.bank.server.Amount;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote, AutoCloseable {
    /**
     * Create remote account
     *
     * @param name     name of person
     * @param lastName lastName of person
     * @param id       account id
     * @param passport passport person
     * @return account
     * @throws RemoteException if we get something wrong
     */
    Account createRemoteAccount(String name, String lastName, String id, int passport) throws RemoteException;

    /**
     * Create local account
     *
     * @param name     name of person
     * @param lastName lastName of person
     * @param id       account id
     * @param passport passport person
     * @return account
     * @throws RemoteException if we get something wrong
     */
    Account createLocalAccount(String name, String lastName, String id, int passport) throws RemoteException;

    /**
     * Returns account by identifier.
     *
     * @param id account id
     * @return account with specified identifier or {@code null} if such account does not exists.
     */
    Account getAccount(String id) throws RemoteException;

    /**
     * Get any account by passport
     *
     * @param passport passport
     * @return {@link Account}, if exists, null else
     * @throws RemoteException if we get wrong something
     */
    Account getAccountByPassport(int passport) throws RemoteException;

    /**
     * Get remote amount
     *
     * @param id id of amount
     * @return {@link Amount}
     * @throws RemoteException if we get something wrong
     */
    Amount getRemoteAmount(String id) throws RemoteException;

    /**
     * Get local account by passport
     *
     * @param passport passport
     * @return {@link Account}, if exists, null else
     * @throws RemoteException if we get wrong something
     */
    Account getLocalAccountByPassport(int passport) throws RemoteException;

    /**
     * Get remote account by passport
     *
     * @param passport passport
     * @return {@link Account}, if exists, null else
     * @throws RemoteException if we get wrong something
     */
    Account getRemoteAccountByPassport(int passport) throws RemoteException;

    /**
     * Close bank with deleting existed local account
     */
    void close() throws RemoteException;

    /**
     * Check, exists this amount or not
     *
     * @param id id
     * @return boolean
     */
    boolean containsAmount(String id) throws RemoteException;
}
