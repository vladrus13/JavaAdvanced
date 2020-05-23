package ru.ifmo.rain.kuznetsov.bank;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of class {@link Bank}
 */
public class BankImpl implements Bank {
    /**
     * Port and id of bank
     */
    private final int port, id;
    /**
     * Concurrent map of accounts
     */
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    /**
     * Concurrent map of amounts
     */
    private final Map<String, Amount> amounts = new ConcurrentHashMap<>();
    /**
     * Path to bank's directory
     */
    private final Path TO_LOCAL_BANKS = Path.of( "ru", "ifmo", "rain", "kuznetsov", "bank", "accounts");

    /**
     * Constructor
     * @param port where we get bank
     * @param id of bank
     */
    public BankImpl(final int port, final int id) {
        this.port = port;
        this.id = id;
        try {
            Files.createDirectories(TO_LOCAL_BANKS.resolve(Path.of(String.valueOf(this.id))));
        } catch (IOException exception) {
            // bad exception. We can't ignore it
            exception.printStackTrace();
        }
    }

    @Override
    public Account createRemoteAccount(final String name, final String lastName, final String id, final int passport) throws RemoteException {
        System.out.println("Creating remote account " + id);
        final Account account = new RemoteAccount(name, lastName, id, passport, this);
        if (accounts.putIfAbsent(id, account) == null) {
            UnicastRemoteObject.exportObject(account, port);
            return account;
        } else {
            Account accountFromBank = getAccount(id);
            if (accountFromBank.getPassport() == account.getPassport() &&
                    accountFromBank.getLastName().equals(account.getLastName()) &&
                    accountFromBank.getName().equals(account.getName())) {
                return accountFromBank;
            } else {
                return null;
            }
        }
    }

    @Override
    public Account getAccount(final String id) throws RemoteException {
        System.out.println("Retrieving account " + id);
        return accounts.get(id);
    }

    @Override
    public Account getAccountByPassport(int passport) throws RemoteException {
        return accounts.values().stream().filter(account -> {
            try {
                return account.getPassport() == passport;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return false;
        }).findAny().orElse(null);
    }

    @Override
    public Amount getRemoteAmount(String id) throws RemoteException {
        amounts.computeIfAbsent(id, key -> new Amount(id));
        return amounts.get(id);
    }

    @Override
    public LocalAccount createLocalAccount(final String name, final String lastName, final String id, final int passport) throws RemoteException {
        System.out.println("Creating local account " + id);
        File fileAccount = TO_LOCAL_BANKS.resolve(Path.of(String.valueOf(this.id))).resolve(Path.of(id + ".account")).toFile();
        if (fileAccount.exists()) {
            // account exists
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileAccount));
                return (LocalAccount) objectInputStream.readObject();
            } catch (IOException ignored) { } catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("Error on export existed account" + e.getMessage());
            }
        }
        final LocalAccount account = new LocalAccount(name, lastName, id, passport);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileAccount));
            objectOutputStream.writeObject(account);
        } catch (IOException exception) {
            // worst situation. We can't write files and don't have serialization
            exception.printStackTrace();
        }
        return account;
    }

    @Override
    public void close() throws RemoteException {
        try {
            Files.walkFileTree(TO_LOCAL_BANKS.resolve(String.valueOf(id)), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path file, IOException e) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
