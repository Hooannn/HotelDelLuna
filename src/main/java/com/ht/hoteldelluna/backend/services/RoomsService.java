package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.enums.RoomStatus;
import com.ht.hoteldelluna.models.Floor;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.RoomType;

import javax.print.Doc;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class RoomsService {
    java.sql.Connection dbConnection = Connection.shared.getConnection();

    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT rooms.*, floors.num AS floor_num, room_types.name AS type_name, room_types.pricePerHour AS type_price_per_hour " +
                "FROM rooms " +
                "JOIN floors ON rooms.floor = floors.id " +
                "JOIN room_types ON rooms.type = room_types.id";
        try (Statement statement = dbConnection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Room room = parseRoomResultSet(resultSet);
                rooms.add(room);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rooms;
    }

    // Mark: Doing
    public Room getRoomById(String roomId) {
        Room room = null;
        String query = "SELECT rooms.*, floors.num AS floor_num, room_types.name AS type_name, room_types.pricePerHour AS type_price_per_hour " +
                "FROM rooms " +
                "JOIN floors ON rooms.floor = floors.id " +
                "JOIN room_types ON rooms.type = room_types.id " +
                "WHERE rooms.id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, roomId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    room = parseRoomResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    public boolean checkNameofRoom(String name) {
        String query = "SELECT COUNT(*) FROM rooms WHERE name = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addRoom(Room room, String roomTypeId, String floorId) {
        String query = "INSERT INTO rooms (name, status, type, floor) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, room.getName());
            preparedStatement.setString(2, room.getStatus().toString());
            preparedStatement.setString(3, roomTypeId);
            preparedStatement.setString(4, floorId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoom(String roomId) {
        String query = "DELETE FROM rooms WHERE id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, roomId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRoomStatus(String roomId, RoomStatus status) {
        String query = "UPDATE rooms SET status = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, status.toString());
            preparedStatement.setString(2, roomId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Room parseRoomResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        RoomStatus status = RoomStatus.valueOf(resultSet.getString("status"));
        int floorId = resultSet.getInt("floor");
        int typeid = resultSet.getInt("type");
        String typeName = resultSet.getString("type_name");
        int floorNum = resultSet.getInt("floor_num");
        double pricePerHour = resultSet.getDouble("type_price_per_hour");

        Room room = new Room(id, name, status);
        room.setFloor(new Floor(floorId, floorNum));
        room.setType(new RoomType(typeid, typeName, pricePerHour));

        return room;
    }
}
