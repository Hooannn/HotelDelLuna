
package com.ht.hoteldelluna.controllers.main;

import com.ht.hoteldelluna.backend.services.InvoicesService;
import com.ht.hoteldelluna.models.Invoice;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class CashierManagerController implements Initializable {
    @FXML
    private MFXComboBox<String> checkInTimeSelection;

    @FXML
    private MFXComboBox<String> checkOutTimeSelection;

    @FXML
    private MFXComboBox<String> totalSelection;

    @FXML
    private MFXPaginatedTableView<Invoice> invoicesTable;

    @FXML
    private HBox listViewMode;

    @FXML
    private HBox tableViewMode;

    private List<Invoice> invoices;

    private final InvoicesService invoicesService = new InvoicesService();
    private final Stage stage;
    public CashierManagerController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        invoices = invoicesService.getInvoices();
        setupTable();
    }

    private void setupTable() {
        MFXTableColumn<Invoice> nameColumn = new MFXTableColumn<>("Tên Khách Hàng", false, Comparator.comparing(Invoice::getCustomerName));
        MFXTableColumn<Invoice> roomIdColumn = new MFXTableColumn<>("Mã Phòng", false, Comparator.comparing(Invoice::getRoomId));
        MFXTableColumn<Invoice> checkInTimeColumn = new MFXTableColumn<>("Thời Gian Vào", false, Comparator.comparing(Invoice::getCheckInTime));
        MFXTableColumn<Invoice> checkOutTimeColumn = new MFXTableColumn<>("Thời Gian Ra", false, Comparator.comparing(Invoice::getCheckOutTime));
        MFXTableColumn<Invoice> totalColumn = new MFXTableColumn<>("Tổng Tiền", false, Comparator.comparing(Invoice::getTotal));

        invoicesTable.getTableColumns().addAll(nameColumn, roomIdColumn, checkInTimeColumn, checkOutTimeColumn, totalColumn);
//        invoicesTable.setItems((ObservableList<Invoice>) invoices);
    }
}

