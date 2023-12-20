package com.ht.hoteldelluna.backend;

import com.ht.hoteldelluna.AppConfig;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connection {
    public static Connection shared = new Connection();
    private java.sql.Connection connection;
    public java.sql.Connection getConnection() {
        return connection;
    }
    private Connection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(AppConfig.getJdbcUrl(), AppConfig.getUser(), AppConfig.getPassword());

            createTablesIfNotExists();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTablesIfNotExists() {
        try (Statement statement = connection.createStatement()) {
            // Create User table
            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "fullName VARCHAR(255)," +
                    "username VARCHAR(255) UNIQUE," +
                    "password VARCHAR(255)," +
                    "role ENUM('ADMIN', 'STAFF', 'MANAGER', 'GUEST')" +
                    ")";
            statement.executeUpdate(createUserTableSQL);

            // Create RoomType table
            String createRoomTypeTableSQL = "CREATE TABLE IF NOT EXISTS room_types (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) UNIQUE," +
                    "pricePerHour DECIMAL(10, 2)" +
                    ")";
            statement.executeUpdate(createRoomTypeTableSQL);

            // Create Floor table
            String createFloorTableSQL = "CREATE TABLE IF NOT EXISTS floors (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "num INT UNIQUE" +
                    ")";
            statement.executeUpdate(createFloorTableSQL);

            // Create Room table
            String createRoomTableSQL = "CREATE TABLE IF NOT EXISTS rooms (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) UNIQUE," +
                    "type INT," +
                    "floor INT," +
                    "status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE')," +
                    "FOREIGN KEY (type) REFERENCES room_types(id)," +
                    "FOREIGN KEY (floor) REFERENCES floors(id)" +
                    ")";
            statement.executeUpdate(createRoomTableSQL);

            // Create Invoice table
            String createInvoiceTableSQL = "CREATE TABLE IF NOT EXISTS invoices (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "checkInTime VARCHAR(255)," +
                    "checkOutTime VARCHAR(255)," +
                    "total DECIMAL(10, 2)," +
                    "customerName VARCHAR(255)," +
                    "createdBy INT," +
                    "room INT," +
                    "FOREIGN KEY (room) REFERENCES rooms(id) ON DELETE SET NULL," +
                    "FOREIGN KEY (createdBy) REFERENCES users(id) ON DELETE SET NULL" +
                    ")";
            statement.executeUpdate(createInvoiceTableSQL);

            // Create Reservation table
            String createReservationTableSQL = "CREATE TABLE IF NOT EXISTS reservations (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "checkInTime VARCHAR(255)," +
                    "checkOutTime VARCHAR(255)," +
                    "customerName VARCHAR(255)," +
                    "createdBy INT," +
                    "note VARCHAR(255)," +
                    "customerCount INT," +
                    "status ENUM('OPENING', 'CLOSED')," +
                    "room INT," +
                    "FOREIGN KEY (room) REFERENCES rooms(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (createdBy) REFERENCES users(id) ON DELETE SET NULL" +
                    ")";
            statement.executeUpdate(createReservationTableSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}