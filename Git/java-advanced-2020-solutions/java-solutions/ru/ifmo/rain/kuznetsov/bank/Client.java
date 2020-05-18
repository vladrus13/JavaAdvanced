package ru.ifmo.rain.kuznetsov.bank;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Client class
 */
public class Client {
    /**
     * Main class for conlose run of client
     * @param args id of bank in first arg, else id = 0
     * @throws RemoteException if we get something wrong
     */
    public static void main(final String... args) throws RemoteException {
        final Bank bank;
        try {
            bank = (Bank) Naming.lookup("//localhost/bank");
        } catch (final NotBoundException e) {
            System.out.println("Bank is not bound");
            return;
        } catch (final MalformedURLException e) {
            System.out.println("Bank URL is invalid");
            return;
        }
        final String name = args.length >= 1 ? args[0] : "NON";
        final String family = args.length >= 2 ? args[1] : "NON";
        final String passport = args.length >= 3 ? args[2] : "NON";
        final String accountId = args.length >= 4 ? args[3] : "NON";
        final String changeAmount = args.length >= 5 ? args[4] : "0";

        Account account = bank.getAccount(accountId);
        if (account == null) {
            System.out.println("Creating account");
            account = bank.createRemoteAccount(name, family, accountId, Integer.parseInt(passport));
        } else {
            System.out.println("Account already exists");
        }
        account.addAmount(Integer.parseInt(changeAmount), accountId + ":1");
        System.out.println("Account id: " + account.getId());
        System.out.println("Money: " + account.getAmount(accountId + ":1"));
        System.out.println("Passport " + account.getPassport());
        System.out.println("Adding money" + account.getAmount(accountId + ":1"));
        System.out.println("Money: " + account.getAmount(accountId + ":1"));
    }
}
