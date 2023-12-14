package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.enums.UserRole;
import com.ht.hoteldelluna.models.Room;
import com.ht.hoteldelluna.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsersService {
    java.sql.Connection dbConnection = Connection.shared.getConnection();
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fullname = resultSet.getString("fullname");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                UserRole role = UserRole.valueOf(resultSet.getString("role"));
                users.add(new User(id, fullname, username, password, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserByUsername(String username) throws Exception {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String fullName = resultSet.getString("fullName");
                    String storedUsername = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");

                    return new User(id, fullName, storedUsername, password, UserRole.valueOf(role));
                }
            }
        }
        throw new Exception("Tài khoản không tồn tại");
    }

    public boolean existsByUsername(String username) {
        boolean exists = false;
        String query = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    exists = true;
                }
            }
        } catch (SQLException e) {
            exists = false;
            e.printStackTrace();
        }
        return exists;
    }

    public User getUserById(String userId) {
        User user = null;
        String query = "SELECT * from users where id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String fullName = resultSet.getString("fullname");
                    String storedUsername = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");

                    user = new User(id, fullName, storedUsername, password, UserRole.valueOf(role));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    public boolean addUser(User user) {
        String query = "INSERT INTO users (fullname, username, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole().toString());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String userId) {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(String userId, String username, String fullName, String password) {
        String query = "UPDATE users SET username = ?, fullname = ?, password = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, fullName);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
