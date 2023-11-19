package com.ht.hoteldelluna.controllers.auth;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.AppState;
import com.ht.hoteldelluna.backend.services.UsersService;
import com.ht.hoteldelluna.controllers.main.MainController;
import com.ht.hoteldelluna.enums.UserRole;
import com.ht.hoteldelluna.models.User;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.input.KeyCode;

public class AuthController implements Initializable {
    private final Stage stage;

    private final AppState appState = AppState.shared;
    public MFXTextField usernameTextField;
    public MFXPasswordField passwordTextField;
    public Label lblMessage;
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

    @FXML
    private void login() {
        UsersService usersService = new UsersService();

        // Lấy các giá trị user nhập vào
        // cứ lấy giá trị fake test trước
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        User user = null;
        try {
            user = usersService.getUserByUsername(username);
        } catch (Exception e) {
            lblMessage.setText(e.getMessage());
            lblMessage.setTextFill(Color.RED);
            return ;
        }
        // Kiểm tra username và password có hợp lệ không
        if (password.equals(user.getPassword())) {
            // Đăng nhập thành công, hiển thị thông báo
            lblMessage.setText("Đăng nhập thành công!");
            lblMessage.setTextFill(Color.GREEN);
            try {
                // Cập nhật user hiện tại
                appState.setAuthUser(user);
                authLoginBtn.getScene().getWindow().hide();

                // Logic vào trang chủ khi đăng nhập thành công
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
        } else {
            // Đăng nhập thất bại, hiển thị thông báo
            lblMessage.setText("Đăng nhập thất bại!");
            lblMessage.setTextFill(Color.RED);}

        // Gọi userService để lấy user đó từ databse
        // vi dụ: User user = userService.loginUser(username, password);

        // Kiểm tra
        // Nếu sai pass thì (tạm thời): System.out "sai pass"
        // Thưc tiễn: mở 1 hộp thoại thông báo pass bị sai

        // Cập nhật user hiện tại

//        try {
//            // Cập nhật user hiện tại
//            appState.setAuthUser(new User("Khai Hoan", "hoanthui", "123456", UserRole.ADMIN));
//            authLoginBtn.getScene().getWindow().hide();
//
//            // Logic vào trang chủ khi đăng nhập thành công
//            Stage mainStage = new Stage();
//            FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/Main.fxml"));
//            loader.setControllerFactory(c -> new MainController(mainStage));
//            Parent root = loader.load();
//            Scene scene = new Scene(root);
//            MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
//            scene.setFill(Color.TRANSPARENT);
//            mainStage.initStyle(StageStyle.TRANSPARENT);
//            mainStage.setScene(scene);
//            mainStage.setTitle("Hotel Del Luna");
//            mainStage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public AuthController(Stage stage) {
        this.stage = stage;
        this.toggleGroup = new ToggleGroup();
        ToggleButtonsUtil.addAlwaysOneSelectedSupport(toggleGroup);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblMessage.setText("");
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

        initializeLoader();
    }

    private void initializeLoader() {
    }

    private ToggleButton createToggle(String icon, String text) {
        return createToggle(icon, text, 0);
    }

    private ToggleButton createToggle(String icon, String text, double rotate) {
        MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text, wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(toggleGroup);
        if (rotate != 0)
            wrapper.getIcon().setRotate(rotate);
        return toggleNode;
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) login();
    }
}
