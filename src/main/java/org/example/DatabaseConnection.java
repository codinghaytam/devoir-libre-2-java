package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection = null;

    private static final String URL = "jdbc:mysql://localhost:3306/devoi2db";  // Update with your database URL
    private static final String USER = "root";  // Update with your DB username
    private static final String PASSWORD = "";  // Update with your DB password

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    // Singleton method to get the connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error connecting to the database", e);
            }
        }
        return connection;
    }

//  // Close the connection when the app ends (optional)
//  public static void closeConnection() {
//      if (connection != null) {
//          try {
//              connection.close();
//          } catch (SQLException e) {
//              e.printStackTrace();
//          }
//      }
//  }
}

