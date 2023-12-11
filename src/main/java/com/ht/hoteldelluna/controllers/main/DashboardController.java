package com.ht.hoteldelluna.controllers.main;

import com.ht.hoteldelluna.backend.services.InvoicesService;
import com.ht.hoteldelluna.controllers.Reloadable;
import com.ht.hoteldelluna.models.Invoice;
import com.ht.hoteldelluna.utils.Helper;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable, Reloadable {
    public MFXFontIcon revenueIcon;
    public Label revenueLabel;
    public MFXFontIcon averageRentTimeIcon;
    public Label averageRentTimeLabel;
    public Label totalRentTimeLabel;
    public MFXFontIcon totalRentTimeIcon;
    public MFXFontIcon totalRentCountIcon;
    public Label totalRentCountLabel;
    public Pane totalRentCountIconPane;
    public Pane totalRentTimeIconPane;
    public Pane averageRentTimeIconPane;
    public Pane revenueIconPane;
    @FXML
    private PieChart popularRoomTypePieChart;
    @FXML
    private BarChart revenueBarChart;
    @FXML
    private MFXComboBox<String> timeSelection;
    private final InvoicesService invoicesService = new InvoicesService();

    private List<Invoice> currentInvoices = null;
    private List<Invoice> prevInvoices = null;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTimeSelection();
    }

    public DashboardController(Stage stage) {}

    private void setupDashboard(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime prevStartTime, LocalDateTime prevEndTime, String timeframe) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                currentInvoices = invoicesService.getInvoicesByDateRange(startTime, endTime);
                prevInvoices = invoicesService.getInvoicesByDateRange(prevStartTime, prevEndTime);
                return null;
            }
        };
        task.setOnSucceeded(taskFinishEvent -> {
            setupRevenueCard();
            setupAverageRentTimeCard();
            setupTotalRentCountCard();
            setupTotalRentTimeCard();
            setupRevenueChart(timeframe);
            setupPopularRoomTypeChart();
        });
        new Thread(task).start();
    }


    private void setupRevenueCard() {
        double currentRevenue = calculateTotal(currentInvoices);
        double prevRevenue = calculateTotal(prevInvoices);
        revenueLabel.setText(Helper.formatCurrency(currentRevenue));
        if (prevRevenue >= currentRevenue) {
            revenueIcon.setDescription("fas-arrow-down");
            revenueIcon.setColor(Color.rgb(186, 22, 22));
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(249, 206, 206), new CornerRadii(8), null);
            Background background = new Background(backgroundFill);
            revenueIconPane.setBackground(background);
        } else {
            revenueIcon.setDescription("fas-arrow-up");
            revenueIcon.setColor(Color.rgb(89, 169, 89));
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(232, 239, 229), new CornerRadii(8), null);
            Background background = new Background(backgroundFill);
            revenueIconPane.setBackground(background);
        }
    }

    private void setupAverageRentTimeCard() {
        Duration currentAverageRentTimeDuration = calculateAverageRentTime(currentInvoices);
        Duration prevAverageRentTimeDuration = calculateAverageRentTime(prevInvoices);
        long seconds = currentAverageRentTimeDuration.getSeconds();
        double hours = (double) seconds / 3600;
        averageRentTimeLabel.setText(String.format("%.1f", hours) + "h");
        if (prevAverageRentTimeDuration.compareTo(currentAverageRentTimeDuration) >= 0) {
            averageRentTimeIcon.setDescription("fas-arrow-down");
            averageRentTimeIcon.setColor(Color.rgb(186, 22, 22));
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(249, 206, 206), new CornerRadii(8), null);
            Background background = new Background(backgroundFill);
            averageRentTimeIconPane.setBackground(background);
        } else {
            averageRentTimeIcon.setDescription("fas-arrow-up");
            averageRentTimeIcon.setColor(Color.rgb(89, 169, 89));
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(232, 239, 229), new CornerRadii(8), null);
            Background background = new Background(backgroundFill);
            averageRentTimeIconPane.setBackground(background);
        }
    }

    private void setupTotalRentTimeCard() {
        Duration currentTotalRentTimeDuration = calculateTotalRentTime(currentInvoices);
        Duration prevTotalRentTimeDuration = calculateTotalRentTime(prevInvoices);
        long seconds = currentTotalRentTimeDuration.getSeconds();
        double hours = (double) seconds / 3600;
        totalRentTimeLabel.setText(String.format("%.1f", hours) + "h");
        if (prevTotalRentTimeDuration.compareTo(currentTotalRentTimeDuration) >= 0) {
            totalRentTimeIcon.setDescription("fas-arrow-down");
            totalRentTimeIcon.setColor(Color.rgb(186, 22, 22));
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(249, 206, 206), new CornerRadii(8), null);
            Background background = new Background(backgroundFill);
            totalRentTimeIconPane.setBackground(background);
        } else {
            totalRentTimeIcon.setDescription("fas-arrow-up");
            totalRentTimeIcon.setColor(Color.rgb(89, 169, 89));
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(232, 239, 229), new CornerRadii(8), null);
            Background background = new Background(backgroundFill);
            totalRentTimeIconPane.setBackground(background);
        }
    }

    private void setupTotalRentCountCard() {
        int currentTotalRentTimeCount = calculateTotalRentTimeCount(currentInvoices);
        int prevTotalRentTimeCount = calculateTotalRentTimeCount(prevInvoices);
        totalRentCountLabel.setText(String.valueOf(currentTotalRentTimeCount));
        if (prevTotalRentTimeCount >= currentTotalRentTimeCount) {
            totalRentCountIcon.setDescription("fas-arrow-down");
            totalRentCountIcon.setColor(Color.rgb(186, 22, 22));
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(249, 206, 206), new CornerRadii(8), null);
            Background background = new Background(backgroundFill);
            totalRentCountIconPane.setBackground(background);
        } else {
            totalRentCountIcon.setDescription("fas-arrow-up");
            totalRentCountIcon.setColor(Color.rgb(89, 169, 89));
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(232, 239, 229), new CornerRadii(8), null);
            Background background = new Background(backgroundFill);
            totalRentCountIconPane.setBackground(background);
        }
    }

    private Duration calculateAverageRentTime(List<Invoice> invoices) {
        if (invoices.isEmpty()) {
            return Duration.ZERO;
        }

        Duration totalDuration = Duration.ZERO;

        for (Invoice invoice : invoices) {
            LocalDateTime checkInTime = parseDateTime(invoice.getCheckInTime());
            LocalDateTime checkOutTime = parseDateTime(invoice.getCheckOutTime());

            Duration rentDuration = Duration.between(checkInTime, checkOutTime);
            totalDuration = totalDuration.plus(rentDuration);
        }

        return totalDuration.dividedBy(invoices.size());
    }

    private Duration calculateTotalRentTime(List<Invoice> invoices) {
        Duration totalDuration = Duration.ZERO;

        for (Invoice invoice : invoices) {
            LocalDateTime checkInTime = parseDateTime(invoice.getCheckInTime());
            LocalDateTime checkOutTime = parseDateTime(invoice.getCheckOutTime());

            Duration rentDuration = Duration.between(checkInTime, checkOutTime);
            totalDuration = totalDuration.plus(rentDuration);
        }

        return totalDuration;
    }

    private int calculateTotalRentTimeCount(List<Invoice> invoices) {
        return invoices.size();
    }

    private double calculateTotal(List<Invoice> invoices) {
        double total = 0;
        for (Invoice invoice : invoices) {
            total += invoice.getTotal();
        }
        return total;
    }

    private static LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private void setupTimeSelection() {
        List<String> initialValues = Arrays.asList("Hôm nay", "Tuần này", "Tháng này", "Năm này");
        timeSelection.setItems(FXCollections.observableList(initialValues));
        timeSelection.valueProperty().addListener((observable, oldValue, newValue) -> {
            LocalDateTime prevStartTime = null;
            LocalDateTime prevEndTime = null;
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            switch (newValue) {
                case "Hôm nay" -> {
                    startTime = Helper.getStartOfDay();
                    endTime = Helper.getEndOfDay();
                    prevStartTime = Helper.getStartOfPreviousDay();
                    prevEndTime = Helper.getEndOfPreviousDay();
                }
                case "Tuần này" -> {
                    startTime = Helper.getStartOfWeek();
                    endTime = Helper.getEndOfWeek();
                    prevStartTime = Helper.getStartOfPreviousWeek();
                    prevEndTime = Helper.getEndOfPreviousWeek();
                }
                case "Tháng này" -> {
                    startTime = Helper.getStartOfMonth();
                    endTime = Helper.getEndOfMonth();
                    prevStartTime = Helper.getStartOfPreviousMonth();
                    prevEndTime = Helper.getEndOfPreviousMonth();
                }
                default -> {
                    startTime = Helper.getStartOfYear();
                    endTime = Helper.getEndOfYear();
                    prevStartTime = Helper.getStartOfPreviousYear();
                    prevEndTime = Helper.getEndOfPreviousYear();
                }
            }

            setupDashboard(startTime, endTime, prevStartTime, prevEndTime, newValue);
        });
    }

    private void setupRevenueChart(String timeframe) {
        revenueBarChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.setName("Doanh thu (đ)");
        switch (timeframe) {
            case "Hôm nay" -> setupTodayChart(series);
            case "Tuần này" -> setupThisWeekChart(series);
            case "Tháng này" -> setupThisMonthChart(series);
            case "Năm này" -> setupThisYearChart(series);
            default -> {

            }
        }
        revenueBarChart.getData().setAll(series);
    }

    private void setupTodayChart(XYChart.Series<String, Number> series) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH");

        for (int i = 0; i < 24; i++) {
            LocalDateTime hourStartTime = now.withHour(i).withMinute(0).withSecond(0);
            String label = hourStartTime.format(hourFormatter);
            double totalRevenue = calculateTotalRevenueForHour(currentInvoices, hourStartTime, hourStartTime.plusHours(1));
            series.getData().add(new XYChart.Data<>(label, totalRevenue));
        }
    }

    private void setupThisWeekChart(XYChart.Series<String, Number> series) {
        LocalDate startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE");

        for (int i = 0; i < 7; i++) {
            LocalDate currentDay = startOfWeek.plusDays(i);
            String label = currentDay.format(dayFormatter);
            double totalRevenue = calculateTotalRevenueForDay(currentInvoices, LocalDateTime.of(currentDay, LocalTime.MIN), LocalDateTime.of(currentDay, LocalTime.MAX));
            series.getData().add(new XYChart.Data<>(label, totalRevenue));
        }
    }

    private void setupThisMonthChart(XYChart.Series<String, Number> series) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d");

        int lastDayOfMonth = startOfMonth.lengthOfMonth();
        for (int i = 0; i < lastDayOfMonth; i++) {
            LocalDate currentDay = startOfMonth.plusDays(i);
            String label = currentDay.format(dayFormatter);
            double totalRevenue = calculateTotalRevenueForDay(currentInvoices, LocalDateTime.of(currentDay, LocalTime.MIN), LocalDateTime.of(currentDay, LocalTime.MAX));
            series.getData().add(new XYChart.Data<>(label, totalRevenue));
        }
    }

    private void setupThisYearChart(XYChart.Series<String, Number> series) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");

        for (int i = 1; i <= 12; i++) {
            LocalDate startOfMonth = LocalDate.now().withMonth(i).withDayOfMonth(1);
            String label = startOfMonth.format(monthFormatter);
            double totalRevenue = calculateTotalRevenueForMonth(
                    currentInvoices,
                    LocalDateTime.of(startOfMonth, LocalTime.MIDNIGHT).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN),
                    LocalDateTime.of(startOfMonth, LocalTime.MIDNIGHT).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX)
            );
            series.getData().add(new XYChart.Data<>(label, totalRevenue));
        }
    }

    private double calculateTotalRevenueForHour(List<Invoice> invoices, LocalDateTime startTime, LocalDateTime endTime) {
        double totalRevenue = 0;

        for (Invoice invoice : invoices) {
            LocalDateTime invoiceCheckInTime = LocalDateTime.parse(invoice.getCheckInTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime invoiceCheckOutTime = LocalDateTime.parse(invoice.getCheckOutTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            if (invoiceCheckInTime.isBefore(endTime) && invoiceCheckInTime.isAfter(startTime)) {
                totalRevenue += invoice.getTotal();
            }
        }

        return totalRevenue;
    }

    private double calculateTotalRevenueForDay(List<Invoice> invoices, LocalDateTime startDate, LocalDateTime endDate) {
        double totalRevenue = 0;

        for (Invoice invoice : invoices) {
            LocalDateTime invoiceCheckInTime = LocalDateTime.parse(invoice.getCheckInTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime invoiceCheckOutTime = LocalDateTime.parse(invoice.getCheckOutTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            if (invoiceCheckInTime.isAfter(startDate) && invoiceCheckInTime.isBefore(endDate)) {
                totalRevenue += invoice.getTotal();
            }
        }

        return totalRevenue;
    }

    private double calculateTotalRevenueForMonth(List<Invoice> invoices, LocalDateTime startMonth, LocalDateTime endMonth) {
        double totalRevenue = 0;

        for (Invoice invoice : invoices) {
            LocalDateTime invoiceCheckInTime = LocalDateTime.parse(invoice.getCheckInTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime invoiceCheckOutTime = LocalDateTime.parse(invoice.getCheckOutTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            if (invoiceCheckInTime.isAfter(startMonth) && invoiceCheckInTime.isBefore(endMonth)) {
                totalRevenue += invoice.getTotal();
            }
        }

        return totalRevenue;
    }

    private void setupPopularRoomTypeChart() {
        popularRoomTypePieChart.getData().clear();
        HashMap<String, Integer> counter = new HashMap<String, Integer>();


        for (Invoice currentInvoice : currentInvoices) {
            String roomType = currentInvoice.getRoom().getType().getName();
            if (counter.get(roomType) == null) {
                counter.put(roomType, 1);
            } else {
                int newValue = counter.get(roomType) + 1;
                counter.put(roomType, newValue);
            }
        }

        for (String s : counter.keySet()) {
            popularRoomTypePieChart.getData().add(new PieChart.Data(s, counter.get(s)));
        }
    }

    @Override
    public void reload() {
        String prevSelected = timeSelection.getSelectedItem();
        if (prevSelected == null) {
            timeSelection.selectFirst();
        } else {
            timeSelection.clearSelection();
            timeSelection.selectItem(prevSelected);
        }
    }
}
