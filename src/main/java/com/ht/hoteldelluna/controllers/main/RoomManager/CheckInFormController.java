package com.ht.hoteldelluna.controllers.main.RoomManager;

import com.ht.hoteldelluna.backend.AppState;
import com.ht.hoteldelluna.backend.services.InvoicesService;
import com.ht.hoteldelluna.enums.ReservationStatus;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Reservation;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.utils.Helper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import jfxtras.scene.control.LocalDateTimeTextField;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final InvoicesService invoicesService = new InvoicesService();
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

        if (reservation != null) {
            authUserNameTextField.setText(reservation.getCreatedBy().getFullName() == null ? "Không còn tồn tại" : reservation.getCreatedBy().getFullName());
            LocalDateTime checkInTime = LocalDateTime.parse(reservation.getCheckInTime());
            LocalDateTime timeAfter = null;
            if (reservation.getCheckOutTime() != null) {
                timeAfter = LocalDateTime.parse(reservation.getCheckOutTime());
            }
            if (timeAfter == null) {
                timeAfter = LocalDateTime.now();
            }
            Duration duration = Duration.between(checkInTime, timeAfter);
            double hours = (double) duration.toSeconds() / 3600;
            double total = invoicesService.calculateTotalPrice(hours, room.getType().getPricePerHour());
            String displayTotal = Helper.formatCurrency(total);
            totalTextField.setText(displayTotal);
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

    public Reservation getReservation() throws Exception {
        validate();
        if (reservation != null) {
            reservation.setNote(noteTextArea.getText());
            reservation.setCheckOutTime(checkOutDateTimeTextField.getLocalDateTime().toString());
            return reservation;
        }
        return new Reservation(
                999,
                checkInDateTimeTextField.getLocalDateTime().toString(),
                null,
                customerNameTextField.getText(),
                Integer.parseInt(totalCustomersTextField.getText()),
                noteTextArea.getText(),
                ReservationStatus.OPENING);
    }

    private void validate() throws Exception {
        if (reservation != null) {
            LocalDateTime checkInTime = checkInDateTimeTextField.getLocalDateTime();
            LocalDateTime checkOutTime = checkOutDateTimeTextField.getLocalDateTime();
            if (checkOutTime == null) throw new Exception("Vui lòng nhập giờ check-out");
            if (checkOutTime.isBefore(checkInTime)) throw new Exception("Giờ check-out không được sớm hơn giờ check-in");
        } else {
            LocalDateTime checkInTime = checkInDateTimeTextField.getLocalDateTime();
            String totalCustomers = totalCustomersTextField.getText();
            String customerName = customerNameTextField.getText();
            if (checkInTime == null) throw new Exception("Vui lòng nhập giờ check-in");
            if (totalCustomers.isEmpty()) throw new Exception("Vui lòng nhập số lượng khách");
            if (!isNumeric(totalCustomers)) throw new Exception("Số lượng khách chỉ được nhập số");
            if (Integer.parseInt(totalCustomers) <= 0) throw new Exception("Số lượng khách phải lớn hơn 0");
            if (customerName.isEmpty()) throw new Exception("Vui lòng nhập tên khách hàng");
        }
    }

    public String getNote() {
        return noteTextArea.getText();
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
