package com.ht.hoteldelluna.controllers.main;

import com.ht.hoteldelluna.backend.services.InvoicesService;
import com.ht.hoteldelluna.models.Invoice;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class InvoiceDetailController implements Initializable {
    private final Invoice invoice;
    private final int invoiceId;
    private final InvoicesService invoicesService = new InvoicesService();


    private double totalTime = 0;

    @FXML
    private Label checkInTimeLabel;

    @FXML
    private Label createdByNameLabel;

    @FXML
    private Label checkOutTimeLabel;

    @FXML
    private Label createdAtLabel;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label floorNumLabel;

    @FXML
    private Label invoiceIdLabel;

    @FXML
    private Label pricePerHourLabel;

    @FXML
    private Label roomIdLabel;

    @FXML
    private Label roomTypeLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Label totalTimeLabel;

    @FXML
    private Label discountLabel;

    @FXML
    private GridPane invoiceDetailPane;

    @FXML
    private HBox customerNameBox;

    @FXML
    private Label changableLabel;

    @FXML
    private VBox notFoundPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceIdLabel.setText(String.format("%04d", this.invoiceId));

        if (this.invoice != null) {
            createdAtLabel.setText(invoice.getFormattedCheckOutTime().substring(0, 10));
            customerNameLabel.setText(invoice.getCustomerName());
            checkInTimeLabel.setText(invoice.getFormattedCheckInTime());
            checkOutTimeLabel.setText(invoice.getFormattedCheckOutTime());

            if (invoice.getRoom() != null) {
                roomIdLabel.setText(invoice.getRoomName());
                roomTypeLabel.setText(invoice.getRoom().getType().getName());
                floorNumLabel.setText(String.format("%02d", invoice.getRoom().getFloor().getNum()));
                pricePerHourLabel.setText(invoice.getRoom().getType().getFormattedPricePerHour());
            } else {
                roomIdLabel.setText("Không còn tồn tại");
                roomTypeLabel.setText("Không còn tồn tại");
                floorNumLabel.setText("Không còn tồn tại");
                pricePerHourLabel.setText("Không còn tồn tại");
            }
            createdByNameLabel.setText(invoice.getCreatedBy().getFullName() == null ? "Không còn tồn tại" : invoice.getCreatedBy().getFullName());
            totalTimeLabel.setText(getFormattedTotalTime());
            discountLabel.setText(isDiscount() ? "10%" : "Không");
            totalLabel.setText(invoice.getFormattedTotal());
        } else {
            invoiceDetailPane.setVisible(false);
            customerNameBox.setVisible(false);
            notFoundPane.setVisible(true);
            changableLabel.setText("Trạng thái");
            createdAtLabel.setText("Không tồn tại hoặc đã bị xóa");
        }
    }

    private String getFormattedTotalTime() {
        int days = (int) (this.totalTime / 3600 / 24);
        int hours = ((int) (this.totalTime / 3600)) % 24;
        int mins = ((int) Math.ceil(this.totalTime / 60)) % 60;

        if (days >= 1) {
            if (hours < 1 && mins >= 1) {
                return String.format("%02d ngày, %02d phút", days, mins);
            } else {
                return String.format("%02d ngày, %02d giờ", days, hours);
            }
        } else {
            return String.format("%02d giờ, %02d phút", hours, mins);
        }
    }

    private boolean isDiscount() {
        return this.totalTime >= (24 * 3600);
    }

    private double getTotalTime() {
        LocalDateTime checkInTime = LocalDateTime.parse(invoice.getCheckInTime());
        LocalDateTime checkOutTime = LocalDateTime.parse(invoice.getCheckOutTime());
        Duration duration = Duration.between(checkInTime, checkOutTime);
        return (double) duration.toSeconds();
    }

    public InvoiceDetailController(int invoiceId) {
        this.invoiceId = invoiceId;
        this.invoice = invoicesService.geInvoiceDetailsById(String.valueOf(invoiceId));
        if (this.invoice != null) {
            this.totalTime = getTotalTime();
        }
    }
}
