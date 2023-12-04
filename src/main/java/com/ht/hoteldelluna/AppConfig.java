package com.ht.hoteldelluna;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream input = new FileInputStream("local.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Failed to load local.properties. Using default values.");
            e.printStackTrace();
        }
    }

    public static String getJdbcUrl() {
        return properties.getProperty("jdbc.url", "jdbc:mysql://localhost:3306/default");
    }

    public static String getUser() {
        return properties.getProperty("user", "root");
    }

    public static String getPassword() {
        return properties.getProperty("password", "");
    }
}