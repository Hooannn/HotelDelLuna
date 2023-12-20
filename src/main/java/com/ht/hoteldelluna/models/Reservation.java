package com.ht.hoteldelluna.models;

import com.ht.hoteldelluna.backend.services.InvoicesService;
import com.ht.hoteldelluna.enums.ReservationStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reservation {
    private int id;
    private Room room;
    private String checkInTime;
    private String checkOutTime;
    private String customerName;
    private User createdBy;
    private String note;
    private ReservationStatus status;
    private int customerCount;

    public Reservation() {}

    public User getCreatedBy() {
        return createdBy;
    }
    public Reservation(int id, String checkInTime, String checkOutTime, String customerName, int customerCount, String note, ReservationStatus status) {
        this.id = id;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.customerName = customerName;
        this.note = note;
        this.status = status;
        this.customerCount = customerCount;
    }
    public Reservation(int id, String checkInTime, String checkOutTime, String customerName, int customerCount, String note, ReservationStatus status, Room room) {
        this.id = id;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.customerName = customerName;
        this.note = note;
        this.status = status;
        this.customerCount = customerCount;
        this.room = room;
    }
    public Reservation(int id, String checkInTime, String checkOutTime, String customerName, int customerCount, String note, ReservationStatus status, Room room, User createdBy) {
        this.id = id;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.customerName = customerName;
        this.note = note;
        this.status = status;
        this.customerCount = customerCount;
        this.createdBy = createdBy;
        this.room = room;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
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
    @Override
    public String toString() {
        return "Reservation{" +
                "_id=" + id +
                ", room=" + room +
                ", checkInTime=" + checkInTime +
                ", checkOutTime=" + checkOutTime +
                ", status=" + status +
                ", customerName='" + customerName + '\'' +
                ", customerCount=" + customerCount +
                '}';
    }
}
