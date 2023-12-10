package com.ht.hoteldelluna.controllers.main.roomSetting;

import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateRoomForm implements Initializable {
    @FXML
    private MFXTextField name;

    @FXML
    private MFXComboBox<Floor> floor;

    @FXML
    private MFXComboBox<RoomType> type;

    @FXML
    private MFXTextField price;

    @FXML
    private MFXComboBox<String> state;
    @FXML
    private MFXButton ok;
    @FXML
    private MFXButton cancel;
    private String roomName;

    private Room room;
    public UpdateRoomForm(Room room) {
        // Initialize your form (if needed) e.g., populate ComboBox items
        this.room = room;


    }
    public void displayRoomInformation(Room room) {
        name.setText(room.getName());
        floor.setText(room.getFloor().toString());
        type.setText(room.getType().toString());
        // Kiểm tra xem giá trị có tồn tại trong danh sách không
        if (floor.getItems().contains(room.getFloor())) {
            floor.getSelectionModel().selectItem(room.getFloor());
        }

        if (type.getItems().contains(room.getType())) {
            type.getSelectionModel().selectItem(room.getType());
        }

        price.setText(String.valueOf(room.getType().getPricePerHour()));

        // Kiểm tra xem giá trị có tồn tại trong danh sách không
        if (state.getItems().contains(room.getStatus().toString())) {
            state.getSelectionModel().selectItem(room.getStatus().toString());
        }

    }
    public void showRoomExistsNotification() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Room Exists");
        alert.setHeaderText(null);
        alert.setContentText("The room already exists in the database.");
        alert.showAndWait();
    }
    private void updateRoom(javafx.event.ActionEvent event) throws Exception {

        RoomsService roomsService = new RoomsService();
        String roomName = name.getText();
        if (roomsService.checkNameofRoom(roomName))
        {
            showRoomExistsNotification();
            return;
        }
        if ( floor.getSelectedItem() == null || type.getSelectedItem() == null) {
            return;
        }
        else {

            Floor valueFloor= floor.getSelectedItem();
            RoomType valueRoomType = type.getSelectedItem();
            roomsService.updateRoom(String.valueOf(room.getId()), roomName, String.valueOf(valueRoomType.getId()), String.valueOf(valueFloor.getId()));
        }
        // Update room

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FloorsService floorsService = new FloorsService();
        RoomTypesService roomTypesService = new RoomTypesService();
        for (Floor floor : floorsService.getFloors()) {
            this.floor.getItems().add(floor);
        }
        for (RoomType roomType : roomTypesService.getRoomTypes()) {
            this.type.getItems().add(roomType);
        }
        for (RoomStatus status : RoomStatus.values()) {
            this.state.getItems().add(String.valueOf(status));
        }
        displayRoomInformation(room);
        price.setDisable(true);
        state.setDisable(true);
        ok.setOnAction(e -> {
            try {
                updateRoom(e);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Stage stage = (Stage) name.getScene().getWindow();
            stage.close();
        });
        cancel.setOnAction(e -> {
            Stage stage = (Stage) name.getScene().getWindow();
            stage.close();
        });

    }
}
