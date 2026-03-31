package com.linh.exam.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Establish connection to SQLite database
    public static Connection connect() throws SQLException {
        // Get project directory path
        String dbPath = System.getProperty("user.dir") + "/university.db";

        // Connect to database
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

}


