package com.scibetta.model;

public class CreditCard {

    private int id;
    private int number;
    private int balance;
    private int owner; // int id
    private int status; // 0 false, 1 true

    public CreditCard() {

    }

    public CreditCard(int id, int number, int balance, int owner, int status) {
        this.id = id;
        this.number = number;
        this.balance = balance;
        this.owner = owner;
        this.status = status;
    }

    public CreditCard(int number, int balance, int status) {
        this.number = number;
        this.balance = balance;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + id +
                ", number=" + number +
                ", balance=" + balance +
                ", owner=" + owner +
                ", status=" + status +
                '}';
    }

}