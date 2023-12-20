package com.ht.hoteldelluna.controllers.main.RoomManager;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.ReservationsService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.controllers.Reloadable;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RoomManagerController implements Initializable, RoomCardControllerDelegate, Reloadable {
    @FXML
    private StackPane loadingPane;
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
        setupInterval();
    }
    public RoomManagerController(Stage stage) {
        this.stage = stage;
    }
    private void setupInterval() {
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(() -> {
                    if (floorsSelection.getSelectedItem() != null) {
                        setupRoomCards(floorsSelection.getSelectedItem().getId());
                    }
                });
            }
        }, 60000, 60000);
    }
    private void setupFloorsSelection() {
        Floor prevSelected = floorsSelection.getSelectedItem();
        floorsSelection.getItems().clear();
        floorsSelection.setItems(FXCollections.observableList(floors));
        floorsSelection.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                setupRoomCards(newValue.getId());
        });

        if (prevSelected == null) {
            floorsSelection.selectFirst();
        } else {
            int prevId = prevSelected.getId();
            Floor item = floorsSelection.getItems().stream().filter(i ->
                i.getId() == prevId
            ).findFirst().orElse(null);

            if (item == null) {
                floorsSelection.selectFirst();
            } else {
                floorsSelection.selectItem(item);
            }
        }

    }

    private void setupRoomCards(int floorId) {
        roomFlowPane.getChildren().clear();
        List<Room> renderRooms = rooms.stream().filter(room -> room.getFloor().getId() == floorId).toList();
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
                    .filter(r -> r.getRoom() != null && r.getRoom().getId() == room.getId())
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
            loadingPane.setVisible(true);
        });
    }

    private void stopLoading() {
        Platform.runLater(() -> {
            roomFlowPane.setDisable(false);
            loadingPane.setVisible(false);
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
        task.setOnSucceeded(taskFinishEvent -> setupRoomCards(floorsSelection.getSelectedItem().getId()));
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
        task.setOnSucceeded(taskFinishEvent -> setupRoomCards(floorsSelection.getSelectedItem().getId()));
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
        task.setOnSucceeded(taskFinishEvent -> setupRoomCards(floorsSelection.getSelectedItem().getId()));
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
        task.setOnSucceeded(taskFinishEvent -> setupRoomCards(floorsSelection.getSelectedItem().getId()));
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
        task.setOnSucceeded(taskFinishEvent -> setupRoomCards(floorsSelection.getSelectedItem().getId()));
        new Thread(task).start();
    }

    @Override
    public void reload() {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                startLoading();
                floors = floorsService.getFloors();
                rooms = roomsService.getRooms();
                reservations = reservationsService.getOpeningReservations();
                stopLoading();
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            setupFloorsSelection();
        });
        new Thread(task).start();
    }
}
