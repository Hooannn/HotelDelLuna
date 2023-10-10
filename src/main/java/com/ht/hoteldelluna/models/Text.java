package com.ht.hoteldelluna.models;

public class Text {
    private String id;
    private String value;

    public Text(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
