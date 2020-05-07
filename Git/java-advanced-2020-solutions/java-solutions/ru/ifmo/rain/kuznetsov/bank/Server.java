package ru.ifmo.rain.kuznetsov.bank;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

public class Server {
    private final static int PORT = 8888;
    public static void main(final String... args) {
        System.out.println("Run");
        final Bank bank = new RemoteBank(PORT);
        System.out.println("Bank created");
        try {
            UnicastRemoteObject.exportObject(bank, PORT);
            Naming.rebind("//localhost/bank", bank);
        } catch (final RemoteException e) {
            System.out.println("Cannot export object: " + e.getMessage());
            e.printStackTrace();
        } catch (final MalformedURLException e) {
            System.out.println("Malformed URL");
        }
        System.out.println("Server started");
    }
}
