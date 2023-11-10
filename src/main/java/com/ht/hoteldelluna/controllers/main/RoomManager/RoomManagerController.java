package com.ht.hoteldelluna.controllers.main.RoomManager;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Room;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RoomManagerController implements Initializable {
    @FXML
    private FlowPane roomFlowPane;
    @FXML
    private MFXComboBox<Floor> floorsSelection;
    private final RoomsService roomsService = new RoomsService();
    private final FloorsService floorsService = new FloorsService();

    private List<Room> rooms;
    private List<Floor> floors;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupFloorsSelection();
    }

    public RoomManagerController(Stage stage) {
        floors = floorsService.getFloors();
        rooms = roomsService.getRooms();
    }

    private void setupFloorsSelection() {
        floorsSelection.setItems(FXCollections.observableList(floors));
        floorsSelection.valueProperty().addListener((observable, oldValue, newValue) -> setupRoomCards(newValue.getNum()));
        floorsSelection.selectFirst();
    }

    private void setupRoomCards(int floor) {
        roomFlowPane.getChildren().clear();
        List<Room> renderRooms = rooms.stream().filter(room -> room.getFloor().getNum() == floor).toList();
        renderRooms.forEach(room -> {
            FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomManager/RoomCard.fxml"));
            loader.setControllerFactory(c -> new RoomCardController(room));
            try {
                roomFlowPane.getChildren().add(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
