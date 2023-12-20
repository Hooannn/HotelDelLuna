package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InvoicesService {
    java.sql.Connection dbConnection = Connection.shared.getConnection();

    public List<Invoice> getInvoicesByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT invoices.*, rooms.name AS room_name, rooms.status AS room_status, room_types.name AS room_type " +
                "FROM invoices " +
                "JOIN rooms ON invoices.room = rooms.id " +
                "JOIN room_types ON rooms.type = room_types.id " +
                "WHERE STR_TO_DATE(invoices.checkInTime, '%Y-%m-%dT%H:%i:%s') >= STR_TO_DATE(?, '%Y-%m-%dT%H:%i:%s') " +
                "AND STR_TO_DATE(invoices.checkInTime, '%Y-%m-%dT%H:%i:%s') <= STR_TO_DATE(?, '%Y-%m-%dT%H:%i:%s') " +
                "ORDER BY invoices.checkOutTime DESC";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            preparedStatement.setString(1, startTime.toString());
            preparedStatement.setString(2, endTime.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Invoice invoice = parseInvoiceWithRoomTypeResultSet(resultSet);
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }
    public List<Invoice> getInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT invoices.*, rooms.name AS room_name, rooms.status AS room_status " +
                "FROM invoices " +
                "LEFT JOIN rooms ON invoices.room = rooms.id " +
                "ORDER BY invoices.checkOutTime DESC";
        try (Statement statement = dbConnection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Invoice invoice = parseInvoiceResultSet(resultSet);
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public Invoice getInvoiceById(String invoiceId) {
        Invoice invoice = null;
        String query = "SELECT invoices.*, rooms.name AS room_name, rooms.status AS room_status " +
                "FROM invoices " +
                "JOIN rooms ON invoices.room = rooms.id " +
                "WHERE invoices.id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, invoiceId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    invoice = parseInvoiceResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoice;
    }

    public Invoice geInvoiceDetailsById(String invoiceId) {
        Invoice invoice = null;
        String query = "SELECT invoices.*, rooms.name AS room_name, rooms.status, num AS floors, room_types.name AS room_type, pricePerHour, users.fullName as created_by_name " +
                "FROM invoices " +
                "LEFT JOIN users ON users.id = invoices.createdBy " +
                "LEFT JOIN rooms ON invoices.room = rooms.id " +
                "LEFT JOIN floors ON rooms.floor = floors.id " +
                "LEFT JOIN room_types ON rooms.type = room_types.id " +
                "WHERE invoices.id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, invoiceId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    invoice = parseDetailedInvoiceResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoice;
    }

    public boolean addInvoice(Invoice invoice, String roomId, String createdBy) {
        if (createdBy == null) {
            return addInvoice(invoice, roomId);
        }
        String query = "INSERT INTO invoices (checkInTime, checkOutTime, total, customerName, room, createdBy) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, invoice.getCheckInTime());
            preparedStatement.setString(2, invoice.getCheckOutTime());
            preparedStatement.setDouble(3, invoice.getTotal());
            preparedStatement.setString(4, invoice.getCustomerName());
            preparedStatement.setString(5, roomId);
            preparedStatement.setString(6, createdBy);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addInvoice(Invoice invoice, String roomId) {
        String query = "INSERT INTO invoices (checkInTime, checkOutTime, total, customerName, room) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, invoice.getCheckInTime());
            preparedStatement.setString(2, invoice.getCheckOutTime());
            preparedStatement.setDouble(3, invoice.getTotal());
            preparedStatement.setString(4, invoice.getCustomerName());
            preparedStatement.setString(5, roomId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double calculateTotalPrice(double hours, double pricePerHour) {
        if (hours < 0) return 0;
        if (hours >= 24) {
            return hours * pricePerHour * 90/100;
        }
        return hours * pricePerHour;
    }

    private Invoice parseInvoiceResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String checkInTime = resultSet.getString("checkInTime");
        String checkOutTime = resultSet.getString("checkOutTime");
        double total = resultSet.getDouble("total");
        String customerName = resultSet.getString("customerName");

        Room room = null;
        if (resultSet.getObject("room") != null) {
            room = new Room(resultSet.getInt("room"), resultSet.getString("room_name"), RoomStatus.valueOf(resultSet.getString("room_status")));
        }

        return new Invoice(id, checkInTime, checkOutTime, total, customerName, room);
    }

    private Invoice parseDetailedInvoiceResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String checkInTime = resultSet.getString("checkInTime");
        String checkOutTime = resultSet.getString("checkOutTime");
        String createdByName = resultSet.getString("created_by_name");
        int createdById = resultSet.getInt("createdBy");
        double total = resultSet.getDouble("total");
        String customerName = resultSet.getString("customerName");

        Floor floor = null;
        RoomType type = null;
        Room room = null;
        if (resultSet.getObject("room") != null) {
            type = new RoomType(resultSet.getString("room_type"), resultSet.getDouble("pricePerHour"));
            floor = new Floor(0, resultSet.getInt("floors"));
            room = new Room(resultSet.getInt("room"),
                    resultSet.getString("room_name"),
                    type,
                    floor,
                    RoomStatus.valueOf(resultSet.getString("status"))
            );
        }
        User createdBy = new User(createdById, createdByName);
        return new Invoice(id, checkInTime, checkOutTime, total, customerName, room, createdBy);
    }

    private Invoice parseInvoiceWithRoomTypeResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String checkInTime = resultSet.getString("checkInTime");
        String checkOutTime = resultSet.getString("checkOutTime");
        double total = resultSet.getDouble("total");
        String customerName = resultSet.getString("customerName");

        RoomType type = new RoomType(resultSet.getString("room_type"));
        Room room = new Room(resultSet.getInt("room"),
                resultSet.getString("room_name"),
                type,
                null,
                RoomStatus.valueOf(resultSet.getString("room_status"))
        );

        return new Invoice(id, checkInTime, checkOutTime, total, customerName, room);
    }
}
