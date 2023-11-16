package com.ht.hoteldelluna.backend;

import com.ht.hoteldelluna.models.User;

public class AppState {
    public static final AppState shared = new AppState();
    private User authUser;
    private AppState() {}

    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }

    public User getAuthUser() {
        return authUser;
    }
}
