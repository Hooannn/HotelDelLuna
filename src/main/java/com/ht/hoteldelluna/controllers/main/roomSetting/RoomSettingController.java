package com.ht.hoteldelluna.controllers.main.roomSetting;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.controllers.Reloadable;
import com.ht.hoteldelluna.models.Invoice;
import com.ht.hoteldelluna.models.Room;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;
import io.github.palexdev.materialfx.controls.MFXButton;

public class RoomSettingController implements Initializable, Reloadable {
    @FXML
    private MFXComboBox roomPopularity;
    @FXML
    private MFXComboBox roomSeller;
    @FXML
    private MFXPaginatedTableView<Room> roomTable;
    @FXML
    private MFXButton btnNewCreate;
    ObservableList<Room> sampleDocuments;
    @FXML MFXButton editButton;
    @FXML MFXButton deleteButton;
    private List<Room> rooms ;
    private final RoomsService roomsService = new RoomsService();

    public RoomSettingController() {
        this.rooms = roomsService.getRooms();
    }
    private void refresh_data() {
        this.rooms = roomsService.getRooms();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        setupPaginated();

        roomPopularity.getItems().addAll("từ cao đến thấp", "từ thấp đến cao");
    }
    @FXML
    public void checkForm(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/newRoomForm.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root,600, 600);
        stage.setTitle("Tạo phòng mới");
        stage.setScene(scene);
        stage.showAndWait();
        try {
            if (!stage.isShowing()) {
                repaginate();
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }

    }
    //update data from table. When i click on one line in record of data in table, it will show data in form
    @FXML

    private void fetchDocuments() {
    this.sampleDocuments = FXCollections.observableList(rooms);
}
    @FXML
    private void updateRoom(ActionEvent event,Room selection) throws IOException {
        if (selection == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomManager/updateRoomForm.fxml"));
        UpdateRoomForm updateRoomForm = new UpdateRoomForm(selection);
        loader.setControllerFactory(r -> updateRoomForm);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root,600, 600);
        stage.setTitle("Sửa thông tin phòng");
        stage.setScene(scene);
        stage.showAndWait();

        try {
            if (!stage.isShowing()) {
                repaginate();
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    public void showTypeEmptyNotification() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Type Empty");
        alert.setHeaderText(null);
        alert.setContentText("Please choose type.");
        alert.showAndWait();
    }
    private void deleteRoom(ActionEvent event,Room room) throws IOException {
        if (room == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xóa Phòng");
        alert.setHeaderText(null);
        alert.setContentText("Bạn chắc chắn muốn xóa phòng : "+ room.getName());
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonTypeCancel, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            roomsService.deleteRoom(String.valueOf(room.getId()));
            alert.close();
            repaginate();
        }
        if (alert.getResult().getText().equals("Cancel")) {
            alert.close();
        }

        try {
            if (!alert.isShowing()) {
                repaginate();
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    // Update room
//    public String toString() {
//        return "Room{" +
//                "_id=" + _id +
//                ", name='" + name + '\'' +
//                ", type=" + type +
//                ", floor=" + floor +
//                ", overnightPrice=" + overnightPrice +
//                ", status=" + status +
//                '}';
//    }
    public void setupPaginated() {
        MFXTableColumn<Room> idColumn = new MFXTableColumn<>("ID", false, Comparator.comparing(Room::getId));
        MFXTableColumn<Room> nameColumn = new MFXTableColumn<>("Name", false,
                Comparator.comparing(Room::getName));
        MFXTableColumn<Room> typeColumn = new MFXTableColumn<>("Type", false,
                Comparator.comparing(room -> room.getType().getName()));
        MFXTableColumn<Room> floorColumn = new MFXTableColumn<>("Floor", false,
                Comparator.comparing(room -> room.getFloor().getNum()));
        MFXTableColumn<Room> statusColumn = new MFXTableColumn<>("Trạng thái", false,
                Comparator.comparing(Room::getStatus));
        MFXTableColumn<Room> actionColumn = new MFXTableColumn<>("Action", true,
                Comparator.comparing(Room::getId));
        actionColumn.setMinWidth(300);
        idColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Room::getId));
        nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Room::getName));
        typeColumn.setRowCellFactory(device -> new MFXTableRowCell<>(room -> room.getType().getName()));
        floorColumn.setRowCellFactory(device -> new MFXTableRowCell<>(room -> room.getFloor().getNum()));
        statusColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Room::getStatus));
        actionColumn.setRowCellFactory(device -> {
            MFXTableRowCell rc =new MFXTableRowCell<>(Room::getId);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            MFXButton editBtn = new MFXButton(String.valueOf(device.getId()));
            MFXButton deleteBtn = new MFXButton("Delete");
            hBox.getChildren().addAll(editBtn,deleteBtn);
            device.getId();

            editBtn.setOnAction(e->{
                try {
                    System.out.println("ID: "+device.getId());
                    Room selection = roomsService.getRoomById(String.valueOf(device.getId()));
                    updateRoom(e,selection);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                    }
            );

            deleteBtn.setOnAction(e->{
                try {
                    System.out.println("ID: "+device.getId());
                    Room selection = roomsService.getRoomById(String.valueOf(device.getId()));
                    deleteRoom(e,selection);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            rc.setGraphic(hBox);
            return rc;
                });


        roomTable.getTableColumns().addAll(idColumn, nameColumn, typeColumn, floorColumn, statusColumn,actionColumn);
        fetchDocuments();
        roomTable.setItems(sampleDocuments);
    }
    private void repaginate() {
        refresh_data() ;
        fetchDocuments();
        roomTable.setItems(sampleDocuments);
    }

    @Override
    public void reload() {

    }
}
