package ru.ifmo.rain.kuznetsov.bank.client;

import ru.ifmo.rain.kuznetsov.bank.common.Account;
import ru.ifmo.rain.kuznetsov.bank.common.Bank;

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
     *
     * @param args name, family name, passport, id, change amount
     * @throws RemoteException if we get something wrong
     */
    public static void main(final String... args) throws RemoteException {
        final Bank bank;
        try {
            bank = (Bank) Naming.lookup("//localhost/bank");
        } catch (final NotBoundException e) {
            log("Bank is not bound");
            return;
        } catch (final MalformedURLException e) {
            log("Bank URL is invalid");
            return;
        }
        final String name = args.length >= 1 ? args[0] : "NON";
        final String lastName = args.length >= 2 ? args[1] : "NON";
        final String passport = args.length >= 3 ? args[2] : "NON";
        final String accountId = args.length >= 4 ? args[3] : "NON";
        final String changeAmount = args.length >= 5 ? args[4] : "0";

        Account account = bank.getAccount(accountId);
        if (account == null) {
            log("Creating account");
            account = bank.createRemoteAccount(name, lastName, accountId, Integer.parseInt(passport));
            if (account == null) {
                log("Wrong data");
                return;
            }
        } else {
            log("Account already exists");
        }
        account.addAmount(Integer.parseInt(changeAmount), accountId + ":1");
        log("Account id: " + account.getId());
        log("Money: " + account.getAmount(accountId + ":1"));
        log("Passport " + account.getPassport());
        log("Adding money" + account.getAmount(accountId + ":1"));
        log("Money: " + account.getAmount(accountId + ":1"));
    }

    /**
     * Logger for Client
     * @param s logging
     */
    private static void log(String s) {
        System.out.println(s);
    }
}
