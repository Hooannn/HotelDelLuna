package com.ht.hoteldelluna.controllers.main.roomSetting;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.controllers.Reloadable;
import com.ht.hoteldelluna.delegate.NewEntityDelegate;
import com.ht.hoteldelluna.delegate.UpdateEntityDelegate;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;

import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.palexdev.materialfx.controls.MFXButton;

public class RoomSettingController implements Initializable, Reloadable, NewEntityDelegate, UpdateEntityDelegate {
    @FXML
    private MFXComboBox<Floor> filterFloorSelection;
    @FXML
    private MFXComboBox<RoomType> filterTypeSelection;
    @FXML
    private MFXComboBox<String> filterStatusSelection;
    @FXML
    private MFXTableView<Room> roomsTable;
    @FXML
    private MFXButton btnNewCreate;
    private List<Room> rooms;
    private final RoomsService roomsService = new RoomsService();
    private final RoomTypesService roomTypesService = new RoomTypesService();
    private final FloorsService floorsService = new FloorsService();
    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;
    private final Stage stage;
    public RoomSettingController(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupPaginated();
        setupDialog();
        setupFilter();
    }

    private void setupFilter() {
        filterStatusSelection.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            List<Room> nextRooms = rooms.stream().filter(room -> room.getStatus().getText().equals(newValue)).toList();
            roomsTable.setItems(FXCollections.observableArrayList(nextRooms));
        });
        filterTypeSelection.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            List<Room> nextRooms = rooms.stream().filter(room -> room.getType().getId() == newValue.getId()).toList();
            roomsTable.setItems(FXCollections.observableArrayList(nextRooms));
        });
        filterFloorSelection.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            List<Room> nextRooms = rooms.stream().filter(room -> room.getFloor().getId() == newValue.getId()).toList();
            roomsTable.setItems(FXCollections.observableArrayList(nextRooms));
        });
    }
    private void deleteRoom(ActionEvent event,Room room) throws IOException {
        if (room == null) {
            return;
        }
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Không thể xoá phòng này");
            alert.setHeaderText(null);
            alert.setContentText("Chỉ có thể xoá phòng có trạng thái trống");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xóa Phòng");
        alert.setHeaderText(null);
        alert.setContentText("Bạn chắc chắn muốn xóa phòng: " + room.getName());
        ButtonType buttonTypeCancel = new ButtonType("Huỷ bỏ", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonTypeCancel, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            roomsService.deleteRoom(String.valueOf(room.getId()));
            alert.close();
            reload();
        }
        if (alert.getResult().getText().equals("Huỷ bỏ")) {
            alert.close();
        }

        try {
            if (!alert.isShowing()) {
                reload();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupPaginated() {
        MFXTableColumn<Room> idxColumn = new MFXTableColumn<>("STT", false);
        idxColumn.setRowCellFactory(room ->  new MFXTableRowCell<>(_room -> rooms.indexOf(_room) + 1));

        MFXTableColumn<Room> nameColumn = new MFXTableColumn<>("Tên phòng", false,
                Comparator.comparing(Room::getName));
        MFXTableColumn<Room> typeColumn = new MFXTableColumn<>("Loại phòng", false,
                Comparator.comparing(room -> room.getType().getName()));
        MFXTableColumn<Room> floorColumn = new MFXTableColumn<>("Tầng", false,
                Comparator.comparing(room -> room.getFloor().getNum()));
        MFXTableColumn<Room> statusColumn = new MFXTableColumn<>("Trạng thái", false,
                Comparator.comparing(Room::getStatus));
        MFXTableColumn<Room> actionColumn = new MFXTableColumn<>("Thao tác", false);
        nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Room::getName));
        typeColumn.setRowCellFactory(device -> new MFXTableRowCell<>(room -> room.getType().getName()));
        floorColumn.setRowCellFactory(device -> new MFXTableRowCell<>(room -> room.getFloor().getNum()));
        statusColumn.setRowCellFactory(device -> new MFXTableRowCell<>(room -> room.getStatus().getText()));

        actionColumn.setRowCellFactory(room -> {
            AtomicInteger id = new AtomicInteger();
            MFXTableRowCell cell = new MFXTableRowCell<>(_room -> {
                id.set(((Room) _room).getId());
                return "";
            });
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(8);
            MFXButton editBtn = new MFXButton("Sửa");
            MFXButton deleteBtn = new MFXButton("Xóa");
            editBtn.getStyleClass().add("table-modify-record-btn");
            deleteBtn.getStyleClass().add("table-delete-record-btn");
            hBox.getChildren().addAll(editBtn,deleteBtn);
            editBtn.setOnAction(e->{
                try {
                    Room selection = roomsService.getRoomById(String.valueOf(id.get()));
                    showUpdateRoomDialog(selection);
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }
                    }
            );

            deleteBtn.setOnAction(e->{
                try {
                    Room selection = roomsService.getRoomById(String.valueOf(id.get()));
                    deleteRoom(e,selection);
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }
            });
            cell.setAlignment(Pos.TOP_CENTER);
            cell.setGraphic(hBox);
            return cell;
        });

        idxColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.08));
        nameColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.21));
        typeColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.21));
        floorColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.19));
        statusColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.19));
        actionColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.13));


        roomsTable.getTableColumns().addAll(idxColumn, nameColumn, typeColumn, floorColumn, statusColumn,actionColumn);
    }



    private void prepareFilterData() {
        filterStatusSelection.setItems(FXCollections.observableArrayList(List.of(
                RoomStatus.AVAILABLE.getText(), RoomStatus.MAINTENANCE.getText(), RoomStatus.OCCUPIED.getText()
        )));
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                List<RoomType> roomTypes = roomTypesService.getRoomTypes();
                List<Floor> floors = floorsService.getFloors();
                filterTypeSelection.setItems(FXCollections.observableArrayList(roomTypes));
                filterFloorSelection.setItems(FXCollections.observableArrayList(floors));
                return null;
            }
        };
        new Thread(task).start();
    }

    @Override
    public void reload() {
        this.rooms = roomsService.getRooms();
        roomsTable.setItems(FXCollections.observableArrayList(rooms));
        clearFilter();
        prepareFilterData();
    }
    public void clearFilter(ActionEvent actionEvent) {
        clearFilter();
    }

    private void clearFilter() {
        filterFloorSelection.clearSelection();
        filterTypeSelection.clearSelection();
        filterStatusSelection.clearSelection();
        roomsTable.setItems(FXCollections.observableArrayList(rooms));
    }

    private void setupDialog() {
        Platform.runLater(() -> {
            dialogContent = MFXGenericDialogBuilder.build()
                    .makeScrollable(true)
                    .setShowMinimize(false)
                    .setShowAlwaysOnTop(false)
                    .get();

            dialog = MFXGenericDialogBuilder.build(dialogContent)
                    .toStageDialogBuilder()
                    .initOwner(stage)
                    .initModality(Modality.APPLICATION_MODAL)
                    .setDraggable(false)
                    .setOwnerNode((Pane) stage.getScene().getRoot())
                    .setScrimPriority(ScrimPriority.NODE)
                    .setScrimOwner(true)
                    .get();

            dialogContent.setMaxSize(stage.getMaxWidth(), stage.getMaxHeight());
            dialogContent.getStyleClass().add("mfx-info-dialog");
        });
    }

    @FXML
    private void showNewRoomDialog() {
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/newRoomForm.fxml"));
        NewRoom controller = new NewRoom(this);
        loader.setControllerFactory(r -> controller);
        dialogContent.setHeaderText("Tạo phòng mới");
        dialogContent.setContent(null);
        dialogContent.setContentText(null);
        try {
            dialogContent.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.showDialog();
    }

    private void showUpdateRoomDialog(Room selection) {
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomManager/updateRoomForm.fxml"));
        UpdateRoomForm controller = new UpdateRoomForm(selection, this);
        loader.setControllerFactory(r -> controller);
        dialogContent.setHeaderText("Sửa thông tin phòng");
        dialogContent.setContent(null);
        dialogContent.setContentText(null);
        try {
            dialogContent.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.showDialog();
    }

    @Override
    public void onCreated() {
        dialog.close();
        reload();
    }

    @Override
    public void onUpdated() {
        dialog.close();
        reload();
    }

    @Override
    public void onCancelled() {
        dialog.close();
    }
}
