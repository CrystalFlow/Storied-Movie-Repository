package com.krishnapatel.cubed.db;

import java.sql.Statement;
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

    public static void initialize() {
        String createMovieTable = "CREATE TABLE IF NOT EXISTS movies (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                ");";
        String createReviewDateTable = "CREATE TABLE IF NOT EXISTS reviewDates (" +
                "id INTERGER PRIMARY KEY AUTOINCREMENT," + 
                "movie_id INTEGER NOT NULL," +
                "review TEXT, " + 
                "dateWatched TEXT, " + // store as MM-DD-YYYY
                "rating INTEGER," + // store rating from 0-10
                "liked INTEGER," + // store Boolean as 0/1
                "FOREIGN KEY(movie_id) REFERENCES movies(id)" +
                ");";
        String createFilmActualTable = "CREATE TABLE IF NOT EXISTS filmActual (" + 
                "id INTEGER PRIMARY KEY AUTOINCREMENT," + 
                "movie_id INTEGER NOT NULL, " + 
                "title TEXT," +
                "year INT," + // YYYY
                "runtime TEXT," + // xxx min
                "genre TEXT," +
                "director TEXT," +
                "actors TEXT," +
                "plot TEXT," +
                "posterURL TEXT," + 
                "imdbID TEXT ," + // Starts with "tt"
                "type TEXT," + // Movie or TV
                "FOREIGN KEY(movie_id) REFERENCES movies(id)" + 
                ");";
        
        /*
         * Maybe create a favorites table
         */

        try (Connection conn = connect();
            Statement stmt = conn.createStatement()) {
            // Initializing each table
            stmt.execute(createMovieTable);
            stmt.execute(createReviewDateTable);
            stmt.execute(createFilmActualTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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