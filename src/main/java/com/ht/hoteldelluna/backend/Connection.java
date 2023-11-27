package com.ht.hoteldelluna.backend;

import javax.swing.plaf.nimbus.State;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class Connection {
    public static Connection shared = new Connection();
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/default"; // Replace this to your local properties
    static final String USER = "hoanthui"; // Replace this to your local properties
    static final String PASSWORD = "123456"; // Replace this to your local properties
    private java.sql.Connection connection;
    public java.sql.Connection getConnection() {
        return connection;
    }
    private Connection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

            System.out.println("Connected to MySQL successfully!");
            createTablesIfNotExists();
            System.out.println("Created the tables!");
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
                    "room INT," +
                    "FOREIGN KEY (room) REFERENCES rooms(id)" +
                    ")";
            statement.executeUpdate(createInvoiceTableSQL);

            // Create Reservation table
            String createReservationTableSQL = "CREATE TABLE IF NOT EXISTS reservations (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "checkInTime VARCHAR(255)," +
                    "checkOutTime VARCHAR(255)," +
                    "customerName VARCHAR(255)," +
                    "note VARCHAR(255)," +
                    "customerCount INT," +
                    "status ENUM('OPENING', 'CLOSED')," +
                    "room INT," +
                    "FOREIGN KEY (room) REFERENCES rooms(id)" +
                    ")";
            statement.executeUpdate(createReservationTableSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
