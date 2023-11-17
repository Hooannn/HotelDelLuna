package com.ht.hoteldelluna.models;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Invoice {
    private ObjectId _id;
    private String checkInTime;
    private String checkOutTime;
    private double total;
    private String customerName;
    private Room room; // reference to Room

    public Invoice() {
    }

    public Invoice(String checkInTime, String checkOutTime, double total, String customerName) {
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.total = total;
        this.customerName = customerName;
    }

    public Invoice(String checkInTime, String checkOutTime, double total, String customerName, Room room) {
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.total = total;
        this.customerName = customerName;
        this.room = room;
    }

    public ObjectId getId() {
        return this._id;
    }

    public String getCheckInTime() {
        return this.checkInTime;
    }

    public String getCheckOutTime() {
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

    public String getRoomId() {
        return this.room.getId().toString();
    }

    public String getFormattedCheckInTime() {
        LocalDateTime dateTime = LocalDateTime.parse(this.checkInTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        return dateTime.format(newFormat);
    }
    public String getFormattedCheckOutTime() {
        LocalDateTime dateTime = LocalDateTime.parse(this.checkOutTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        return dateTime.format(newFormat);
    }

    public String getRoomName() {
        return this.room.getName();
    }
    @Override
    public String toString() {
        return "Invoice{" +
                "checkInTime=" + checkInTime +
                ", checkOutTime=" + checkOutTime +
                ", total=" + total +
                ", customerName='" + customerName + '\'' +
                ", room=" + room +
                '}';
    }
}