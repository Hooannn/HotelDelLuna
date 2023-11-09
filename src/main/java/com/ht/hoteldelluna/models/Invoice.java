package com.ht.hoteldelluna.models;

import org.bson.types.ObjectId;

import java.util.Date;

public class Invoice {
    private ObjectId _id;
    private Date checkInTime;
    private Date checkOutTime;
    private double total;
    private String customerName;
    private Room room; // reference to Room

    public Invoice() {}
    public Invoice(Date checkInTime, Date checkOutTime, double total, String customerName, Room room) {
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.total = total;
        this.customerName = customerName;
        this.room = room;
    }

    public ObjectId getId() {
        return this._id;
    }

    public Date getCheckInTime() {
        return this.checkInTime;
    }

    public Date getCheckOutTime() {
        return this.checkOutTime;
    }

    public double getTotal() {
        return this.total;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Room getRoom() {
        return this.room;
    }
}