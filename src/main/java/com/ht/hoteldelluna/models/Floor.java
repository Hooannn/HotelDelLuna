package com.ht.hoteldelluna.models;
public class Floor {
    private String id;
    private int num;

    public Floor(String id, int num) {
        this.id = id;
        this.num = num;
    }

    public String getId() {
        return this.id;
    }

    public int getNum() {
        return this.num;
    }
}