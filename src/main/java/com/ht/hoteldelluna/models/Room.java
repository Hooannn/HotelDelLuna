package com.ht.hoteldelluna.models;

import com.ht.hoteldelluna.enums.RoomStatus;

public class Room {
    private int id;
    private String name;
    private RoomType type;
    private Floor floor;
    private RoomStatus status;

    public Room() {}
    public Room(String name, RoomType type, Floor floor, RoomStatus status) {
        this.name = name;
        this.type = type;
        this.floor = floor;
        this.status = status;
    }

    public Room(int id, String name, RoomStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Room(String name, RoomStatus status) {
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
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

    public RoomStatus getStatus() {
        return status;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", floor=" + floor +
                ", status=" + status +
                '}';
    }
}

