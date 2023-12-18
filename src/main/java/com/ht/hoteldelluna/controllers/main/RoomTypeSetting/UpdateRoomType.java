package com.ht.hoteldelluna.controllers.main.RoomTypeSetting;

import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.delegate.UpdateEntityDelegate;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.RoomType;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateRoomType implements Initializable {
    @FXML
    private MFXTextField name;
    @FXML
    private MFXTextField price;
    private UpdateEntityDelegate delegate;
    private RoomType roomType;
    public UpdateRoomType(RoomType roomType , UpdateEntityDelegate delegate) {
        this.roomType = roomType;
        this.delegate = delegate;
    }



    public void displayRoomInformation(RoomType roomType) {
        name.setText(String.valueOf(roomType.getName()));
        price.setText(String.valueOf(roomType.getPricePerHour()));
    }
    public void showAlertMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void updateRoomType(javafx.event.ActionEvent event) {
        RoomTypesService roomTypesService = new RoomTypesService();
        String updatedName = name.getText();
        String updatedPrice = price.getText();
        if (updatedName.isEmpty() || updatedName.isBlank()) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tên loại phòng không được để trống.");
            return;
        }
        if (updatedPrice.isEmpty() || updatedPrice.isBlank()) {
            showAlertMessage("Yêu cầu không hợp lệ", "Giá phòng không được để trống.");
            return;
        }
        if (!updatedName.equals(roomType.getName()) && roomTypesService.checkRoomTypeName(updatedName)) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tên loại phòng đã tồn tại.");
            return;
        }
        //check price is number
        try {
            System.out.println();
            Integer.parseInt(updatedPrice);
        } catch (NumberFormatException e) {
            showAlertMessage("Yêu cầu không hợp lệ", "Giá phòng phải là số.");
            return;
        }

        roomTypesService.updateRoomType(roomType.getId(), updatedName, Integer.parseInt(updatedPrice));
        delegate.onUpdated();
    }

    @FXML
    public void cancel() {
        delegate.onCancelled();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayRoomInformation(roomType);
    }


}