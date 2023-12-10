package com.ht.hoteldelluna.controllers.main.RoomTypeSetting;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.controllers.Reloadable;
import com.ht.hoteldelluna.controllers.main.FloorSetting.UpdateFloor;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomTypeSettingController implements Reloadable, Initializable {
    @FXML
    private MFXPaginatedTableView<RoomType> table;

    ObservableList<RoomType> sampleDocuments;

    private List<RoomType> roomTypes;
    private final RoomTypesService roomTypesService = new RoomTypesService();
    public RoomTypeSettingController () {
        this.roomTypes = roomTypesService.getRoomTypes(); // Change getRooms() to getFloors()
    }

    private void refresh_data() {
        this.roomTypes = roomTypesService.getRoomTypes(); // Change getRooms() to getFloors()
    }
    @FXML
    private void fetchDocuments() {
        this.sampleDocuments = FXCollections.observableList(roomTypes);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupPaginated();
    }


    @Override
    public void reload() {

    }

    public void setupPaginated() {
        MFXTableColumn<RoomType> idColumn = new MFXTableColumn<>("ID", false, Comparator.comparing(RoomType::getId));
        MFXTableColumn<RoomType> numColumn = new MFXTableColumn<>("Name", false,
                Comparator.comparing(RoomType::getName));
        MFXTableColumn<RoomType> priceColumn = new MFXTableColumn<>("Price", false,
                Comparator.comparing(RoomType::getPricePerHour));
        MFXTableColumn<RoomType> actionColumn = new MFXTableColumn<>("Action", true
                );
        actionColumn.setMinWidth(200);
        idColumn.setRowCellFactory(roomType -> new MFXTableRowCell<>(RoomType::getId));
        numColumn.setRowCellFactory(roomType -> new MFXTableRowCell<>(RoomType::getName));
        actionColumn.setRowCellFactory(roomType -> {

            AtomicInteger id = new AtomicInteger();
            MFXTableRowCell cell = new MFXTableRowCell<>(_device -> {
                id.set(((RoomType) _device).getId());
                return "";
            });

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            MFXButton editBtn = new MFXButton("Sửa");
            MFXButton deleteBtn = new MFXButton("Xóa");
            hBox.getChildren().addAll(editBtn,deleteBtn);
            editBtn.setOnAction(e->{
                try {
                    System.out.println("ID: "+id.get());
                    RoomType selection = roomTypesService.getRoomTypeById(String.valueOf(id.get()));
                    updateRoomType(e,selection);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            deleteBtn.setOnAction(e -> {
                System.out.println("ID: " + id.get());
                RoomType selection = roomTypesService.getRoomTypeById(String.valueOf(id.get()));
                try {
                    deleteRoomType(e, selection);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            cell.setGraphic(hBox);
            return cell;
        });
        table.getTableColumns().addAll(idColumn, numColumn,priceColumn, actionColumn);
        fetchDocuments();
        table.setItems(sampleDocuments);
    }
    @FXML
    private void updateRoomType(ActionEvent event, RoomType selection) throws IOException {
        if (selection == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/FloorSetting/updateFloor.fxml"));
        UpdateRoomType updateRoomType = new UpdateRoomType(selection);
        loader.setControllerFactory(r -> updateRoomType);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Sửa thông tin kiểu phòng");
        stage.setScene(scene);
        stage.showAndWait();
    }
    private void deleteRoomType(ActionEvent event, RoomType roomType) throws IOException {
        if (roomType == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xóa Tầng");
        alert.setHeaderText(null);
        alert.setContentText("Bạn chắc chắn muốn xóa Tầng : "+ roomType.getName());
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonTypeCancel, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            roomTypesService.deleteRoomType(String.valueOf(roomType.getId()));
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
    private void repaginate() {
        refresh_data() ;
        fetchDocuments();
        table.setItems(sampleDocuments);
    }
    @FXML
    public void newRoomType (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomTypeSetting/newRoomType.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Thêm kiểu phòng");
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
}
