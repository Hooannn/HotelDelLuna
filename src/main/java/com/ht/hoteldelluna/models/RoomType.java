package com.ht.hoteldelluna.models;

public class RoomType {
    private String id;
    private String name;

    public RoomType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
