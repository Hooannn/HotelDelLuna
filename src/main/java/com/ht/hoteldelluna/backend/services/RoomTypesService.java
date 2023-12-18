package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.models.RoomType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomTypesService {
    java.sql.Connection dbConnection = Connection.shared.getConnection();

    public List<RoomType> getRoomTypes() {
        List<RoomType> roomTypes = new ArrayList<>();
        try (Statement statement = dbConnection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM room_types");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double pricePerHour = resultSet.getDouble("pricePerHour");
                roomTypes.add(new RoomType(id, name, pricePerHour));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomTypes;
    }

    public RoomType getRoomTypeById(String roomTypeId) {
        RoomType roomType = null;
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT * FROM room_types WHERE id = ?")) {
            preparedStatement.setString(1, roomTypeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    double pricePerHour = resultSet.getDouble("pricePerHour");
                    roomType = new RoomType(id, name, pricePerHour);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomType;
    }

    public RoomType getRoomTypeByName(String name) {
        RoomType roomType = null;
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT * FROM room_types WHERE name = ?")) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String rname = resultSet.getString("name");
                    double pricePerHour = resultSet.getDouble("pricePerHour");
                    roomType = new RoomType(id, rname, pricePerHour);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomType;
    }

    public boolean addRoomType(RoomType roomType) {
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO room_types (name, pricePerHour) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, roomType.getName());
            preparedStatement.setDouble(1, roomType.getPricePerHour());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoomType(String roomTypeId) {
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement("DELETE FROM room_types WHERE id = ?")) {
            preparedStatement.setString(1, roomTypeId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addMockRoomTypes() {
        try (Statement statement = dbConnection.createStatement()) {
            statement.addBatch("INSERT INTO room_types (name, pricePerHour) VALUES ('VIP', '50000')");
            statement.addBatch("INSERT INTO room_types (name, pricePerHour) VALUES ('Thường', '50000')");
            statement.addBatch("INSERT INTO room_types (name, pricePerHour) VALUES ('Super Vip', '50000')");
            int[] results = statement.executeBatch();
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkRoomTypeName(String updatedName) {
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT * FROM room_types WHERE name = ?")) {
            preparedStatement.setString(1, updatedName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateRoomType(int id, String updatedName, int i) {
        //update room type
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement("UPDATE room_types SET name = ?, pricePerHour = ? WHERE id = ?")) {
            preparedStatement.setString(1, updatedName);
            preparedStatement.setInt(2, i);
            preparedStatement.setInt(3, id);
            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void addRoomType(String name, int i) {
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO room_types (name, pricePerHour) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, i);
            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
