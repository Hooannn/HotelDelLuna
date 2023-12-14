package com.ht.hoteldelluna.controllers.main.roomSetting;

import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.delegate.NewEntityDelegate;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class NewRoom {
    @FXML
    private MFXTextField nameOfRoom;
    @FXML
    private MFXComboBox floor;
    @FXML
    private MFXComboBox type;
    public Stage stage;
    private NewEntityDelegate delegate;
    public NewRoom(NewEntityDelegate delegate) {
        this.delegate = delegate;
    }

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

    public void showAlertMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void checkOK (ActionEvent event) {
        String name = nameOfRoom.getText();
        RoomsService roomsService = new RoomsService();
        if (name.isEmpty() || name.isBlank()) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tên phòng không được để trống.");
            return;
        }
        if (roomsService.checkNameofRoom(name)) {
            showAlertMessage("Yêu cầu không hợp lệ", "Phòng đã tồn tại.");
            return;
        }
        else if (floor.getSelectionModel().getSelectedItem() == null) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tầng không được để trống.");
            return;
        }
        else if (type.getSelectionModel().getSelectedItem() == null) {
            showAlertMessage("Yêu cầu không hợp lệ", "Loại phòng không được để trống.");
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

        delegate.onCreated();
    }

    @FXML
    public void checkCancel (ActionEvent event) {
        delegate.onCancelled();
    }
}
