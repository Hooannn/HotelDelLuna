package com.ht.hoteldelluna.controllers.main.roomSetting;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.backend.Parser;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.controllers.main.RoomManager.RoomCardController;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;
import com.ht.hoteldelluna.models.Text;
import com.mongodb.client.MongoCollection;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;

public class RoomSettingController implements Initializable {
    @FXML
    private MFXComboBox roomPopularity;
    @FXML
    private MFXComboBox roomSeller;
    @FXML
    private MFXPaginatedTableView roomTable;

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
//        btnNewCreate.setOnAction(event -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/NewRoomForm.fxml"));
//                loader.setControllerFactory(c -> new NewRoomForm());
//                MFXDialog dialog = new MFXDialog(loader.load());
//                dialog.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
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
    private void setupPaginated() {
        MFXTableColumn<Room> idColumn = new MFXTableColumn<>("ID", false, Comparator.comparing(Room::getId));
        MFXTableColumn<Room> nameColumn = new MFXTableColumn<>("Name", false,
                Comparator.comparing(Room::getName));
        MFXTableColumn<Room> typeColumn = new MFXTableColumn<>("Type", false,
                Comparator.comparing(room -> room.getType().getName()));
        MFXTableColumn<Room> floorColumn = new MFXTableColumn<>("Floor", false,
                Comparator.comparing(room -> room.getFloor().getNum()));
        MFXTableColumn<Room> overnightPriceColumn = new MFXTableColumn<>("Giá qua đêm", false,
                Comparator.comparing(Room::getOvernightPrice));
        MFXTableColumn<Room> statusColumn = new MFXTableColumn<>("Trạng thái", false,
                Comparator.comparing(Room::getStatus));

        idColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Room::getId));
        nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Room::getName));
        typeColumn.setRowCellFactory(device -> new MFXTableRowCell<>(room -> room.getType().getName()));
        floorColumn.setRowCellFactory(device -> new MFXTableRowCell<>(room -> room.getFloor().getNum()));
        overnightPriceColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Room::getOvernightPrice));
        statusColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Room::getStatus));

        roomTable.getTableColumns().addAll(idColumn, nameColumn, typeColumn, floorColumn, overnightPriceColumn, statusColumn);
        fetchDocuments();
        roomTable.setItems(sampleDocuments);
    }




}
