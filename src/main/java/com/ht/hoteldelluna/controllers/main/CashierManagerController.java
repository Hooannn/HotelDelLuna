
package com.ht.hoteldelluna.controllers.main;

import com.ht.hoteldelluna.backend.services.InvoicesService;
import com.ht.hoteldelluna.models.Invoice;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.SortState;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class CashierManagerController implements Initializable {
    @FXML
    private MFXComboBox<SortState> checkInTimeSelection;

    @FXML
    private MFXComboBox<SortState> checkOutTimeSelection;

    @FXML
    private MFXComboBox<SortState> totalSelection;

    @FXML
    private MFXPaginatedTableView<Invoice> invoicesTable;

    @FXML
    private HBox listViewMode;

    @FXML
    private HBox tableViewMode;

    private MFXTableColumn<Invoice> checkInTimeColumn;
    private MFXTableColumn<Invoice> checkOutTimeColumn;
    private MFXTableColumn<Invoice> totalColumn;

    private List<Invoice> invoices;

    private final InvoicesService invoicesService = new InvoicesService();
    private final Stage stage;

    public static final StringConverter<SortState> converter = new StringConverter<SortState>() {
        @Override
        public String toString(SortState state) {
            if (state != null) {
                switch (state) {
                    case ASCENDING:
                        return "Tăng dần";
                    case DESCENDING:
                        return "Giảm dần";
                    case UNSORTED:
                        return "Mặc định";
                    default:
                        return state.toString().toLowerCase();
                }
            }
            return null;
        }

        @Override
        public SortState fromString(String string) {
            return SortState.valueOf(string.toUpperCase());
        }
    };

    public CashierManagerController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        invoices = invoicesService.getInvoices();

        setupTable();
        setupSelections();
        invoicesTable.autosizeColumnsOnInitialization();

        When.onChanged(invoicesTable.currentPageProperty())
                .then((oldValue, newValue) -> invoicesTable.autosizeColumns())
                .listen();
    }

    private void setupSelections() {
        checkInTimeSelection.setItems(FXCollections.observableArrayList(SortState.values()));
        checkOutTimeSelection.setItems(FXCollections.observableArrayList(SortState.values()));
        totalSelection.setItems(FXCollections.observableArrayList(SortState.values()));

        checkInTimeSelection.setConverter(converter);
        checkOutTimeSelection.setConverter(converter);
        totalSelection.setConverter(converter);

        checkInTimeSelection.valueProperty().addListener((observable, oldValue, newValue) -> checkInTimeColumn.setSortState(newValue));
        checkOutTimeSelection.valueProperty().addListener((observable, oldValue, newValue) -> checkOutTimeColumn.setSortState(newValue));
        totalSelection.valueProperty().addListener((observable, oldValue, newValue) -> totalColumn.setSortState(newValue));
    }

    private void setupTable() {
        MFXTableColumn<Invoice> idxColumn = new MFXTableColumn<>("STT", false);
        idxColumn.setRowCellFactory(invoice -> new MFXTableRowCell<>(_invoice -> invoices.indexOf(_invoice) + 1));

        MFXTableColumn<Invoice> nameColumn = new MFXTableColumn<>("Tên Khách Hàng", false, Comparator.comparing(Invoice::getCustomerName));
        MFXTableColumn<Invoice> roomIdColumn = new MFXTableColumn<>("Mã Phòng", false, Comparator.comparing(Invoice::getRoomId));
        checkInTimeColumn = new MFXTableColumn<>("Thời Gian Thuê Phòng", false, Comparator.comparing(Invoice::getCheckInTime));
        checkOutTimeColumn = new MFXTableColumn<>("Thời Gian Trả Phòng", false, Comparator.comparing(Invoice::getCheckOutTime));
        totalColumn = new MFXTableColumn<>("Tổng Tiền", false, Comparator.comparing(Invoice::getTotal));

        nameColumn.setRowCellFactory(invoice -> new MFXTableRowCell<>(Invoice::getCustomerName));
        roomIdColumn.setRowCellFactory(invoice -> new MFXTableRowCell<>(Invoice::getRoomName));
        checkInTimeColumn.setRowCellFactory(invoice -> new MFXTableRowCell<>(Invoice::getFormattedCheckInTime));
        checkOutTimeColumn.setRowCellFactory(invoice -> new MFXTableRowCell<>(Invoice::getFormattedCheckOutTime));
        totalColumn.setRowCellFactory(invoice -> new MFXTableRowCell<>(Invoice::getTotal));

        idxColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.10));
        nameColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.18));
        roomIdColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.15));
        checkInTimeColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.21));
        checkOutTimeColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.21));
        totalColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.15));

        invoicesTable.getTableColumns().addAll(idxColumn, nameColumn, roomIdColumn, checkInTimeColumn, checkOutTimeColumn, totalColumn);
        invoicesTable.setRowsPerPage(10);
        invoicesTable.setPagesToShow(5);
        invoicesTable.setItems(FXCollections.observableArrayList(invoices));
    }
}

