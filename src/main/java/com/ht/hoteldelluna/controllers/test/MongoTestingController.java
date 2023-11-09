package com.ht.hoteldelluna.controllers.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.ht.hoteldelluna.backend.Connection;
import org.bson.Document;

import com.ht.hoteldelluna.models.Text;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;

public class MongoTestingController implements Initializable {
    Connection dm = Connection.shared;
    ObservableList<Text> sampleDocuments;

    @FXML
    private MFXTextField textField;

    @FXML
    private MFXButton submitButton;

    @FXML
    private Label headerLabel;

    @FXML
    private MFXPaginatedTableView<Text> paginatedTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paginatedTable.autosizeColumnsOnInitialization();
        When.onChanged(paginatedTable.currentPageProperty())
                .then((oldValue, newValue) -> paginatedTable.autosizeColumns())
                .listen();

        submitButton.setOnAction(this::handleSubmitButtonClick);

        fetchDocuments();

        setupPaginated();
    }

    private void handleSubmitButtonClick(ActionEvent event) {
        String textFromTextField = textField.getText();
        //dm.addSampleDocument(textFromTextField);
        textField.clear();

        fetchDocuments();
        paginatedTable.setItems(sampleDocuments);
    }

    private void fetchDocuments() {
        this.sampleDocuments = FXCollections.observableList(new ArrayList<>());
    }

    private void setupPaginated() {
        MFXTableColumn<Text> idColumn = new MFXTableColumn<>("ID", false, Comparator.comparing(Text::getId));
        MFXTableColumn<Text> valueColumn = new MFXTableColumn<>("Value", false,
                Comparator.comparing(Text::getValue));

        idColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Text::getId));
        valueColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Text::getValue));

        paginatedTable.getTableColumns().addAll(idColumn, valueColumn);
        paginatedTable.setItems(sampleDocuments);
    }
}
