package com.ht.hoteldelluna.models;

import com.ht.hoteldelluna.enums.UserRole;
public class User {
    private String id;
    private String fullName;
    private String username;
    private String password;
    private UserRole role;

    public User(String id, String fullName, String username, String password, UserRole role) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return this.id;
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