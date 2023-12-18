package com.ht.hoteldelluna.controllers.main.FloorSetting;

import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.delegate.UpdateEntityDelegate;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateFloor implements Initializable {
    @FXML
    private MFXTextField name;
    @FXML
    private MFXTextField id;
    private UpdateEntityDelegate delegate;
    private Floor floor;

    public UpdateFloor(Floor floor, UpdateEntityDelegate delegate) {
        this.floor = floor;
        this.delegate = delegate;
    }
    public void displayRoomInformation(Floor floor) {
        name.setText(String.valueOf(floor.getNum()));
        id.setText(String.valueOf(floor.getId()));
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
        FloorsService floorsService = new FloorsService();
        String updatedName = name.getText();
        if (updatedName.isEmpty() || updatedName.isBlank()) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tầng không được để trống.");
            return;
        }
        try {
            Integer.parseInt(updatedName);
        } catch (NumberFormatException e) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tầng phải là số.");
            return;
        }
        if (!updatedName.equals(floor.getId()) && floorsService.checkFloorNum(Integer.parseInt(updatedName))) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tầng đã tồn tại.");
            return;
        }
        //check floor is number

        floorsService.updateFloor(floor.getId(), Integer.parseInt(updatedName));
        delegate.onUpdated();
    }

    @FXML
    public void cancel() {
        delegate.onCancelled();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setDisable(true);
        displayRoomInformation(floor);
    }

}
