package com.ht.hoteldelluna.controllers.main.FloorSetting;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.controllers.main.roomSetting.NewRoom;
import com.ht.hoteldelluna.controllers.main.roomSetting.UpdateRoomForm;
import com.ht.hoteldelluna.delegate.NewEntityDelegate;
import com.ht.hoteldelluna.delegate.UpdateEntityDelegate;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.controllers.Reloadable;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class FloorSettingController implements Initializable, Reloadable, NewEntityDelegate, UpdateEntityDelegate {
    @FXML
    private MFXButton btnNewCreate;
    @FXML
    private MFXTableView<Floor> roomsTable;
    private List<Floor> floors;
    private final FloorsService floorsService = new FloorsService();

    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;
    private final Stage stage;

    public FloorSettingController(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupPaginated();
        setupFilter();
        setupDialog();

    }
    private void setupFilter() {

    }
    private void deleteRoom(ActionEvent event,Floor floor) throws IOException {
        if (floor == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xóa Tầng");
        alert.setHeaderText(null);
        alert.setContentText("Bạn chắc chắn muốn xóa Tầng: " + floor.getNum());
        ButtonType buttonTypeCancel = new ButtonType("Huỷ bỏ", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonTypeCancel, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            //check roomtable have a floor which is deleted. if have, dont delete,and show alert
            RoomsService roomsService = new RoomsService();
            List<Room> rooms = roomsService.getRooms();
            for (Room room : rooms) {
                if (room.getFloor().getId() == floor.getId()) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Thông báo");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Không thể xoá tầng này vì tầng này vẫn còn phòng. Bạn hãy xóa tất cả các phòng trong tầng này trước khi xóa tầng.");
                    alert1.showAndWait();
                    return;
                }
            }
            floorsService.deleteFloorByID(String.valueOf(floor.getId()));
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

    @FXML
    private void showNewRoomDialog() {
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/FloorSetting/NewFloorForm.fxml"));
        NewFloor controller = new NewFloor(this);
        loader.setControllerFactory(r -> controller);
        dialogContent.setHeaderText("Tạo tầng mới");
        dialogContent.setContent(null);
        dialogContent.setContentText(null);
        try {
            dialogContent.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.showDialog();
    }

    private void showUpdateRoomDialog(Floor selection) {
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/FloorSetting/updateFloor.fxml"));
        UpdateFloor controller = new UpdateFloor(selection, this);
        loader.setControllerFactory(r -> controller);
        dialogContent.setHeaderText("Sửa thông tin tầng");
        dialogContent.setContent(null);
        dialogContent.setContentText(null);
        try {
            dialogContent.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.showDialog();
    }

    public void setupPaginated() {
        MFXTableColumn<Floor> idxColumn = new MFXTableColumn<>("STT", false);
        idxColumn.setRowCellFactory(room ->  new MFXTableRowCell<>(_room -> floors.indexOf(_room) + 1));

        MFXTableColumn<Floor> numColumn = new MFXTableColumn<>("Tầng", false,
                Comparator.comparing(Floor::getNum));
        MFXTableColumn<Floor> actionColumn = new MFXTableColumn<>("Thao tác", false);
        numColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Floor::getNum));
        actionColumn.setRowCellFactory(room -> {
            AtomicInteger id = new AtomicInteger();
            MFXTableRowCell cell = new MFXTableRowCell<>(_room -> {
                id.set(((Floor) _room).getId());
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
                            Floor selection = floorsService.getFloorById(String.valueOf(id.get()));
                            showUpdateRoomDialog(selection);
                        } catch (Exception ioException) {
                            ioException.printStackTrace();
                        }
                    }
            );

            deleteBtn.setOnAction(e->{
                try {
                    Floor selection = floorsService.getFloorById(String.valueOf(id.get()));
                    deleteRoom(e,selection);
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }
            });
            cell.setAlignment(Pos.TOP_CENTER);
            cell.setGraphic(hBox);
            return cell;
        });



        idxColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.44));
        actionColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.12));
        numColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.44));


        roomsTable.getTableColumns().addAll(idxColumn,  numColumn, actionColumn);
    }


    private void fetchDocuments() {
        Task<ObservableList<Floor>> task = new Task<>() {
            @Override
            protected ObservableList<Floor> call() {
                return FXCollections.observableArrayList(floorsService.getFloors());
            }
        };
        task.setOnSucceeded(e -> {
            roomsTable.setItems(task.getValue());
        });
        new Thread(task).start();
    }

    @Override
    public void reload() {
        this.floors = floorsService.getFloors();
        roomsTable.setItems(FXCollections.observableArrayList(floors));
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