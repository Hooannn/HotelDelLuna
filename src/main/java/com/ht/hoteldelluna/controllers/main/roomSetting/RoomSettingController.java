package com.ht.hoteldelluna.controllers.main.roomSetting;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.models.Room;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;
import io.github.palexdev.materialfx.controls.MFXButton;

public class RoomSettingController implements Initializable {
    @FXML
    private MFXComboBox roomPopularity;
    @FXML
    private MFXComboBox roomSeller;
    @FXML
    private MFXPaginatedTableView<Room> roomTable;

    @FXML
    private MFXButton btnNewCreate;
    ObservableList<Room> sampleDocuments;

    private List<Room> rooms ;
    private final RoomsService roomsService = new RoomsService();

    public RoomSettingController() {
        rooms = roomsService.getRooms();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        stage.show();

    }
    private void fetchDocuments() {
    this.sampleDocuments = FXCollections.observableList(rooms);
}
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

        idColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Room::getId));
        nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Room::getName));
        typeColumn.setRowCellFactory(device -> new MFXTableRowCell<>(room -> room.getType().getName()));
        floorColumn.setRowCellFactory(device -> new MFXTableRowCell<>(room -> room.getFloor().getNum()));
        statusColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Room::getStatus));

        roomTable.getTableColumns().addAll(idColumn, nameColumn, typeColumn, floorColumn, statusColumn);
        fetchDocuments();
        roomTable.setItems(sampleDocuments);
    }

}
