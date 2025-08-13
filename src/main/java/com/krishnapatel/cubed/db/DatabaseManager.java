package com.krishnapatel.cubed.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.krishnapatel.cubed.util.ConfigLoader;

public class DatabaseManager {

    private static Connection conn = null;

    public static Connection connect() {
        if (conn == null) {
            try {
                String dbPath = ConfigLoader.getDB();
                conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                System.out.println("Connected to SQLite at " + dbPath);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void shutdown() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("DB connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}