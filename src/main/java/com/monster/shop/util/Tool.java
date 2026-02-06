package com.monster.shop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Tool {
    // Database URL for MySQL 8.0
    // Includes timezone and encoding settings to prevent common errors
    public static final String URL = "jdbc:mysql://localhost:3306/monster_shop?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true&useSSL=false";
    
    // Database Credentials
    public static final String USER = "root";
    // TODO: Change this to your MySQL password
    public static final String PASSWORD = "1234"; 

    // Static block to load driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to get connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Main method for testing connection immediately
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("=================================");
            System.out.println("Connection Success! (連線成功)");
            System.out.println("Connected to: " + conn.getCatalog());
            System.out.println("=================================");
        } catch (SQLException e) {
            System.err.println("Connection Failed! (連線失敗)");
            e.printStackTrace();
        }
    }
}