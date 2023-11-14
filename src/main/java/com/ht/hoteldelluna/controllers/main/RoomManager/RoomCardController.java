package com.ht.hoteldelluna.controllers.main.RoomManager;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.InvoicesService;
import com.ht.hoteldelluna.backend.services.ReservationsService;
import com.ht.hoteldelluna.backend.services.RoomsService;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Invoice;
import com.ht.hoteldelluna.models.Reservation;
import com.ht.hoteldelluna.models.Room;
import com.mongodb.client.result.InsertOneResult;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXContextMenu;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.ResourceBundle;

public class RoomCardController implements Initializable {
    @FXML
    private Label checkOutDateTimeLabel;
    @FXML
    private Label timeCounterLabel;
    @FXML
    private Label totalCounterLabel;
    @FXML
    private Label checkInDateTimeLabel;
    @FXML
    private HBox rightHeaderContainer;
    @FXML
    private AnchorPane root;
    @FXML
    private Label roomNameLabel;
    @FXML
    private Label roomTypeLabel;
    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;
    private RoomCardControllerDelegate delegate;
    private final Stage stage;
    private final Room room;
    private final Reservation reservation;
    private final ReservationsService reservationsService = new ReservationsService();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roomNameLabel.setText(room.getName());
        roomTypeLabel.setText(room.getType().getName());
        setupContextButton();
        setupDialog();
    }

    public RoomCardController(Stage stage, Room room, Reservation reservation, RoomCardControllerDelegate delegate) {
        this.stage = stage;
        this.room = room;
        this.reservation = reservation;
        this.delegate = delegate;
    }
    private void setupContextButton() {
        MFXContextMenu contextMenu = new MFXContextMenu(root);
        MFXIconWrapper iconWrapper = new MFXIconWrapper("fas-ellipsis-vertical", 16, 16);
        iconWrapper.setStyle("-fx-text-fill: #737373; -fx-cursor: hand;");
        rightHeaderContainer.getChildren().add(iconWrapper);

        MFXContextMenuItem checkInItem = new MFXContextMenuItem("Check-in");
        MFXContextMenuItem checkOutItem = new MFXContextMenuItem("Check-out");
        MFXContextMenuItem cleanRoomItem = new MFXContextMenuItem("Dọn phòng");
        MFXContextMenuItem cancelRoomItem = new MFXContextMenuItem("Huỷ phòng");

        checkInItem.setOnAction(event -> showCheckInDialog());
        checkOutItem.setOnAction(event -> showCheckOutDialog());
        cleanRoomItem.setOnAction(event -> cleanRoom());
        cancelRoomItem.setOnAction(event -> cancelReservation());
        contextMenu.getItems().addAll(checkInItem, checkOutItem, cleanRoomItem, cancelRoomItem);

        iconWrapper.setOnMouseClicked(event -> {
            double screenX = iconWrapper.localToScreen(iconWrapper.getLayoutBounds().getMinX(), 0).getX() + 20;
            double screenY = iconWrapper.localToScreen(0, iconWrapper.getLayoutBounds().getMinY()).getY();
            contextMenu.show(iconWrapper, screenX, screenY);
        });

        switch (room.getStatus()) {
            case MAINTENANCE -> {
                checkInItem.setDisable(true);
                checkOutItem.setDisable(true);
                cleanRoomItem.setDisable(false);
                cancelRoomItem.setDisable(true);
            }
            case OCCUPIED -> {
                checkInItem.setDisable(true);
                checkOutItem.setDisable(false);
                cleanRoomItem.setDisable(true);
                cancelRoomItem.setDisable(false);
            }
            default -> {
                checkInItem.setDisable(false);
                checkOutItem.setDisable(true);
                cleanRoomItem.setDisable(true);
                cancelRoomItem.setDisable(true);
            }
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

            dialogContent.setMaxSize(stage.getMaxWidth(), stage.getMaxHeight());
        });
    }

    private void cancelReservation() {
        if (reservation == null) return;
        reservationsService.closeReservation(reservation.getId().toString(), reservation.getRoom().getId().toString());
        delegate.onCancelled(room);
    }

    private void cleanRoom() {
        RoomsService roomsService = new RoomsService();
        if (reservation == null) {
            roomsService.updateRoomStatus(room.getId().toString(), RoomStatus.AVAILABLE);
        } else {
            reservationsService.closeReservation(reservation.getId().toString(), reservation.getRoom().getId().toString());
        }
        delegate.onCleaned(room);
    }

    private void showCheckInDialog() {
        MFXButton updateButton = new MFXButton("Cập nhật");
        MFXButton exitButton = new MFXButton("Thoát");
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomManager/CheckInForm.fxml"));
        loader.setControllerFactory(c -> new CheckInFormController(room, reservation));
        dialogContent.setHeaderText("Check-in");
        dialogContent.clearActions();
        dialogContent.addActions(
                Map.entry(updateButton, event -> {
                    CheckInFormController checkInFormController = loader.getController();
                    Reservation reservation = checkInFormController.getReservation();
                    InsertOneResult result = reservationsService.addReservation(reservation, room.getId().toString());
                    dialog.close();
                    delegate.onCheckedIn(room);
                }),
                Map.entry(exitButton, event -> dialog.close())
        );

        try {
            dialogContent.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.showDialog();
    }

    private void showCheckOutDialog() {
        MFXButton updateButton = new MFXButton("Cập nhật");
        MFXButton checkOutButton = new MFXButton("Thanh toán");
        MFXButton exitButton = new MFXButton("Thoát");
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/RoomManager/CheckInForm.fxml"));
        loader.setControllerFactory(c -> new CheckInFormController(room, reservation));
        dialogContent.setHeaderText("Check-out");
        dialogContent.clearActions();
        dialogContent.addActions(
                Map.entry(checkOutButton, event -> {
                    if (reservation == null) {
                        dialog.close();
                        return;
                    }
                    InvoicesService invoicesService = new InvoicesService();
                    CheckInFormController checkInFormController = loader.getController();
                    Reservation reservation = checkInFormController.getReservation();
                    reservationsService.checkout(reservation.getId().toString(), reservation.getRoom().getId().toString());
                    LocalDateTime checkInTime = LocalDateTime.parse(reservation.getCheckInTime());
                    LocalDateTime checkOutTime = LocalDateTime.parse(reservation.getCheckOutTime());
                    Duration duration = Duration.between(checkInTime, checkOutTime);
                    long seconds = duration.getSeconds();
                    double total = seconds * 100; //TODO: Calculate the true total
                    invoicesService.addInvoice(
                            new Invoice(
                                    reservation.getCheckInTime(),
                                    reservation.getCheckOutTime(),
                                    total,
                                    reservation.getCustomerName()),
                            room.getId().toString()
                    );
                    dialog.close();
                    delegate.onCheckedOut(room);
                }),
                Map.entry(updateButton, event -> {
                    CheckInFormController checkInFormController = loader.getController();
                    String updatedNote = checkInFormController.getNote();
                    if (!reservation.getNote().equals(updatedNote)) {
                        reservationsService.updateNote(reservation.getId().toString(), updatedNote);
                        delegate.onUpdated(room);
                    }
                    dialog.close();
                }),
                Map.entry(exitButton, event -> dialog.close())
        );

        try {
            dialogContent.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.showDialog();
    }

    @Override
    public String toString() {
        return "RoomCardController{" +
                "stage=" + stage +
                ", room=" + room +
                ", reservation=" + reservation +
                '}';
    }
}
