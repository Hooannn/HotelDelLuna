package com.ht.hoteldelluna.models;

import com.ht.hoteldelluna.enums.UserRole;
import org.bson.types.ObjectId;

public class User {
    private ObjectId _id;
    private String fullName;
    private String username;
    private String password;
    private UserRole role;

    public User() {}
    public User(String fullName, String username, String password, UserRole role) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public ObjectId getId() {
        return this._id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public UserRole getRole() {
        return this.role;
    }
}