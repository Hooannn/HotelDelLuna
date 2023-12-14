package com.ht.hoteldelluna.controllers.main.roomSetting;

import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.delegate.UpdateEntityDelegate;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateRoomForm implements Initializable {
    @FXML
    private MFXTextField name;

    @FXML
    private MFXComboBox<Floor> floor;

    @FXML
    private MFXComboBox<RoomType> type;

    private UpdateEntityDelegate delegate;
    private Room room;
    public UpdateRoomForm(Room room, UpdateEntityDelegate delegate) {
        this.room = room;
        this.delegate = delegate;
    }
    public void displayRoomInformation(Room room) {
        name.setText(room.getName());
        floor.setText(room.getFloor().toString());
        type.setText(room.getType().toString());
        if (floor.getItems().contains(room.getFloor())) {
            floor.selectItem(room.getFloor());
        }

        if (type.getItems().contains(room.getType())) {
            type.selectItem(room.getType());
        }
    }

    public void showAlertMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void updateRoom(javafx.event.ActionEvent event) {
        RoomsService roomsService = new RoomsService();
        String updatedName = name.getText();
        if (updatedName.isEmpty() || updatedName.isBlank()) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tên phòng không được để trống.");
            return;
        }
        if (!updatedName.equals(room.getName()) && roomsService.checkNameofRoom(updatedName))
        {
            showAlertMessage("Yêu cầu không hợp lệ", "Phòng đã tồn tại.");
            return;
        }
        Floor valueFloor = floor.getSelectedItem() == null ? room.getFloor() : floor.getSelectedItem();
        RoomType valueRoomType = type.getSelectedItem() == null ? room.getType() : type.getSelectedItem();
        roomsService.updateRoom(String.valueOf(room.getId()), updatedName, String.valueOf(valueRoomType.getId()), String.valueOf(valueFloor.getId()));
        delegate.onUpdated();
    }

    @FXML
    public void cancel() {
        delegate.onCancelled();
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
        displayRoomInformation(room);
    }
}
