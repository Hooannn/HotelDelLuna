package com.ht.hoteldelluna.controllers.main.FloorSetting;

import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.models.Floor;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class UpdateFloor {
    @FXML
    private MFXButton ok;
    @FXML
    private MFXButton cancel;
    @FXML
    private MFXTextField id;
    @FXML
    private MFXTextField oldFloor;
    @FXML
    private MFXTextField newFloor;

    private Floor floor;

    public UpdateFloor(Floor floor) {
        this.floor = floor;
    }

    @FXML
    public void initialize() {
        id.setText(String.valueOf(floor.getId()));
        oldFloor.setText(String.valueOf(floor.getNum()));
        FloorsService floorsService = new FloorsService();
        ok.setOnAction(actionEvent -> {
            if (newFloor.getText().isEmpty()) {
                ok.getScene().getWindow().hide();
                return;
            }
            else if (floorsService.checkFloorNum(Integer.parseInt(newFloor.getText()))==true) {

                showFloorExistsNotification();
                return;
            }


            floor.setNum(Integer.parseInt(newFloor.getText()));

            floorsService.updateFloor(floor);
        });
    }
    private void showFloorExistsNotification() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Floor Exists");
        alert.setHeaderText(null);
        alert.setContentText("The floor already exists in the database.");
        alert.showAndWait();
    }



}
