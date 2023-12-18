package com.ht.hoteldelluna.controllers.main;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.Tab;
import com.ht.hoteldelluna.backend.AppState;
import com.ht.hoteldelluna.controllers.Reloadable;
import com.ht.hoteldelluna.controllers.auth.AuthController;
import com.ht.hoteldelluna.controllers.main.FloorSetting.FloorSettingController;
import com.ht.hoteldelluna.controllers.main.RoomManager.RoomManagerController;
import com.ht.hoteldelluna.controllers.main.RoomTypeSetting.RoomTypeSettingController;
import com.ht.hoteldelluna.controllers.main.StaffSetting.StaffSettingController;
import com.ht.hoteldelluna.controllers.main.roomSetting.RoomSettingController;
import com.ht.hoteldelluna.enums.UserRole;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final Stage stage;
    private final AppState appState = AppState.shared;
    public ToggleButton roomManagerToggle;
    public ToggleButton dashboardToggle;
    public ToggleButton cashierManagerToggle;
    public ToggleButton roomSettingToggle;
    public ToggleButton roomTypeSettingToggle;
    public ToggleButton staffSettingToggle;
    public ToggleButton floorSettingToggle;

    @FXML
    private Label adminSettingLabel;
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
        List<Tab> tabs = new ArrayList<>();
        ToggleButton[] toggles = { roomManagerToggle, dashboardToggle, cashierManagerToggle, roomSettingToggle, roomTypeSettingToggle, staffSettingToggle, floorSettingToggle };
        for (ToggleButton toggle : toggles) {
            toggle.setToggleGroup(toggleGroup);
        }

        FXMLLoader rmloader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomManager/RoomManager.fxml"));
        rmloader.setControllerFactory(c -> new RoomManagerController(this.stage));
        tabs.add(new Tab(roomManagerToggle, rmloader));

        FXMLLoader dbloader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/Dashboard.fxml"));
        dbloader.setControllerFactory(c -> new DashboardController(this.stage));
        tabs.add(new Tab(dashboardToggle, dbloader));

        FXMLLoader csloader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/CashierManager.fxml"));
        csloader.setControllerFactory(c -> new CashierManagerController(this.stage));
        tabs.add(new Tab(cashierManagerToggle, csloader));

        FXMLLoader rsloader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomSetting.fxml"));
        rsloader.setControllerFactory(c -> new RoomSettingController(this.stage));
        tabs.add(new Tab(roomSettingToggle, rsloader));

        FXMLLoader rtsloader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomTypeSetting.fxml"));
        rtsloader.setControllerFactory(c -> new RoomTypeSettingController(this.stage));
        tabs.add(new Tab(roomTypeSettingToggle, rtsloader));

        FXMLLoader ssloader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/StaffSetting/StaffSetting.fxml"));
        ssloader.setControllerFactory(c -> new StaffSettingController(this.stage));
        tabs.add(new Tab(staffSettingToggle, ssloader));

        FXMLLoader fsloader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/FloorSetting.fxml"));
        fsloader.setControllerFactory(c -> new FloorSettingController(this.stage));
        tabs.add(new Tab(floorSettingToggle, fsloader));

        for (Tab tab : tabs) {
            try {
                tab.getLoader().load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton toggleButton = (ToggleButton) newValue;
                Tab tab = tabs.stream().filter(t -> t.getToggle().equals(toggleButton)).findFirst().orElse(null);
                if (tab != null) {
                    contentPane.getChildren().setAll((Node) tab.getLoader().getRoot());
                    Reloadable controller = tab.getLoader().getController();
                    controller.reload();
                }
            } else {
                ToggleButton toggleButton = (ToggleButton) oldValue;
                toggleGroup.selectToggle(toggleButton);
            }
        });

        if (appState.getAuthUser().getRole() == UserRole.ADMIN) {
            adminNavBar.setVisible(true);
            adminSettingLabel.setVisible(true);
        } else {
            adminNavBar.setVisible(false);
            adminSettingLabel.setVisible(false);
        }

        toggleGroup.selectToggle(roomManagerToggle);
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
