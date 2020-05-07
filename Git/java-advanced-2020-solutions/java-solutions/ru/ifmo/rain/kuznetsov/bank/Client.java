package ru.ifmo.rain.kuznetsov.bank;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
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

        final String accountId = args.length >= 1 ? args[0] : "geo";
        final String passport = args.length >= 2 ? args[1] : "geo";

        Account account = bank.getAccount(accountId);
        if (account == null) {
            System.out.println("Creating account");
            account = bank.createAccount(accountId, Integer.parseInt(passport));
        } else {
            System.out.println("Account already exists");
        }
        System.out.println("Account id: " + account.getId());
        System.out.println("Money: " + account.getAmount());
        System.out.println("Passport " + account.getPassport());
        System.out.println("Adding money");
        account.setAmount(account.getAmount() + 100);
        System.out.println("Money: " + account.getAmount());
    }
}
