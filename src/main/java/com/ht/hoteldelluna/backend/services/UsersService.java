package com.ht.hoteldelluna.backend.services;

import com.ht.hoteldelluna.backend.Connection;
import com.ht.hoteldelluna.enums.UserRole;
import com.ht.hoteldelluna.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsersService {
    java.sql.Connection dbConnection = Connection.shared.getConnection();
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
}
