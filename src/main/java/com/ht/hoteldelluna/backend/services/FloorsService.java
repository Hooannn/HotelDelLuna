package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.models.Floor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FloorsService {
    private final java.sql.Connection dbConnection = Connection.shared.getConnection();

    public List<Floor> getFloors() {
        List<Floor> floors = new ArrayList<>();
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM floors ORDER BY num")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int num = resultSet.getInt("num");
                floors.add(new Floor(id, num));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return floors;
    }

    public Floor getFloorById(String floorId) {
        Floor floor = null;
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT * FROM floors WHERE id = ?")) {
            preparedStatement.setString(1, floorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int num = resultSet.getInt("num");
                    floor = new Floor(id, num);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return floor;
    }

    public Floor getFloorByNum(int num) {
        Floor floor = null;
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT * FROM floors WHERE num = ?")) {
            preparedStatement.setInt(1, num);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    floor = new Floor(id, num);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return floor;
    }

    public boolean addFloor(Floor floor) {
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO floors (num) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, floor.getNum());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addMockFloors() {
        try (Statement statement = dbConnection.createStatement()) {
            statement.addBatch("INSERT INTO floors (num) VALUES (1)");
            statement.addBatch("INSERT INTO floors (num) VALUES (2)");
            statement.addBatch("INSERT INTO floors (num) VALUES (3)");
            int[] results = statement.executeBatch();

            return results.length > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
