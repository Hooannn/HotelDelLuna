package com.ht.hoteldelluna.models;

import org.bson.types.ObjectId;

public class Floor {
    private ObjectId _id;
    private int num;

    public Floor() {}
    public Floor(int num) {
        this.num = num;
    }

    public void setId(ObjectId id) {
        this._id = id;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ObjectId getId() {
        return this._id;
    }

    public int getNum() {
        return this.num;
    }

    @Override
    public String toString() {
        return "Floor{" +
                "_id=" + _id +
                ", num=" + num +
                '}';
    }
}