package ru.ifmo.rain.kuznetsov.bank;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

/**
 * Server class
 */
public class Server {
    private final static int PORT = 8888;

    /**
     * Class for start server
     * @param args id of bank in first arg, else id = 0
     */
    public static void main(final String... args) {
        int id = 0;
        if (!(args == null || args.length < 1)) {
            id = Integer.parseInt(args[0]);
        }
        final Bank bank = new BankImpl(PORT, id);
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
