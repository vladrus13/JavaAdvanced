package ru.ifmo.rain.kuznetsov.bank;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BankImpl implements Bank {
    /**
     * Port and id of bank
     */
    private final int port, id;
    /**
     * Concurrent map of accounts
     */
    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();
    /**
     * Concurrent map of amounts
     */
    private final ConcurrentMap<String, Amount> amounts = new ConcurrentHashMap<>();
    /**
     * Path to bank's directory
     */
    private final Path TO_LOCAL_BANKS = Path.of("java-solutions", "ru", "ifmo", "rain", "kuznetsov", "bank", "accounts");

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
    public Account createRemoteAccount(final String name, final String family, final String id, final int passport) throws RemoteException {
        System.out.println("Creating remote account " + id);
        final Account account = new RemoteAccount(name, family, id, passport);
        if (accounts.putIfAbsent(id, account) == null) {
            UnicastRemoteObject.exportObject(account, port);
            return account;
        } else {
            return getAccount(id);
        }
    }

    @Override
    public Account getAccount(final String id) {
        System.out.println("Retrieving account " + id);
        return accounts.get(id);
    }

    @Override
    public Account createLocalAccount(final String name, final String family, final String id, final int passport) throws RemoteException {
        System.out.println("Creating local account " + id);
        File fileAccount = TO_LOCAL_BANKS.resolve(Path.of(String.valueOf(this.id))).resolve(Path.of(id + ".account")).toFile();
        if (fileAccount.exists()) {
            // account exists
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileAccount));
                return (Account) objectInputStream.readObject();
            } catch (IOException ignored) { } catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("Error on export existed account" + e.getMessage());
            }
        }
        final Account account = new LocalAccount(name, family, id, passport);
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
    public void close() {
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
