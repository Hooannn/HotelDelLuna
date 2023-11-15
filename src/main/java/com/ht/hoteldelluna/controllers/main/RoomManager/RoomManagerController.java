package com.ht.hoteldelluna.controllers.main.RoomManager;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.ReservationsService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Reservation;
import com.ht.hoteldelluna.models.Room;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RoomManagerController implements Initializable, RoomCardControllerDelegate {
    @FXML
    private FlowPane roomFlowPane;
    @FXML
    private MFXComboBox<Floor> floorsSelection;
    private final RoomsService roomsService = new RoomsService();
    private final FloorsService floorsService = new FloorsService();
    private final ReservationsService reservationsService = new ReservationsService();
    private List<Room> rooms;
    private List<Reservation> reservations;
    private List<Floor> floors;

    private final Stage stage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                floors = floorsService.getFloors();
                rooms = roomsService.getRooms();
                reservations = reservationsService.getOpeningReservations();
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            setupFloorsSelection();
        });
        new Thread(task).start();
    }

    public RoomManagerController(Stage stage) {
        this.stage = stage;
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
            FXMLLoader loader;
            switch (room.getStatus()) {
                case OCCUPIED -> {
                    loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomManager/OccupiedRoomCard.fxml"));
                }
                case MAINTENANCE -> {
                    loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomManager/MaintenanceRoomCard.fxml"));
                }
                default -> {
                    loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomManager/AvailableRoomCard.fxml"));
                }
            }
            Reservation reservation = reservations
                    .stream()
                    .filter(r -> r.getRoom() != null && r.getRoom().getId().toString().equals(room.getId().toString()))
                    .findFirst()
                    .orElse(null);

            loader.setControllerFactory(c -> new RoomCardController(stage, room, reservation, this));
            try {
                roomFlowPane.getChildren().add(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String toString() {
        return "RoomManagerController{" +
                "rooms=" + rooms +
                ", reservations=" + reservations +
                ", floors=" + floors +
                '}';
    }

    private void startLoading() {
        Platform.runLater(() -> {
            roomFlowPane.setDisable(true);
        });
    }

    private void stopLoading() {
        Platform.runLater(() -> {
            roomFlowPane.setDisable(false);
        });
    }

    @Override
    public void onCheckedIn(Room room) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                startLoading();
                rooms = roomsService.getRooms();
                reservations = reservationsService.getOpeningReservations();
                stopLoading();
                return null;
            }
        };
        task.setOnSucceeded(taskFinishEvent -> setupRoomCards(floorsSelection.getSelectedItem().getNum()));
        new Thread(task).start();
    }

    @Override
    public void onCheckedOut(Room room) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                startLoading();
                rooms = roomsService.getRooms();
                reservations = reservationsService.getOpeningReservations();
                stopLoading();
                return null;
            }
        };
        task.setOnSucceeded(taskFinishEvent -> setupRoomCards(floorsSelection.getSelectedItem().getNum()));
        new Thread(task).start();
    }

    @Override
    public void onCleaned(Room room) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                startLoading();
                rooms = roomsService.getRooms();
                reservations = reservationsService.getOpeningReservations();
                stopLoading();
                return null;
            }
        };
        task.setOnSucceeded(taskFinishEvent -> setupRoomCards(floorsSelection.getSelectedItem().getNum()));
        new Thread(task).start();
    }

    @Override
    public void onCancelled(Room room) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                startLoading();
                rooms = roomsService.getRooms();
                reservations = reservationsService.getOpeningReservations();
                stopLoading();
                return null;
            }
        };
        task.setOnSucceeded(taskFinishEvent -> setupRoomCards(floorsSelection.getSelectedItem().getNum()));
        new Thread(task).start();
    }

    @Override
    public void onUpdated(Room room) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                startLoading();
                rooms = roomsService.getRooms();
                reservations = reservationsService.getOpeningReservations();
                stopLoading();
                return null;
            }
        };
        task.setOnSucceeded(taskFinishEvent -> setupRoomCards(floorsSelection.getSelectedItem().getNum()));
        new Thread(task).start();
    }
}
