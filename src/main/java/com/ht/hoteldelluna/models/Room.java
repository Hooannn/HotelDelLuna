package com.ht.hoteldelluna.models;

import com.ht.hoteldelluna.enums.RoomStatus;

public class Room {
    private String id;
    private String name;
    private RoomType type; // reference to RoomType
    private Floor floor; // reference to Floor
    private double overnightPrice;
    private RoomStatus status;

    public Room(String id, String name, RoomType type, Floor floor, double overnightPrice, RoomStatus status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.floor = floor;
        this.overnightPrice = overnightPrice;
        this.status = status;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public RoomType getType() {
        return this.type;
    }

    public Floor getFloor() {
        return this.floor;
    }

    public double getOvernightPrice() {
        return this.overnightPrice;
    }

    public RoomStatus getStatus() {
        return status;
    }
}

