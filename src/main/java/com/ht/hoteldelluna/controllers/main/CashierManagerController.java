package com.ht.hoteldelluna.controllers.main;

import com.ht.hoteldelluna.backend.services.InvoicesService;
import com.ht.hoteldelluna.models.Invoice;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CashierManagerController implements Initializable {

    private List<Invoice> invoices;

    private final InvoicesService invoicesService = new InvoicesService();

    public CashierManagerController(Stage stage) {
        invoices = invoicesService.getInvoices();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
