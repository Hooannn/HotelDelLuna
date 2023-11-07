package com.ht.hoteldelluna.models;

import java.util.Date;

public class Invoice {
    private String id;
    private Date checkInTime;
    private Date checkOutTime;
    private double total;
    private String customerName;
    private Room room; // reference to Room

    public Invoice(String id, Date checkInTime, Date checkOutTime, double total, String customerName, Room room) {
        this.id = id;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.total = total;
        this.customerName = customerName;
        this.room = room;
    }

    public String getId() {
        return this.id;
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