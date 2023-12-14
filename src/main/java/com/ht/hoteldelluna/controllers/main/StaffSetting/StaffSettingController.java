package com.ht.hoteldelluna.controllers.main.StaffSetting;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.UsersService;
import com.ht.hoteldelluna.controllers.Reloadable;
import com.ht.hoteldelluna.delegate.NewEntityDelegate;
import com.ht.hoteldelluna.delegate.UpdateEntityDelegate;
import com.ht.hoteldelluna.enums.UserRole;
import com.ht.hoteldelluna.models.User;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;

import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import javafx.scene.control.*;

import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StaffSettingController implements Initializable, Reloadable, NewEntityDelegate, UpdateEntityDelegate {
    @FXML
    private MFXComboBox<UserRole> filterRoleSelection;
    @FXML
    private MFXButton btnNewCreate;
    @FXML
    private MFXTableView<User> staffsTable;
    private List<User> users;
    private final UsersService usersService = new UsersService();
    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;
    private final Stage stage;
    public StaffSettingController(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupPaginated();
        setupDialog();
        setupFilter();
    }

    private void setupFilter() {
        filterRoleSelection.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            List<User> nextUsers = users.stream().filter(user -> user.getRole() == newValue).toList();
            staffsTable.setItems(FXCollections.observableArrayList(nextUsers));
        });
    }
    private void deleteUser(ActionEvent event, User user) throws IOException {
        if (user == null) {
            return;
        }
        if (user.getRole() == UserRole.ADMIN) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Không thể người này");
            alert.setHeaderText(null);
            alert.setContentText("Không thể xoá quản trị viên");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xóa nhân viên");
        alert.setHeaderText(null);
        alert.setContentText("Bạn chắc chắn muốn Xóa nhân viên: " + user.getFullName());
        ButtonType buttonTypeCancel = new ButtonType("Huỷ bỏ", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonTypeCancel, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            usersService.deleteUser(String.valueOf(user.getId()));
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
        MFXTableColumn<User> idxColumn = new MFXTableColumn<>("STT", false);
        idxColumn.setRowCellFactory(user ->  new MFXTableRowCell<>(_user -> users.indexOf(_user) + 1));

        MFXTableColumn<User> nameColumn = new MFXTableColumn<>("Họ tên", false,
                Comparator.comparing(User::getFullName));
        MFXTableColumn<User> usernameColumn = new MFXTableColumn<>("Tài khoản", false,
                Comparator.comparing(User::getUsername));
        MFXTableColumn<User> passwordColumn = new MFXTableColumn<>("Mật khẩu", false,
                Comparator.comparing(User::getPassword));
        MFXTableColumn<User> roleColumn = new MFXTableColumn<>("Role", false,
                Comparator.comparing(User::getRole));

        MFXTableColumn<User> actionColumn = new MFXTableColumn<>("Thao tác", false);
        nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(User::getFullName));
        usernameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(User::getUsername));
        passwordColumn.setRowCellFactory(device -> new MFXTableRowCell<>(User::getPassword));
        roleColumn.setRowCellFactory(device -> new MFXTableRowCell<>(User::getRole));

        actionColumn.setRowCellFactory(user -> {
            AtomicInteger id = new AtomicInteger();
            MFXTableRowCell cell = new MFXTableRowCell<>(_user -> {
                id.set(((User) _user).getId());
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
            editBtn.setOnAction(e -> {
                    try {
                        User selection = usersService.getUserById(String.valueOf(id.get()));
                        showUpdateRoomDialog(selection);
                    } catch (Exception ioException) {
                        ioException.printStackTrace();
                    }
                }
            );

            deleteBtn.setOnAction(e->{
                try {
                    User selection = usersService.getUserById(String.valueOf(id.get()));
                    deleteUser(e, selection);
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }
            });
            cell.setAlignment(Pos.TOP_CENTER);
            cell.setGraphic(hBox);
            return cell;
        });

        idxColumn.prefWidthProperty().bind(staffsTable.widthProperty().multiply(0.08));
        nameColumn.prefWidthProperty().bind(staffsTable.widthProperty().multiply(0.21));
        usernameColumn.prefWidthProperty().bind(staffsTable.widthProperty().multiply(0.21));
        passwordColumn.prefWidthProperty().bind(staffsTable.widthProperty().multiply(0.19));
        roleColumn.prefWidthProperty().bind(staffsTable.widthProperty().multiply(0.19));
        actionColumn.prefWidthProperty().bind(staffsTable.widthProperty().multiply(0.13));


        staffsTable.getTableColumns().addAll(idxColumn, nameColumn, usernameColumn, passwordColumn, roleColumn, actionColumn);
    }



    private void prepareFilterData() {
        filterRoleSelection.setItems(FXCollections.observableArrayList(List.of(
                UserRole.ADMIN, UserRole.STAFF
        )));
    }

    @Override
    public void reload() {
        this.users = usersService.getUsers();
        staffsTable.setItems(FXCollections.observableArrayList(users));
        clearFilter();
        prepareFilterData();
    }
    public void clearFilter(ActionEvent actionEvent) {
        clearFilter();
    }

    private void clearFilter() {
        filterRoleSelection.clearSelection();
        staffsTable.setItems(FXCollections.observableArrayList(users));
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
    public void showNewRoomDialog() {
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/StaffSetting/NewStaff.fxml"));
        NewStaff controller = new NewStaff(this);
        loader.setControllerFactory(r -> controller);
        dialogContent.setHeaderText("Tạo nhân viên mới");
        dialogContent.setContent(null);
        dialogContent.setContentText(null);
        try {
            dialogContent.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.showDialog();
    }

    public void showUpdateRoomDialog(User selection) {
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/StaffSetting/UpdateStaff.fxml"));
        UpdateStaff controller = new UpdateStaff(selection, this);
        loader.setControllerFactory(r -> controller);
        dialogContent.setHeaderText("Sửa thông tin nhân viên");
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
