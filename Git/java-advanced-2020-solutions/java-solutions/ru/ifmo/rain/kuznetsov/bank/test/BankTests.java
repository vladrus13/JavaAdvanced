package ru.ifmo.rain.kuznetsov.bank.test;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import ru.ifmo.rain.kuznetsov.bank.Account;
import ru.ifmo.rain.kuznetsov.bank.Bank;
import ru.ifmo.rain.kuznetsov.bank.Server;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Test class for bank
 */
public class BankTests {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("========================");
        System.out.println("Start testing Bank");
        System.out.println("========================");
        try {
            reloadServer();
        } catch (RemoteException e) {
            System.out.println("\tCan't start server\n\t" + e.getMessage());
        }
    }

    /**
     * Create bank with id for tests
     * @param id bank's id
     * @return {@link Bank}
     */
    public Bank createTestBank(String id) {
        Bank bank = createBank(id);
        assertNotNull("\tBank can't created. Bank is null", bank);
        return bank;
    }

    /**
     * Create registry in port 1099
     * @throws RemoteException if came something wrong
     */
    private static void reloadServer() throws RemoteException {
        System.out.println("Reloading server");
        LocateRegistry.createRegistry(1099);
    }

    /**
     * Just create bank with id
     * @param id bank's id
     * @return {@link Bank}
     */
    private Bank createBank(String id) {
        Server.main(id);
        Bank bank = null;
        try {
            bank = (Bank) Naming.lookup("//localhost/bank");
        } catch (final NotBoundException e) {
            System.out.println("Bank is not bound");
        } catch (final MalformedURLException e) {
            System.out.println("Bank URL is invalid");
        } catch (RemoteException e) {
            System.out.println("Bank can't created");
        }
        return bank;
    }

    /**
     * Testing for money account
     * @param account {@link Account}
     * @param nameAmount id of amount
     * @param moneyExcepted how much we must have
     * @throws RemoteException if get something wrong
     */
    private void testMoney(Account account, String nameAmount, Integer moneyExcepted) throws RemoteException {
        Integer money = account.getAmount(nameAmount);
        assertNotNull("\tAccount don't have money on \"" + nameAmount + "\"", money);
        assertEquals("\tAccount have wrong amount on \"" + nameAmount + "\"", money, moneyExcepted);
    }

    @Test
    public void testOneAccount() {
        System.out.println("\n========================\nTesting: testOneAccount\n");
        Account account;
        try (Bank bank = createTestBank("01")) {
            account = bank.createRemoteAccount("Vlad", "Kuznetsov", "1", 365000);
            account.addAmount(100, "365000:1");
            testMoney(account, "365000:1", 100);
            account.addAmount(100, "365000:1");
            testMoney(account, "365000:1", 200);
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOneAccountMoreAmount() {
        System.out.println("\n========================\nTesting: testOneAccountMoreAmount\n");
        Account account;
        try (Bank bank = createTestBank("02")) {
            account = bank.createRemoteAccount("Vlad", "Kuznetsov", "1", 666333);
            account.addAmount(200, "666333:1");
            testMoney(account, "666333:1", 200);
            account.addAmount(200, "666333:2");
            testMoney(account, "666333:2", 200);
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Random for passport
     */
    final Random random = new Random();

    @Test
    public void testMoreAccountOneAmountRandom() {
        System.out.println("\n========================\nTesting: testMoreAccountMoreAmountRandom\n");
        Map<Integer, Account> accountMap = new HashMap<>();
        try (Bank bank = createTestBank("03")) {
            for (int i = 0; i < 100; i++) { // generate 100 random accounts
                byte[] name = new byte[10];
                byte[] family = new byte[7];
                new Random().nextBytes(name);
                new Random().nextBytes(family);
                Account account = bank.createRemoteAccount(
                        new String(name, StandardCharsets.UTF_8),
                        new String(family, StandardCharsets.UTF_8),
                        String.valueOf(i + 1),
                        random.nextInt(999998));
                accountMap.put(i + 1, account);
                account.setAmount(0, String.format("%d:%d", account.getPassport(), 1));
            }
            for (int i = 0; i < 1000; i++) { // do smth with it
                int id = random.nextInt(100) + 1;
                Account account = accountMap.get(id);
                String stringAmount = String.format("%d:%d", account.getPassport(), 1);
                int amount = account.getAmount(stringAmount);
                account.addAmount(1, stringAmount);
                testMoney(account, stringAmount, amount + 1);
            }
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLocalAndRemoteAccount() {
        System.out.println("\n========================\nTesting: testLocalAndRemoteAccount\n");
        Account localAccount, remoteAccount;
        try (Bank bank = createTestBank("04")){
            remoteAccount = bank.createRemoteAccount("Vlad", "Kuznetsov", "1", 342000);
            localAccount = bank.createLocalAccount("Vlad", "Kuznetsov", "1", 342000);
            remoteAccount.addAmount(100, "342000:1");
            localAccount.addAmount(200, "342000:1");
            testMoney(remoteAccount, "342000:1", 100);
            testMoney(localAccount, "342000:1", 200);
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }
}
