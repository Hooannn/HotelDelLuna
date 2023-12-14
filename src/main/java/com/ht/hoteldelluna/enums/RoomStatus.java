package com.ht.hoteldelluna.enums;

public enum RoomStatus {
    AVAILABLE("Trống"),
    OCCUPIED("Đang được thuê"),
    MAINTENANCE("Đang dọn"),
    ;

    private final String text;
    RoomStatus(String s) {
        text = s;
    }
    public String getText() {
        return text;
    }
}
