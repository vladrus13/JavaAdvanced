package ru.ifmo.rain.kuznetsov.bank;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote, AutoCloseable {
    /**
     * Create remote account
     * @param name name of person
     * @param family family of person
     * @param id account id
     * @param passport passport person
     * @return account
     * @throws RemoteException if we get something wrong
     */
    Account createRemoteAccount(String name, String family, String id, int passport) throws RemoteException;

    /**
     * Create local account
     * @param name name of person
     * @param family family of person
     * @param id account id
     * @param passport passport person
     * @return account
     * @throws RemoteException if we get something wrong
     */
    Account createLocalAccount(String name, String family, String id, int passport) throws RemoteException;

    /**
     * Returns account by identifier.
     * @param id account id
     * @return account with specified identifier or {@code null} if such account does not exists.
     */
    Account getAccount(String id) throws RemoteException;

    /**
     * Close bank with deleting existed local account
     */
    void close() throws RemoteException;
}
