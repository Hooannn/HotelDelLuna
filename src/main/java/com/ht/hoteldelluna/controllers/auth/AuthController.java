package com.ht.hoteldelluna.controllers.auth;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.AppState;
import com.ht.hoteldelluna.backend.services.UsersService;
import com.ht.hoteldelluna.controllers.main.MainController;
import com.ht.hoteldelluna.models.User;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.input.KeyCode;

public class AuthController implements Initializable {
    private final Stage stage;
    private final AppState appState = AppState.shared;
    public MFXTextField usernameTextField;
    public MFXPasswordField passwordTextField;
    private double xOffset;
    private double yOffset;
    private final ToggleGroup toggleGroup;

    @FXML
    private HBox windowHeader;

    @FXML
    private MFXFontIcon closeIcon;

    @FXML
    private MFXFontIcon minimizeIcon;

    @FXML
    private MFXFontIcon alwaysOnTopIcon;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private StackPane contentPane;

    @FXML
    private MFXButton authLoginBtn;

    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;
    private  User user = null;

    @FXML
    private void login() {
        UsersService usersService = new UsersService();

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showErrorDialog("Đăng nhập thất bại", "Vui lòng điển đủ tài khoản và mật khẩu");
            return;
        }

        try {
            user = usersService.getUserByUsername(username);
        } catch (Exception e) {
            showErrorDialog("Đăng nhập thất bại", e.getMessage());
            return;
        }

        if (password.equals(user.getPassword())) {
            showSuccessDialog("Đăng nhập thành công", "Đăng nhập thành công. Hãy ấn xác nhận để chuyển đến trang chủ");
            switchToMainStage();
        } else {
            showErrorDialog("Đăng nhập thất bại", "Sai mật khẩu");
        }
    }

    public AuthController(Stage stage) {
        this.stage = stage;
        this.toggleGroup = new ToggleGroup();
        ToggleButtonsUtil.addAlwaysOneSelectedSupport(toggleGroup);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Platform.exit());
        minimizeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> ((Stage) rootPane.getScene().getWindow()).setIconified(true));
        alwaysOnTopIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            boolean newVal = !stage.isAlwaysOnTop();
            alwaysOnTopIcon.pseudoClassStateChanged(PseudoClass.getPseudoClass("always-on-top"), newVal);
            stage.setAlwaysOnTop(newVal);
        });

        windowHeader.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        windowHeader.setOnMouseDragged(event -> {
            stage.setOpacity(0.7);
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
        windowHeader.setOnMouseReleased(event -> {
            stage.setOpacity(1.0);
        });

        setupDialog();
    }

    private void showErrorDialog(String headerText, String contentText) {
        MFXFontIcon warnIcon = new MFXFontIcon("fas-circle-xmark", 18);
        dialogContent.setHeaderIcon(warnIcon);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContentText(contentText);
        dialogContent.getStyleClass().removeIf(
                s -> s.equals("mfx-info-dialog") || s.equals("mfx-warn-dialog")
        );
        dialogContent.getStyleClass().add("mfx-error-dialog");
        dialog.showDialog();
    }

    private void showSuccessDialog(String headerText, String contentText) {
        MFXFontIcon warnIcon = new MFXFontIcon("fas-circle-info", 18);
        dialogContent.setHeaderIcon(warnIcon);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContentText(contentText);
        dialogContent.getStyleClass().removeIf(
                s -> s.equals("mfx-warn-dialog") || s.equals("mfx-error-dialog")
        );
        dialogContent.getStyleClass().add("mfx-info-dialog");
        dialog.showAndWait();
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) login();
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

            dialogContent.addActions(
                    Map.entry(new MFXButton("Xác nhận"), event -> dialog.close())
            );
            dialogContent.setMaxSize(stage.getMaxWidth(), stage.getMaxHeight());
        });
    }

    private void switchToMainStage() {
        try {
            appState.setAuthUser(user);
            authLoginBtn.getScene().getWindow().hide();

            Stage mainStage = new Stage();
            FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/Main.fxml"));
            loader.setControllerFactory(c -> new MainController(mainStage));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
            scene.setFill(Color.TRANSPARENT);
            mainStage.initStyle(StageStyle.TRANSPARENT);
            mainStage.setScene(scene);
            mainStage.setTitle("Hotel Del Luna");
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
