package com.ht.hoteldelluna.controllers.main.StaffSetting;

import com.ht.hoteldelluna.backend.services.FloorsService;
import com.ht.hoteldelluna.backend.services.RoomTypesService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.backend.services.UsersService;
import com.ht.hoteldelluna.delegate.NewEntityDelegate;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.enums.UserRole;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;
import com.ht.hoteldelluna.models.User;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.List;

public class NewStaff {
    public Stage stage;
    public MFXTextField fullnameTextField;
    public MFXTextField usernameTextField;
    public MFXPasswordField passwordTextField;
    public MFXComboBox<UserRole> roleSelection;
    private NewEntityDelegate delegate;
    public NewStaff(NewEntityDelegate delegate) {
        this.delegate = delegate;
    }
    @FXML
    public void initialize() {
        roleSelection.setItems(FXCollections.observableArrayList(List.of(
                UserRole.ADMIN, UserRole.STAFF
        )));
        roleSelection.setDisable(true);
        roleSelection.selectItem(UserRole.STAFF);
    }
    public void showAlertMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void submit (ActionEvent event) {
        String username = usernameTextField.getText();
        UsersService usersService = new UsersService();
        if (usersService.existsByUsername(username)) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tài khoản này đã tồn tại.");
            return;
        }
        if (username.isBlank() || username.isEmpty()) {
            showAlertMessage("Yêu cầu không hợp lệ", "Tài khoản không được bỏ trống.");
            return;
        }
        if (fullnameTextField.getText().isBlank() || fullnameTextField.getText().isEmpty()) {
            showAlertMessage("Yêu cầu không hợp lệ", "Họ tên không được bỏ trống.");
            return;
        }
        if (passwordTextField.getText().isBlank() || fullnameTextField.getText().isEmpty()) {
            showAlertMessage("Yêu cầu không hợp lệ", "Mật khẩu không được bỏ trống.");
            return;
        }
        User user = new User(fullnameTextField.getText(), username, passwordTextField.getText(), roleSelection.getSelectedItem() == null ? UserRole.STAFF : roleSelection.getSelectedItem());
        usersService.addUser(user);

        delegate.onCreated();
    }

    @FXML
    public void cancel (ActionEvent event) {
        delegate.onCancelled();
    }
}
