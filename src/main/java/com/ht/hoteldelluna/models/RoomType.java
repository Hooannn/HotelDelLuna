package com.ht.hoteldelluna.models;


import org.bson.types.ObjectId;

public class RoomType {
    private ObjectId _id;
    private String name;

    public RoomType() {}
    public RoomType(String name) {
        this.name = name;
    }
    public ObjectId getId() {
        return this._id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                '}';
    }
}
