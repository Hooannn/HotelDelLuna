
package com.ht.hoteldelluna.controllers.main;

import com.ht.hoteldelluna.MFXResourcesLoader;
import com.ht.hoteldelluna.backend.services.InvoicesService;
import com.ht.hoteldelluna.controllers.Reloadable;
import com.ht.hoteldelluna.models.Invoice;
import com.ht.hoteldelluna.utils.Helper;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.enums.SortState;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class CashierManagerController implements Initializable, Reloadable {
    @FXML
    private MFXComboBox<SortState> checkInTimeSelection;

    @FXML
    private MFXComboBox<SortState> checkOutTimeSelection;

    @FXML
    private MFXComboBox<SortState> totalSelection;

    @FXML
    private MFXTableView<Invoice> invoicesTable;

    @FXML
    private HBox listViewMode;

    @FXML
    private HBox tableViewMode;

    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;
    private MFXTableColumn<Invoice> checkInTimeColumn;
    private MFXTableColumn<Invoice> checkOutTimeColumn;
    private MFXTableColumn<Invoice> totalColumn;
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
        setupSelections();
        setupDialog();
        invoicesTable.autosizeColumnsOnInitialization();
    }

    private void setupSelections() {
        checkInTimeSelection.setItems(FXCollections.observableArrayList(SortState.values()));
        checkOutTimeSelection.setItems(FXCollections.observableArrayList(SortState.values()));
        totalSelection.setItems(FXCollections.observableArrayList(SortState.values()));

        checkInTimeSelection.setConverter(Helper.sortEnumConverter);
        checkOutTimeSelection.setConverter(Helper.sortEnumConverter);
        totalSelection.setConverter(Helper.sortEnumConverter);

        checkInTimeSelection.valueProperty().addListener((observable, oldValue, newValue) -> checkInTimeColumn.setSortState(newValue));
        checkOutTimeSelection.valueProperty().addListener((observable, oldValue, newValue) -> checkOutTimeColumn.setSortState(newValue));
        totalSelection.valueProperty().addListener((observable, oldValue, newValue) -> totalColumn.setSortState(newValue));
    }

    private void setupTable() {
        MFXTableColumn<Invoice> idxColumn = new MFXTableColumn<>("STT", false);
        idxColumn.setRowCellFactory(invoice ->  new MFXTableRowCell<>(_invoice -> invoices.indexOf(_invoice) + 1));

        MFXTableColumn<Invoice> actionColumn = new MFXTableColumn<>("Thao Tác", false);
        actionColumn.setRowCellFactory(invoice -> {
            AtomicInteger id = new AtomicInteger();
            MFXTableRowCell cell = new MFXTableRowCell<>(_invoice -> {
                id.set(((Invoice) _invoice).getId());
                return "";
            });
            MFXButton viewBtn = new MFXButton("Xem");
            viewBtn.getStyleClass().add("table-view-record-btn");
            viewBtn.setOnAction(e -> showInvoiceDetailDialog(id.get()));
            cell.setAlignment(Pos.TOP_CENTER);
            cell.setGraphic(viewBtn);
            return cell;
        });

        MFXTableColumn<Invoice> nameColumn = new MFXTableColumn<>("Tên Khách Hàng", false, Comparator.comparing(Invoice::getCustomerName));
        MFXTableColumn<Invoice> roomIdColumn = new MFXTableColumn<>("Mã Phòng", false, Comparator.comparing(Invoice::getRoomId));
        checkInTimeColumn = new MFXTableColumn<>("Thời Gian Thuê Phòng", false, Comparator.comparing(Invoice::getCheckInTime));
        checkOutTimeColumn = new MFXTableColumn<>("Thời Gian Trả Phòng", false, Comparator.comparing(Invoice::getCheckOutTime));
        totalColumn = new MFXTableColumn<>("Tổng Tiền", false, Comparator.comparing(Invoice::getTotal));

        nameColumn.setRowCellFactory(invoice -> new MFXTableRowCell<>(Invoice::getCustomerName));
        roomIdColumn.setRowCellFactory(invoice -> new MFXTableRowCell<>(Invoice::getRoomName));
        checkInTimeColumn.setRowCellFactory(invoice -> new MFXTableRowCell<>(Invoice::getFormattedCheckInTime));
        checkOutTimeColumn.setRowCellFactory(invoice -> new MFXTableRowCell<>(Invoice::getFormattedCheckOutTime));
        totalColumn.setRowCellFactory(invoice -> new MFXTableRowCell<>(Invoice::getFormattedTotal));

        idxColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.08));
        nameColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.18));
        roomIdColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.13));
        checkInTimeColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.19));
        checkOutTimeColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.19));
        totalColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.13));
        actionColumn.prefWidthProperty().bind(invoicesTable.widthProperty().multiply(0.1));

        invoicesTable.getTableColumns().addAll(idxColumn, nameColumn, roomIdColumn, checkInTimeColumn, checkOutTimeColumn, totalColumn, actionColumn);
        invoicesTable.setItems(FXCollections.observableArrayList(invoices));
    }

    private void setupDialog() {
        Platform.runLater(() -> {
            dialogContent = MFXGenericDialogBuilder.build()
                    .makeScrollable(true)
                    .setShowMinimize(false)
                    .setShowAlwaysOnTop(false)
                    .setHeaderText("Chi tiết hóa đơn")
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
            dialogContent.getStyleClass().add("mfx-info-dialog");
        });
    }

    private void showInvoiceDetailDialog(int invoiceId) {
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/main/InvoiceDetailDialog.fxml"));
        loader.setControllerFactory(c -> new InvoiceDetailController(invoiceId));
        dialogContent.setContent(null);
        dialogContent.setContentText(null);
        try {
            dialogContent.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.showDialog();
    }

    @Override
    public void reload() {
        invoices = invoicesService.getInvoices();
        invoicesTable.setItems(FXCollections.observableArrayList(invoices));
    }
}

