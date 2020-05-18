package ru.ifmo.rain.kuznetsov.bank;

import java.io.Serializable;
import java.rmi.Remote;

public class Amount implements Remote, Serializable {
    private final String id;
    private int money;

    public Amount(String id, int money) {
        this.id = id;
        this.money = money;
    }

    public Amount(String id) {
        this.id = id;
        this.money = 0;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void addMoney(int money) {
        this.money += money;
    }
}