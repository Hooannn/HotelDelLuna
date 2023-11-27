package com.ht.hoteldelluna.controllers.main.roomSetting;

import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NewRoom {

    @FXML
    private Button okNewRoom;
    @FXML
    private Button cancelNewRoom;
    @FXML
    private MFXTextField nameOfRoom;
    @FXML
    private MFXComboBox floor;
    @FXML
    private MFXComboBox type;
    public Stage stage;
    // Write a funciton to check nameofRoom in Room database has been existed or not

    @FXML
    public void initialize() {
        FloorsService floorsService = new FloorsService();
        RoomTypesService roomTypesService = new RoomTypesService();
        for (Floor floor : floorsService.getFloors()) {
            this.floor.getItems().add(floor.getNum());
        }
        for (RoomType roomType : roomTypesService.getRoomTypes()) {
            this.type.getItems().add(roomType.getName());
        }
    }


    public void showRoomExistsNotification() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Room Exists");
        alert.setHeaderText(null);
        alert.setContentText("The room already exists in the database.");
        alert.showAndWait();
    }
    //Check if floor is empty
    public void showFloorEmptyNotification() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Floor Empty");
        alert.setHeaderText(null);
        alert.setContentText("Please choose floor.");
        alert.showAndWait();
    }
    //Check if type is empty
    public void showTypeEmptyNotification() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Type Empty");
        alert.setHeaderText(null);
        alert.setContentText("Please choose type.");
        alert.showAndWait();
    }
    @FXML
    public  void checkOK (ActionEvent event) {
        String name = nameOfRoom.getText();
        RoomsService roomsService = new RoomsService();
        //check nameofRoom in Room database has been existed or not. If existed then show a dialog to notify
        if (roomsService.checkNameofRoom(name)) {
            showRoomExistsNotification();
            return;
        }
        else if (floor.getSelectionModel().getSelectedItem() == null) {
            showFloorEmptyNotification();
            return;
        }
        else if (type.getSelectionModel().getSelectedItem() == null) {
            showTypeEmptyNotification();
            return;
        }
        RoomTypesService roomTypesService = new RoomTypesService();
        RoomType roomType = roomTypesService.getRoomTypeByName(type.getText());
        String t = type.getText();
        String floorText = floor.getText();
        int intValue = Integer.parseInt(floorText);
        FloorsService floorsService = new FloorsService();
        Floor floorNumber = floorsService.getFloorByNum(intValue);

        Room room = new Room(name, roomType, floorNumber, RoomStatus.AVAILABLE);
        roomsService.addRoom(room, String.valueOf(roomType.getId()), String.valueOf(floorNumber.getId()));
        Stage stage = (Stage) nameOfRoom.getScene().getWindow();
        stage.close();
    }
    public void checkCancel (ActionEvent event) {
        Stage stage = (Stage) nameOfRoom.getScene().getWindow();
        stage.close();
    }


}
