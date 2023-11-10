package com.ht.hoteldelluna.controllers.main;

import com.ht.hoteldelluna.backend.services.RoomServices;
import com.ht.hoteldelluna.models.Room;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RoomManagerController implements Initializable {
    RoomServices roomServices = new RoomServices();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Room> rooms = roomServices.getRooms();
        System.out.println(rooms);
    }

    public RoomManagerController(Stage stage) {
        System.out.println("Stage in RoomManagerController: " + stage);
    }
}
