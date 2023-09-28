package com.scibetta.model;

import java.sql.Timestamp;
import java.util.Objects;

public class Transaction {

    private int id;
    private String description;
    private int userid;
    private int sellerid;
    private Timestamp date;
    private int operation;
    private int creditcardnumber; // serve per eventuale join

    public Transaction() {}

    public Transaction(int id, String description, int userid, int sellerid, Timestamp date, int operation, int creditcardnumber) {
        this.id = id;
        this.description = description;
        this.userid = userid;
        this.sellerid = sellerid;
        this.date = date;
        this.operation = operation;
        this.creditcardnumber = creditcardnumber;
    }

    public Transaction(String description, int userid, int sellerid, Timestamp date, int operation, int creditcardnumber) {
        this.description = description;
        this.userid = userid;
        this.sellerid = sellerid;
        this.date = date;
        this.operation = operation;
        this.creditcardnumber = creditcardnumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCreditcardnumber() {
        return creditcardnumber;
    }

    public void setCreditcardnumber(int creditcardnumber) {
        this.creditcardnumber = creditcardnumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return getId() == that.getId() && getUserid() == that.getUserid() && getSellerid() == that.getSellerid() && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getUserid(), getSellerid(), getDate());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", userid=" + userid +
                ", sellerid=" + sellerid +
                ", date=" + date +
                ", operation=" + operation +
                ", creditcardnumber=" + creditcardnumber +
                '}';
    }

}
