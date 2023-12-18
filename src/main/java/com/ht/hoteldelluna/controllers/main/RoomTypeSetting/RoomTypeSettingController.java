package com.ht.hoteldelluna.controllers.main.RoomTypeSetting;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.controllers.Reloadable;
import com.ht.hoteldelluna.controllers.main.FloorSetting.NewFloor;
import com.ht.hoteldelluna.controllers.main.FloorSetting.UpdateFloor;
import com.ht.hoteldelluna.delegate.NewEntityDelegate;
import com.ht.hoteldelluna.delegate.UpdateEntityDelegate;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomTypeSettingController implements Initializable, Reloadable, NewEntityDelegate, UpdateEntityDelegate {
    @FXML
    private MFXButton btnNewCreate;
    @FXML
    private MFXTableView<RoomType> roomsTable;
    private List<RoomType> roomTypes;
    private final RoomTypesService roomTypesService = new RoomTypesService();

    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;
    private final Stage stage;

    public RoomTypeSettingController(Stage stage) {
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
    private void deleteRoom(ActionEvent event,RoomType roomType) throws IOException {
        if (roomType == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xóa Loai Phòng");
        alert.setHeaderText(null);
        alert.setContentText("Bạn chắc chắn muốn xóa Loại Phoǹng: " + roomType.getName());
        ButtonType buttonTypeCancel = new ButtonType("Huỷ bỏ", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonTypeCancel, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            //check roomtable have a roomtype which is deleted. if have, dont delete,and show alert
            RoomsService roomsService = new RoomsService();
            for (Room room : roomsService.getRooms()) {
                if (room.getType().getId() == roomType.getId()) {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Lỗi");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Không thể xóa Loại Phòng: " + roomType.getName() + " vì có phòng đang sử dụng. Bạn hãy xóa tất cả các phòng có loại phòng này trước.");
                    alert1.showAndWait();
                    return;
                }
            }
            roomTypesService.deleteRoomType(String.valueOf(roomType.getId()));
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
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomTypeSetting/NewRoomTypeForm.fxml"));
        NewRoomType controller = new NewRoomType(this);
        loader.setControllerFactory(r -> controller);
        dialogContent.setHeaderText("Tạo Loại Phòng mới");
        dialogContent.setContent(null);
        dialogContent.setContentText(null);
        try {
            dialogContent.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.showDialog();
    }

    private void showUpdateRoomDialog(RoomType selection) {
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomTypeSetting/UpdateRoomTypeForm.fxml"));
        UpdateRoomType controller = new UpdateRoomType(selection, this);
        loader.setControllerFactory(r -> controller);
        dialogContent.setHeaderText("Sửa thông tin Loại Phòng");
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
        MFXTableColumn<RoomType> idxColumn = new MFXTableColumn<>("STT", false);
        idxColumn.setRowCellFactory(room ->  new MFXTableRowCell<>(_room -> roomTypes.indexOf(_room) + 1));

        MFXTableColumn<RoomType> idColumn = new MFXTableColumn<>("ID", false,
                Comparator.comparing(RoomType::getId));
        MFXTableColumn<RoomType> numColumn = new MFXTableColumn<>("Loại Phòng", false,
                Comparator.comparing(RoomType::getName));
        MFXTableColumn<RoomType> priceColumn = new MFXTableColumn<>("Giá", false,
                Comparator.comparing(RoomType::getPricePerHour));
        MFXTableColumn<RoomType> actionColumn = new MFXTableColumn<>("Thao tác", false);

        idColumn.setRowCellFactory(device -> new MFXTableRowCell<>(RoomType::getId));
        numColumn.setRowCellFactory(device -> new MFXTableRowCell<>(RoomType::getName));
        priceColumn.setRowCellFactory(device -> new MFXTableRowCell<>(RoomType::getPricePerHour));
        actionColumn.setRowCellFactory(room -> {
            AtomicInteger id = new AtomicInteger();
            MFXTableRowCell cell = new MFXTableRowCell<>(_room -> {
                id.set(((RoomType) _room).getId());
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
                            RoomType selection = roomTypesService.getRoomTypeById(String.valueOf(id.get()));
                            showUpdateRoomDialog(selection);
                        } catch (Exception ioException) {
                            ioException.printStackTrace();
                        }
                    }
            );

            deleteBtn.setOnAction(e->{
                try {
                    RoomType selection = roomTypesService.getRoomTypeById(String.valueOf(id.get()));
                    deleteRoom(e,selection);
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }
            });
            cell.setAlignment(Pos.TOP_CENTER);
            cell.setGraphic(hBox);
            return cell;
        });



        idxColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.1));
        idColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.1));
        priceColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.3));
        actionColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.3));

        numColumn.prefWidthProperty().bind(roomsTable.widthProperty().multiply(0.2));


        roomsTable.getTableColumns().addAll(idxColumn, idColumn, numColumn,priceColumn, actionColumn);
    }




    @Override
    public void reload() {
        this.roomTypes = roomTypesService.getRoomTypes();
        roomsTable.setItems(FXCollections.observableArrayList(roomTypes));
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
