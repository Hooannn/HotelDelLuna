package com.ht.hoteldelluna.controllers.main.FloorSetting;

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
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NewFloor {
    @FXML
    private MFXTextField nameOfRoom;
    public Stage stage;

    private NewEntityDelegate delegate;
    public NewFloor(NewEntityDelegate delegate) {
        this.delegate = delegate;
    }
    @FXML
    public void initialize() {
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
        FloorsService floorsService = new FloorsService();
        if (name.isEmpty() || name.isBlank()) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tầng không được để trống.");
            return;
        }
        try {
            Integer.parseInt(name);
        } catch (NumberFormatException e) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tầng phải là số.");
            return;
        }
        if (floorsService.checkFloorNum(Integer.parseInt(name))) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tầng đã tồn tại.");
            return;
        }
        //check floor is number

        Floor floor = new Floor(Integer.parseInt(name));
        floorsService.addFloor(floor);
        delegate.onCreated();
    }

    @FXML
    public void checkCancel (ActionEvent event) {
        delegate.onCancelled();
    }
}
