package com.ht.hoteldelluna.models;


public class RoomType {
    private int id;
    private String name;
    private double pricePerHour;

    public RoomType() {}

    public RoomType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public RoomType(int id, String name, double pricePerHour) {
        this.id = id;
        this.name = name;
        this.pricePerHour = pricePerHour;
    }

    public RoomType(String name, double pricePerHour) {
        this.name = name;
        this.pricePerHour = pricePerHour;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Phòng " + name;
    }
}
