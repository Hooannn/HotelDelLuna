package com.ht.hoteldelluna.controllers.main;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private PieChart popularRoomTypePieChart;
    @FXML
    private BarChart revenueBarChart;
    @FXML
    private MFXComboBox<String> timeSelection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTimeSelection();
        setupRevenueChart();
        setupPopularRoomTypeChart();
    }

    public DashboardController(Stage stage) {}

    private void setupTimeSelection() {
        List<String> initialValues = Arrays.asList("Hôm nay", "Tuần này", "Tháng này", "Năm này");
        timeSelection.setItems(FXCollections.observableList(initialValues));
        timeSelection.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Value changed " + newValue);
        });
        timeSelection.selectFirst();
    }

    private void setupRevenueChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Category 1", 20));
        series.getData().add(new XYChart.Data<>("Category 2", 40));
        series.getData().add(new XYChart.Data<>("Category 3", 15));
        series.getData().add(new XYChart.Data<>("Category 4", 30));
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data<>("Category 1", 2));
        series2.getData().add(new XYChart.Data<>("Category 2", 12));
        series2.getData().add(new XYChart.Data<>("Category 3", 5));
        series2.getData().add(new XYChart.Data<>("Category 4", 1));
        revenueBarChart.getData().addAll(series, series2);
    }

    private void setupPopularRoomTypeChart() {
        PieChart.Data slice1 = new PieChart.Data("Type 1", 30);
        PieChart.Data slice2 = new PieChart.Data("Type 2", 20);
        PieChart.Data slice3 = new PieChart.Data("Type 3", 25);
        PieChart.Data slice4 = new PieChart.Data("Type 4", 15);

        // Add the data to the chart
        popularRoomTypePieChart.getData().addAll(slice1, slice2, slice3, slice4);
    }
}
