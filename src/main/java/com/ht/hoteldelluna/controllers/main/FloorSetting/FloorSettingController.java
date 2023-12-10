package com.ht.hoteldelluna.controllers.main.FloorSetting;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.controllers.main.roomSetting.UpdateRoomForm;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.controllers.Reloadable;
import com.ht.hoteldelluna.models.Room;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class FloorSettingController implements Initializable,Reloadable {
    @FXML
    private MFXButton create;
    @FXML
    private  MFXButton edit;
    @FXML
    private MFXButton delete;
    @FXML
    private MFXPaginatedTableView<Floor> table;

    @Override
    public void reload() {
        repaginate();
    }


    ObservableList<Floor> sampleDocuments; // Change Room to Floor

    private List<Floor> floors; // Change Room to Floor
    private final FloorsService floorsService = new FloorsService(); // Change RoomsService to FloorsService

    public FloorSettingController() {
        this.floors = floorsService.getFloors(); // Change getRooms() to getFloors()
    }

    private void refresh_data() {
        this.floors = floorsService.getFloors(); // Change getRooms() to getFloors()
    }
    @FXML
    private void fetchDocuments() {
        this.sampleDocuments = FXCollections.observableList(floors);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupPaginated();
    }

    public void setupPaginated() {
        MFXTableColumn<Floor> idColumn = new MFXTableColumn<>("ID", false, Comparator.comparing(Floor::getId));
        MFXTableColumn<Floor> numColumn = new MFXTableColumn<>("Num", false,
                Comparator.comparing(Floor::getNum));
        MFXTableColumn<Floor> actionColumn = new MFXTableColumn<>("Action", true,
                null);
        actionColumn.setMinWidth(200);
        actionColumn.setTextAlignment(TextAlignment.CENTER);
        idColumn.setRowCellFactory(floor -> new MFXTableRowCell<>(Floor::getId));
        numColumn.setRowCellFactory(floor -> new MFXTableRowCell<>(Floor::getNum));
        actionColumn.setRowCellFactory(floor -> {

            AtomicInteger id = new AtomicInteger();
            MFXTableRowCell cell = new MFXTableRowCell<>(_device -> {
                id.set(((Floor) _device).getId());
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
                    Floor selection = floorsService.getFloorById(String.valueOf(id.get()));
                    updateFloor(e,selection);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            deleteBtn.setOnAction(e -> {
                System.out.println("ID: " + id.get());
                Floor selection = floorsService.getFloorById(String.valueOf(id.get()));
                try {
                    deleteFloor(e, selection);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            cell.setGraphic(hBox);
            return cell;
        });
        table.getTableColumns().addAll(idColumn, numColumn, actionColumn);
        fetchDocuments();
        table.setItems(sampleDocuments);
    }
    @FXML
    private void updateFloor(ActionEvent event,Floor selection) throws IOException {
        if (selection == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/FloorSetting/updateFloor.fxml"));
        UpdateFloor updateFloorForm = new UpdateFloor(selection);
        loader.setControllerFactory(r -> updateFloorForm);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root,600, 600);
        stage.setTitle("Sửa thông tin Tầng");
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
    private void deleteFloor(ActionEvent event, Floor floor) throws IOException {
        if (floor == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xóa Tầng");
        alert.setHeaderText(null);
        alert.setContentText("Bạn chắc chắn muốn xóa Tầng : "+ floor.getNum());
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonTypeCancel, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            floorsService.deleteFloorByID(String.valueOf(floor.getId()));
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
    private void newFloor(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(com.ht.hoteldelluna.MFXResourcesLoader.loadURL("fxml/main/FloorSetting/NewFloorForm.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root,600, 400);
        stage.setTitle("New Floor");
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