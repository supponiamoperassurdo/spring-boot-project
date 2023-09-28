package com.scibetta.model;

import java.sql.Timestamp;

public class CreditCardTransactionUserView { /* RAPPRESENTA LA VISTA DOPO IL JOIN TRA TRANSACTION E USER: DA USA PER IL ROWMAPPER */

    private int tid;
    private String description;
    private int userid;
    private int sellerid;
    private Timestamp date;
    private int operation;
    private int uid;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String role;
    private String storename;
    private int creditcardnumber;
    private int number; // SOLO PER LA LISTA MOVIMENTI DELL'UTENTE

    public CreditCardTransactionUserView() {}

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getSellerid() {
        return sellerid;
    }

    public void setSellerid(int sellerid) {
        this.sellerid = sellerid;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCreditcardnumber() {
        return creditcardnumber;
    }

    public void setCreditcardnumber(int creditcardnumber) {
        this.creditcardnumber = creditcardnumber;
    }

}
