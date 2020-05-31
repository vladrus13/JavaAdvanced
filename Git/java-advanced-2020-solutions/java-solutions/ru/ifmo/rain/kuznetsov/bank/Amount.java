package ru.ifmo.rain.kuznetsov.bank;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Amount class
 */
public class Amount implements Remote, Serializable {
    /**
     * Id for amount
     */
    private final String id;
    /**
     * Money for amount
     */
    private int money;

    /**
     * Constructor class for {@link Amount}
     * @param id id
     * @param money money
     */
    public Amount(String id, int money) {
        this.id = id;
        this.money = money;
    }

    /**
     * Constructor class for {@link Amount}
     * @param id id
     */
    public Amount(String id) {
        this.id = id;
        this.money =  0;
    }

    /**
     * Getter for money
     * @return money
     */
    public int getMoney() throws RemoteException {
        return money;
    }

    /**
     * Setter for money
     * @param money money
     */
    public void setMoney(int money) throws RemoteException {
        this.money = money;
    }

    /**
     * Adding money
     * @param money adding
     */
    public void addMoney(int money) throws RemoteException {
        this.money += money;
    }
}