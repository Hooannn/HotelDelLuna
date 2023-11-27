package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.enums.ReservationStatus;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Reservation;
import com.ht.hoteldelluna.models.Room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationsService {
    java.sql.Connection dbConnection = Connection.shared.getConnection();

    public List<Reservation> getReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT reservations.*, rooms.name AS room_name, rooms.status AS room_status, rooms.status AS room_status " +
                "FROM reservations " +
                "JOIN rooms ON reservations.room = rooms.id";
        try (Statement statement = dbConnection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Reservation reservation = parseReservationResultSet(resultSet);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public List<Reservation> getOpeningReservations() {
        List<Reservation> openingReservations = new ArrayList<>();
        String query = "SELECT reservations.*, rooms.name AS room_name, rooms.status AS room_status " +
                "FROM reservations " +
                "JOIN rooms ON reservations.room = rooms.id " +
                "WHERE reservations.status = 'OPENING'";
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Reservation reservation = parseReservationResultSet(resultSet);
                openingReservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(openingReservations);
        return openingReservations;
    }

    public Reservation getReservationById(String reservationId) {
        Reservation reservation = null;
        String query = "SELECT reservations.*, rooms.name AS room_name, rooms.status AS room_status " +
                "FROM reservations " +
                "JOIN rooms ON reservations.room = rooms.id " +
                "WHERE reservations.id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, reservationId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    reservation = parseReservationResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    public Reservation getReservationByRoomId(String roomId) {
        Reservation reservation = null;
        String query = "SELECT reservations.*, rooms.name AS room_name, rooms.status AS room_status " +
                "FROM reservations " +
                "JOIN rooms ON reservations.room = rooms.id " +
                "WHERE reservations.room = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, roomId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    reservation = parseReservationResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    public boolean checkout(String reservationId, String roomId) {
        RoomsService roomsService = new RoomsService();
        try {
            dbConnection.setAutoCommit(false);

            String updateReservationQuery = "UPDATE reservations SET checkOutTime = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(updateReservationQuery)) {
                preparedStatement.setString(1, LocalDateTime.now().toString());
                preparedStatement.setString(2, reservationId);
                preparedStatement.executeUpdate();
            }

            roomsService.updateRoomStatus(roomId, RoomStatus.MAINTENANCE);
            dbConnection.commit();

            return true;
        } catch (SQLException e) {
            try {
                dbConnection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                dbConnection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean closeReservation(String reservationId, String roomId) {
        RoomsService roomsService = new RoomsService();
        try {
            dbConnection.setAutoCommit(false);

            String updateReservationQuery = "UPDATE reservations SET status = 'CLOSED' WHERE id = ?";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(updateReservationQuery)) {
                preparedStatement.setString(1, reservationId);
                preparedStatement.executeUpdate();
            }

            roomsService.updateRoomStatus(roomId, RoomStatus.AVAILABLE);
            dbConnection.commit();

            return true;
        } catch (SQLException e) {
            try {
                dbConnection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                dbConnection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updateNote(String reservationId, String updatedNote) {
        String query = "UPDATE reservations SET note = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, updatedNote);
            preparedStatement.setString(2, reservationId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addReservation(Reservation reservation, String roomId) {
        RoomsService roomsService = new RoomsService();
        try {
            dbConnection.setAutoCommit(false);

            String insertReservationQuery = "INSERT INTO reservations (checkInTime, status, room, customerName, note, customerCount) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(insertReservationQuery, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, reservation.getCheckInTime());
                preparedStatement.setString(2, reservation.getStatus().toString());
                preparedStatement.setString(3, roomId);
                preparedStatement.setString(4, reservation.getCustomerName());
                preparedStatement.setString(5, reservation.getNote());
                preparedStatement.setString(6, String.valueOf(reservation.getCustomerCount()));
                preparedStatement.executeUpdate();
            }

            roomsService.updateRoomStatus(roomId, RoomStatus.OCCUPIED);
            dbConnection.commit();

            return true;
        } catch (SQLException e) {
            try {
                dbConnection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                dbConnection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deleteReservation(String reservationId) {
        String query = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, reservationId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Reservation parseReservationResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String checkInTime = resultSet.getString("checkInTime");
        String checkOutTime = resultSet.getString("checkOutTime");
        String statusString = resultSet.getString("status");
        ReservationStatus status = ReservationStatus.valueOf(statusString);
        String note = resultSet.getString("note");
        String customerName = resultSet.getString("customerName");
        int customerCount = resultSet.getInt("customerCount");
        int roomId = resultSet.getInt("room");
        String roomName = resultSet.getString("room_name");
        String roomStatus = resultSet.getString("room_status");
        Room room = new Room(roomId, roomName, RoomStatus.valueOf(roomStatus));
        return new Reservation(id, checkInTime, checkOutTime, customerName, customerCount, note, status, room);
    }
}
