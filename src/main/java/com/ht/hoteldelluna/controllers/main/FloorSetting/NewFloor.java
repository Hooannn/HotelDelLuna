package com.ht.hoteldelluna.controllers.main.FloorSetting;

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

public class NewFloor {
    @FXML
    private Button okNewRoom;
    @FXML
    private Button cancelNewRoom;
    @FXML
    private MFXTextField num;


    public Stage stage;
    // Write a funciton to check nameofRoom in Room database has been existed or not

    @FXML
    public void initialize() {
    }



    public void showFloorExistsNotification() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Floor Exists");
        alert.setHeaderText(null);
        alert.setContentText("The Floor already exists in the database.");
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
    @FXML
    public  void checkOK (ActionEvent event) {
        String fl = num.getText();
        FloorsService floorsService = new FloorsService();
        if (num.getText().isEmpty()) {
            showFloorEmptyNotification();
            return;
        }
        else if (floorsService.checkFloorNum(Integer.parseInt(fl))) {
            showFloorExistsNotification();
            return;
        }


        Floor newfloor = new Floor( Integer.parseInt(fl));
        floorsService.addFloor(newfloor);
        Stage stage = (Stage) num.getScene().getWindow();
        stage.close();
    }
    public void checkCancel (ActionEvent event) {
        Stage stage = (Stage) num.getScene().getWindow();
        stage.close();
    }
}
