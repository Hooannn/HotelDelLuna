package com.ht.hoteldelluna.controllers.main;

import com.ht.hoteldelluna.backend.services.InvoicesService;
import com.ht.hoteldelluna.models.Invoice;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class InvoiceDetailController implements Initializable {
    private final Invoice invoice;
    private final InvoicesService invoicesService = new InvoicesService();

    private double totalTime = 0;

    @FXML
    private Label checkInTimeLabel;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.invoice != null) {
            invoiceIdLabel.setText(String.format("%04d", invoice.getId()));
            createdAtLabel.setText(invoice.getFormattedCheckOutTime().substring(0, 10));
            customerNameLabel.setText(invoice.getCustomerName());

            roomIdLabel.setText(invoice.getRoomName());
            roomTypeLabel.setText(invoice.getRoom().getType().getName());
            floorNumLabel.setText(String.format("%02d", invoice.getRoom().getFloor().getNum()));
            checkInTimeLabel.setText(invoice.getFormattedCheckInTime());
            checkOutTimeLabel.setText(invoice.getFormattedCheckOutTime());

            pricePerHourLabel.setText(invoice.getRoom().getType().getFormattedPricePerHour());
            totalTimeLabel.setText(getFormattedTotalTime());
            discountLabel.setText(isDiscount() ? "10%" : "Không");
            totalLabel.setText(invoice.getFormattedTotal());
        } else {
            System.out.println("Cannot find invoice");
        }
    }

    private String getFormattedTotalTime(){
        return String.valueOf(totalTime);
    }

    private boolean isDiscount () {
        return this.totalTime >= (24 * 3600);
    }

    private double getTotalTime () {
        LocalDateTime checkInTime = LocalDateTime.parse(invoice.getCheckInTime());
        LocalDateTime checkOutTime = LocalDateTime.parse(invoice.getCheckOutTime());
        Duration duration = Duration.between(checkInTime, checkOutTime);
        return (double) duration.toSeconds();
    }

    public InvoiceDetailController(int invoiceId) {
        this.invoice = invoicesService.geInvoiceDetailsById(String.valueOf(invoiceId));
        if (this.invoice != null) {
            this.totalTime = getTotalTime();
        }
    }
}
