package ru.ifmo.rain.kuznetsov.bank.test;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.ifmo.rain.kuznetsov.bank.common.Account;
import ru.ifmo.rain.kuznetsov.bank.common.Bank;
import ru.ifmo.rain.kuznetsov.bank.server.Server;

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
@RunWith(JUnit4.class)
public class BankTests {

    @SuppressWarnings("SameReturnValue")
    private static String generateSplit() {
        return "=========================================";
    }

    @BeforeClass
    public static void beforeClass() {
        System.out.println(generateSplit());
        System.out.println("Start testing Bank");
        System.out.println(generateSplit());
        try {
            reloadServer();
        } catch (RemoteException e) {
            System.out.println("\tCan't start server\n\t" + e.getMessage());
        }
    }

    /**
     * Create bank with id for tests
     *
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
     *
     * @throws RemoteException if came something wrong
     */
    private static void reloadServer() throws RemoteException {
        System.out.println("Reloading server");
        LocateRegistry.createRegistry(1099);
    }

    /**
     * Just create bank with id
     *
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
     *
     * @param account       {@link Account}
     * @param nameAmount    id of amount
     * @param moneyExcepted how much we must have
     * @throws RemoteException if get something wrong
     */
    private void testMoney(Account account, String nameAmount, Integer moneyExcepted) throws RemoteException {
        Integer money = account.getAmount(nameAmount);
        assertNotNull("\tAccount don't have money on \"" + nameAmount + "\"", money);
        assertEquals("\tAccount have wrong amount on \"" + nameAmount + "\"", money, moneyExcepted);
    }

    @Test
    public void testCreateAccount() {
        System.out.println("\n" + generateSplit() + "\nTesting: testCreateAccount\n");
        Account account;
        try (Bank bank = createTestBank("00")) {
            account = bank.createRemoteAccount("Vlad", "Kuznetsov", "1", 365000);
            assertEquals(account.getName(), "Vlad");
            assertEquals(account.getLastName(), "Kuznetsov");
            assertEquals(account.getId(), "1");
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateTwoAccount() {
        System.out.println("\n" + generateSplit() + "\nTesting: testCreateTwoAccount\n");
        Account account1, account2;
        try (Bank bank = createTestBank("01")) {
            account1 = bank.createRemoteAccount("Vlad", "Kuznetsov", "1", 365000);
            account2 = bank.createRemoteAccount("Vladislav", "Kuznetsov", "2", 365001);
            assertEquals(account1.getName(), "Vlad");
            assertEquals(account1.getLastName(), "Kuznetsov");
            assertEquals(account1.getId(), "1");
            assertEquals(account2.getName(), "Vladislav");
            assertEquals(account2.getLastName(), "Kuznetsov");
            assertEquals(account2.getId(), "2");
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateTwoSameAccount() {
        System.out.println("\n" + generateSplit() + "\nTesting: testCreateTwoSameAccount\n");
        Account account1, account2;
        try (Bank bank = createTestBank("02")) {
            account1 = bank.createRemoteAccount("Vlad", "Kuznetsov", "1", 365000);
            account2 = bank.createRemoteAccount("Vlad", "Kuznetsov", "1", 365000);
            assertEquals(account1, account2);
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void testCreateTwoWrongAccount() {
        System.out.println("\n" + generateSplit() + "\nTesting: testCreateTwoWrongAccount\n");
        Account account;
        try (Bank bank = createTestBank("03")) {
            account = bank.createRemoteAccount("Vlad", "Kuznetsov", "1", 365000);
            account = bank.createRemoteAccount("Vlad", "Kuznetsov", "1", 365001);
            assertNull(account);
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOneAccount() {
        System.out.println("\n" + generateSplit() + "\nTesting: testOneAccount\n");
        Account account;
        try (Bank bank = createTestBank("04")) {
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
        System.out.println("\n" + generateSplit() + "\nTesting: testOneAccountMoreAmount\n");
        Account account;
        try (Bank bank = createTestBank("05")) {
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
        System.out.println("\n" + generateSplit() + "\nTesting: testMoreAccountMoreAmountRandom\n");
        Map<Integer, Account> accountMap = new HashMap<>();
        try (Bank bank = createTestBank("06")) {
            for (int i = 0; i < 100; i++) { // generate 100 random accounts
                byte[] name = new byte[10];
                byte[] lastName = new byte[7];
                new Random().nextBytes(name);
                new Random().nextBytes(lastName);
                Account account = bank.createRemoteAccount(
                        new String(name, StandardCharsets.UTF_8),
                        new String(lastName, StandardCharsets.UTF_8),
                        String.valueOf(i + 1),
                        random.nextInt(999998));
                accountMap.put(i + 1, account);
                account.setAmount(0, String.format("%d:%d", account.getPassport(), 1));
            }
            for (int i = 0; i < 50000; i++) { // do smth with it
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
        System.out.println("\n" + generateSplit() + "\nTesting: testLocalAndRemoteAccount\n");
        Account localAccount, remoteAccount;
        try (Bank bank = createTestBank("07")) {
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

    @Test
    public void testLocalAndSearchByPassport() {
        System.out.println("\n" + generateSplit() + "\nTesting: testLocalAndSearchByPassport\n");
        Account account1;
        try (Bank bank = createTestBank("08")) {
            account1 = bank.createLocalAccount("Chika", "Fujiwara", "1", 0);
            bank.createLocalAccount("Kaguya", "Shinomiya", "2", 111111);
            Account account = bank.getLocalAccountByPassport(0);
            assertNotNull("Account with passport \"000000\" doesn't exist", account);
            assertEquals("Chika's Account's not equals", account1, account);
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testRemoteAndSearchByPassport() {
        System.out.println("\n" + generateSplit() + "\nTesting: testRemoteAndSearchByPassport\n");
        Account account1;
        try (Bank bank = createTestBank("09")) {
            account1 = bank.createRemoteAccount("Chika", "Fujiwara", "1", 0);
            bank.createRemoteAccount("Kaguya", "Shinomiya", "2", 111111);
            Account account = bank.getRemoteAccountByPassport(0);
            assertNotNull("Account with passport \"000000\" doesn't exist", account);
            assertEquals("Chika's Account's not equals", account1, account);
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSamePassportRemote() {
        System.out.println("\n" + generateSplit() + "\nTesting: testSamePassportRemote\n");
        Account account1, account2;
        try (Bank bank = createTestBank("10")) {
            account1 = bank.createRemoteAccount("Vlad", "Smith", "1", 395000);
            account2 = bank.createRemoteAccount("Vlad", "Kuzntesov", "2", 395000);
            account1.addAmount(100, account1.getPassport() + ":1");
            testMoney(account2, account1.getPassport() + ":1", 100);
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSamePassportLocal() {
        System.out.println("\n" + generateSplit() + "\nTesting: testSamePassportLocal\n");
        Account account1, account2;
        try (Bank bank = createTestBank("10")) {
            account1 = bank.createLocalAccount("Vlad", "Smith", "1", 395000);
            account2 = bank.createLocalAccount("Vlad", "Kuzntesov", "2", 395000);
            account1.addAmount(100, account1.getPassport() + ":1");
            assertFalse("Local account have another account's amount", account2.isAmount(account1.getPassport() + ":1"));
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }
}
