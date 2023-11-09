package com.ht.hoteldelluna.models;

import com.ht.hoteldelluna.enums.RoomStatus;
import org.bson.types.ObjectId;

public class Room {
    private ObjectId _id;
    private String name;
    private RoomType type; // reference to RoomType
    private Floor floor; // reference to Floor
    private double overnightPrice;
    private RoomStatus status;

    public Room() {}
    public Room(String name, RoomType type, Floor floor, double overnightPrice, RoomStatus status) {
        this.name = name;
        this.type = type;
        this.floor = floor;
        this.overnightPrice = overnightPrice;
        this.status = status;
    }

    public ObjectId getId() {
        return this._id;
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

