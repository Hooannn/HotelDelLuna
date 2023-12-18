package com.ht.hoteldelluna.controllers.main.RoomTypeSetting;

import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.delegate.NewEntityDelegate;
import com.ht.hoteldelluna.models.Floor;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class NewRoomType {
    @FXML
    private MFXTextField nameOfRoom;
    @FXML
    private MFXTextField price;

    public Stage stage;

    private NewEntityDelegate delegate;
    public NewRoomType(NewEntityDelegate delegate) {
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
        RoomTypesService roomTypesService = new RoomTypesService();

        if (name.isEmpty() || name.isBlank()) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tên loại phòng không được để trống.");
            return;
        }
        if (roomTypesService.checkRoomTypeName(name)) {
            showAlertMessage("Yêu cầu không hợp lệ", "Loại phòng này đã tồn tại.");
            return;
        }
          String price = this.price.getText();
        if (price.isEmpty() || price.isBlank()) {
            showAlertMessage("Yêu cầu không hợp lệ", "Giá phòng không được để trống.");
            return;
        }
        //check price is number
        try {
            Integer.parseInt(price);
        } catch (NumberFormatException e) {
            showAlertMessage("Yêu cầu không hợp lệ", "Giá phòng phải là số.");
            return;
        }

        roomTypesService.addRoomType(name, Integer.parseInt(price));
        delegate.onCreated();
    }

    @FXML
    public void checkCancel (ActionEvent event) {
        delegate.onCancelled();
    }
}
