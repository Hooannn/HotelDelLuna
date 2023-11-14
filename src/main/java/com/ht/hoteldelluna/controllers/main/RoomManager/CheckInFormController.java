package com.ht.hoteldelluna.controllers.main.RoomManager;

import com.ht.hoteldelluna.backend.AppState;
import com.ht.hoteldelluna.enums.ReservationStatus;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Reservation;
import com.ht.hoteldelluna.models.Room;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import jfxtras.scene.control.LocalDateTimeTextField;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class CheckInFormController implements Initializable {
    @FXML
    private LocalDateTimeTextField checkInDateTimeTextField;
    @FXML
    private LocalDateTimeTextField checkOutDateTimeTextField;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private MFXTextField customerNameTextField;
    @FXML
    private MFXTextField totalTextField;
    @FXML
    private MFXTextField totalCustomersTextField;
    @FXML
    private MFXTextField roomTypeTextField;
    @FXML
    private MFXTextField roomNameTextField;
    @FXML
    private MFXTextField authUserNameTextField;
    private final Room room;
    private final Reservation reservation;
    private final AppState appState = AppState.shared;
    public CheckInFormController(Room room, Reservation reservation) {
        this.room = room;
        this.reservation = reservation;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authUserNameTextField.setText(appState.getAuthUser().getFullName());
        roomNameTextField.setText(room.getName());
        roomTypeTextField.setText(room.getType().getName());

        if (room.getStatus() == RoomStatus.AVAILABLE) {
            checkOutDateTimeTextField.setDisable(true);
        } else if (room.getStatus() == RoomStatus.OCCUPIED && reservation != null) {
            checkInDateTimeTextField.setDisable(true);
            totalCustomersTextField.setDisable(true);
            customerNameTextField.setDisable(true);
            checkInDateTimeTextField.setLocalDateTime(LocalDateTime.parse(reservation.getCheckInTime()));
            totalCustomersTextField.setText(String.valueOf(reservation.getCustomerCount()));
            customerNameTextField.setText(reservation.getCustomerName());
            noteTextArea.setText(reservation.getNote());
        }
    }

    @Override
    public String toString() {
        return "CheckInFormController{" +
                "noteTextArea=" + noteTextArea.getText() +
                ", customerNameTextField=" + customerNameTextField.getText() +
                ", totalTextField=" + totalTextField.getText() +
                ", totalCustomersTextField=" + totalCustomersTextField.getText() +
                ", roomTypeTextField=" + roomTypeTextField.getText() +
                ", roomNameTextField=" + roomNameTextField.getText() +
                ", authUserNameTextField=" + authUserNameTextField.getText() +
                ", checkInDateTimeTextField=" + checkInDateTimeTextField.getLocalDateTime().toString() +
                ", checkOutDateTimeTextField=" + checkOutDateTimeTextField.getLocalDateTime().toString() +
                '}';
    }

    public Reservation getReservation() {
        if (reservation != null) {
            reservation.setNote(noteTextArea.getText());
            reservation.setCheckOutTime(checkOutDateTimeTextField.getLocalDateTime().toString());
            return reservation;
        }
        return new Reservation(
                checkInDateTimeTextField.getLocalDateTime().toString(),
                null,
                customerNameTextField.getText(),
                Integer.parseInt(totalCustomersTextField.getText()),
                noteTextArea.getText(),
                ReservationStatus.OPENING);
    }

    public String getNote() {
        return noteTextArea.getText();
    }
}
