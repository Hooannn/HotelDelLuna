package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Invoice;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class InvoicesService {
    java.sql.Connection dbConnection = Connection.shared.getConnection();
    public List<Invoice> getInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT invoices.*, rooms.name AS room_name, rooms.status AS room_status " +
                "FROM invoices " +
                "JOIN rooms ON invoices.room = rooms.id " +
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
        String query = "SELECT invoices.*, rooms.name AS room_name, rooms.status, num AS floors, room_types.name AS room_type, pricePerHour " +
                "FROM invoices " +
                "JOIN rooms ON invoices.room = rooms.id " +
                "JOIN floors ON rooms.floor = floors.id " +
                "JOIN room_types ON rooms.type = room_types.id " +
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

        Room room = new Room(resultSet.getInt("room"), resultSet.getString("room_name"), RoomStatus.valueOf(resultSet.getString("room_status")));

        return new Invoice(id, checkInTime, checkOutTime, total, customerName, room);
    }

    private Invoice parseDetailedInvoiceResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String checkInTime = resultSet.getString("checkInTime");
        String checkOutTime = resultSet.getString("checkOutTime");
        double total = resultSet.getDouble("total");
        String customerName = resultSet.getString("customerName");

        Floor floor = new Floor(0, resultSet.getInt("floors"));
        RoomType type = new RoomType(resultSet.getString("room_type"), resultSet.getDouble("pricePerHour"));
        Room room = new Room(resultSet.getInt("room"),
                resultSet.getString("room_name"),
                type,
                floor,
                RoomStatus.valueOf(resultSet.getString("status"))
        );

        return new Invoice(id, checkInTime, checkOutTime, total, customerName, room);
    }
}
