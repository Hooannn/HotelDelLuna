package com.ht.hoteldelluna.controllers.main.RoomManager;

import com.ht.hoteldelluna.models.Room;

public interface RoomCardControllerDelegate {
    void onCheckedIn(Room room);

    void onCheckedOut(Room room);

    void onCleaned(Room room);

    void onCancelled(Room room);
    void onUpdated(Room room);
}
