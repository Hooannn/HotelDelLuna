package com.ht.hoteldelluna.controllers.main;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.AppState;
import com.ht.hoteldelluna.controllers.auth.AuthController;
import com.ht.hoteldelluna.controllers.main.RoomManager.RoomManagerController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.ht.hoteldelluna.MFXResourcesLoader.loadURL;

public class MainController implements Initializable {
    private final Stage stage;
    private final AppState appState = AppState.shared;
    private double xOffset;
    private double yOffset;
    private final ToggleGroup toggleGroup;

    @FXML
    private Label authUserRoleLabel;

    @FXML
    private Label authUserNameLabel;

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
    private VBox staffNavBar;

    @FXML
    private VBox adminNavBar;

    @FXML
    private StackPane contentPane;

    @FXML
    private HBox signOutBtn;

    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;

    @FXML
    private void showSignOutDialog() {
        MFXFontIcon warnIcon = new MFXFontIcon("fas-circle-info", 18);
        dialogContent.setHeaderIcon(warnIcon);
        dialogContent.setHeaderText("Xác nhận đăng xuất");
        dialogContent.setContentText("Bạn có chắc muốn đăng xuất không?");
        dialogContent.getStyleClass().add("mfx-info-dialog");
        dialog.showDialog();
    }

    public MainController(Stage stage) {
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

        authUserNameLabel.setText(appState.getAuthUser().getFullName());
        authUserRoleLabel.setText(appState.getAuthUser().getRole().name());

        initializeLoader();
        setupDialog();
    }

    private void initializeLoader() {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("ROOM_MANAGER", loadURL("fxml/main/RoomManager/RoomManager.fxml"))
                .setBeanToNodeMapper(() -> createToggle("fas-house", "Sơ đồ phòng", "staff"))
                .setControllerFactory(c -> new RoomManagerController(this.stage))
                .setDefaultRoot(true).get());

        loader.addView(MFXLoaderBean.of("CASHIER_MANAGER", loadURL("fxml/main/CashierManager.fxml"))
                .setBeanToNodeMapper(() -> createToggle("fas-dollar-sign", "Thu ngân", "staff"))
                .setControllerFactory(c -> new CashierManagerController(this.stage))
                .get());

        loader.addView(MFXLoaderBean.of("ROOM_SETTING", loadURL("fxml/main/RoomSetting.fxml"))
                .setBeanToNodeMapper(() -> createToggle("fas-bed", "Thiết lập phòng", "admin"))
                .get());

        loader.addView(MFXLoaderBean.of("ROOM_TYPE_SETTING", loadURL("fxml/main/RoomTypeSetting.fxml"))
                .setBeanToNodeMapper(() -> createToggle("fas-list", "Thiết lập loại phòng", "admin"))
                .get());

        loader.addView(MFXLoaderBean.of("STAFF_SETTING", loadURL("fxml/main/StaffSetting.fxml"))
                .setBeanToNodeMapper(() -> createToggle("fas-user-tie", "Thiết lập nhân viên", "admin"))
                .get());

        loader.addView(MFXLoaderBean.of("FLOOR_SETTING", loadURL("fxml/main/FloorSetting.fxml"))
                .setBeanToNodeMapper(() -> createToggle("fas-wrench", "Thiết lập tầng", "admin"))
                .get());


        loader.setOnLoadedAction(beans -> {
            List<ToggleButton> nodes = beans.stream()
                    .map(bean -> {
                        ToggleButton toggle = (ToggleButton) bean.getBeanToNodeMapper().get();
                        toggle.setOnAction(event -> contentPane.getChildren().setAll(bean.getRoot()));
                        if (bean.isDefaultView()) {
                            contentPane.getChildren().setAll(bean.getRoot());
                            toggle.setSelected(true);
                        }
                        return toggle;
                    })
                    .toList();
            List<ToggleButton> staffNodes = nodes.stream().filter(n -> n.getUserData().equals("staff")).toList();
            List<ToggleButton> adminNodes = nodes.stream().filter(n -> n.getUserData().equals("admin")).toList();
            staffNavBar.getChildren().setAll(staffNodes);
            adminNavBar.getChildren().setAll(adminNodes);
        });
        loader.start();
    }

    private ToggleButton createToggle(String icon, String text, Object userData) {
        return createToggle(icon, text, userData, 0);
    }

    private ToggleButton createToggle(String icon, String text, Object userData, double rotate) {
        MFXIconWrapper wrapper = new MFXIconWrapper(icon, 20, 24);
        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text, wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(toggleGroup);
        toggleNode.setUserData(userData);
        if (rotate != 0)
            wrapper.getIcon().setRotate(rotate);
        return toggleNode;
    }

    private void signOut() {
        signOutBtn.getScene().getWindow().hide();

        try {
            appState.setAuthUser(null);
            Stage authStage = new Stage();
            FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/Authentication.fxml"));
            loader.setControllerFactory(c -> new AuthController(authStage));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
            scene.setFill(Color.TRANSPARENT);
            authStage.initStyle(StageStyle.TRANSPARENT);
            authStage.setScene(scene);
            authStage.setTitle("Hotel Del Luna");
            authStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    Map.entry(new MFXButton("Có"), event -> signOut()),
                    Map.entry(new MFXButton("Không"), event -> dialog.close())
            );

            dialogContent.setMaxSize(stage.getMaxWidth(), stage.getMaxHeight());
        });
    }
}
