package com.ht.hoteldelluna.models;

public class Floor {
    private int id;
    private int num;

    public Floor() {};
    public Floor(int num) {
        this.num = num;
    }
    public Floor(int id, int num) {
        this.id = id;
        this.num = num;
    }

<<<<<<< HEAD
=======
    public Floor(int num) {
        this.num = num;
    }
>>>>>>> a679b26fb5c0dcbcbf93cf73dd0a7679e27fcc00

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Tầng " + num;
    }
}