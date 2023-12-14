package com.ht.hoteldelluna.controllers.main.StaffSetting;

import com.ht.hoteldelluna.backend.services.UsersService;
import com.ht.hoteldelluna.delegate.UpdateEntityDelegate;
import com.ht.hoteldelluna.enums.UserRole;
import com.ht.hoteldelluna.models.User;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateStaff implements Initializable {
    public Stage stage;
    public MFXTextField fullnameTextField;
    public MFXTextField usernameTextField;
    public MFXPasswordField passwordTextField;
    public MFXComboBox<UserRole> roleSelection;
    private User user;
    private UpdateEntityDelegate delegate;
    public UpdateStaff(User user, UpdateEntityDelegate delegate) {
        this.user = user;
        this.delegate = delegate;
    }
    public void displayUserInformation(User user) {
        fullnameTextField.setText(user.getFullName());
        usernameTextField.setText(user.getUsername());
        passwordTextField.setText(user.getPassword());
    }
    public void showAlertMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void update(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        UsersService usersService = new UsersService();
        if (!username.equals(user.getUsername()) && usersService.existsByUsername(username)) {
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

        usersService.updateUser(String.valueOf(user.getId()), username, fullnameTextField.getText(), passwordTextField.getText());
        delegate.onUpdated();
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
        delegate.onCancelled();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roleSelection.setItems(FXCollections.observableArrayList(List.of(
                UserRole.ADMIN, UserRole.STAFF
        )));
        roleSelection.setDisable(true);
        roleSelection.selectItem(UserRole.STAFF);
        displayUserInformation(user);
    }
}
