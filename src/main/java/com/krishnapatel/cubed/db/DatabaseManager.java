package com.krishnapatel.cubed.db;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.krishnapatel.cubed.util.ConfigLoader;
import com.krishnapatel.cubed.model.*;

public class DatabaseManager {

    private Connection conn;

    public DatabaseManager() {
            try {
                String dbPath = ConfigLoader.getDB();
                conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                }
                System.out.println("Connected to SQLite at " + dbPath);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public Connection getConnection() { return conn; }

    public void initialize() {
        String createMovieTable = "CREATE TABLE IF NOT EXISTS movies (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL" +
                ");";
        String createReviewDateTable = "CREATE TABLE IF NOT EXISTS reviewDates (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," + 
                "movie_id INTEGER NOT NULL," +
                "review TEXT, " + 
                "dateWatched TEXT, " + // store as MM-DD-YYYY
                "rating INTEGER," + // store rating from 0-10
                "liked INTEGER," + // store Boolean as 0/1
                "FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE" +
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
                "FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE" + 
                ");";
        // Insert into movies, insert into filmActual, insert into watchlist
        // When user watches the movie, remove from watchlist and add to reviewDates 
        String createWatchlistTable = "CREATE TABLE IF NOT EXISTS watchlist (" + 
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "movie_id INTEGER NOT NULL," +
                "addedDate TEXT," + // Date added to watchlist, MM-DD-YYYY
                "FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            // Initializing each table
            stmt.execute(createMovieTable);
            stmt.execute(createReviewDateTable);
            stmt.execute(createFilmActualTable);
            stmt.execute(createWatchlistTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // @addMovie() - Add movie to movie table, filmActual table, and reviewDate table
    // @Parameters - 
    // @Return - 
    //To-do= check watchlist, then remove from there
    public void addMovie(Film film) {
        try {
            conn.setAutoCommit(false);

            int movieId;
            // Insert into movies
            String insertMovie = "INSERT INTO movies(title) VALUES(?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertMovie, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, film.getTitle());
                pstmt.executeUpdate();

                // Get MovieID
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        movieId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to get movie_id");
                    }
                }
            }

            // Insert FilmActual
            String insertFilmActual = "INSERT INTO filmActual(movie_id, title, year, runtime, genre, director, actors, plot, posterURL, imdbID, type) " +
                                        "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            FilmActual actual = film.getActual();
            try (PreparedStatement pstmt = conn.prepareStatement(insertFilmActual)) {
                pstmt.setInt(1, movieId);
                pstmt.setString(2, actual.getTitle());
                pstmt.setInt(3, actual.getYear());
                pstmt.setString(4, actual.getRuntime());
                pstmt.setString(5, actual.getGenre());
                pstmt.setString(6, actual.getDirector());
                pstmt.setString(7, actual.getActors());
                pstmt.setString(8, actual.getPlot());
                pstmt.setString(9, actual.getPosterURL());
                pstmt.setString(10, actual.getImdbID());
                pstmt.setString(11, actual.getType());
                pstmt.executeUpdate();
            }

            // Insert Reviews/Dates
            String insertReview = "INSERT INTO reviewDates(movie_id, review, dateWatched, rating, liked) " +
                                "VALUES(?,?,?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertReview)) {
                ReviewData firstReview = film.getReviews().get(0);
                pstmt.setInt(1, movieId);
                pstmt.setString(2, film.getReviews().isEmpty() ? null : firstReview.getReview());
                pstmt.setString(3, film.getReviews().isEmpty() ? null : firstReview.getDateWatched());
                pstmt.setInt(4, firstReview.getRating());
                pstmt.setInt(5, firstReview.getLiked() ? 1 : 0);
                pstmt.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) { conn.rollback(); }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // @findWatchedMovie - Find movie from DB using movieName
    // @Parameters - String movieName
    // @Return - Film film found
    public Film findWatchedMovie(String movieName) {
        String findMovieStmt = "SELECT id, title FROM movies WHERE title = ?";
        Film film = null;
        
        // Creates film object
        try (PreparedStatement pstmt = conn.prepareStatement(findMovieStmt)) {
            pstmt.setString(1, movieName); // Inserts movie to be found in the query 

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Initializes Film object
                    int movieId = rs.getInt("id");
                    String title = rs.getString("title");
                    film = new Film(movieId, title);

                    // Creates ReviewData objects and adds to Film objects List/ArrayList
                    String findReviewStmt = "SELECT * FROM reviewDates WHERE movie_id = ?";
                    try (PreparedStatement pstmt2 = conn.prepareStatement(findReviewStmt)) {
                        pstmt2.setInt(1, movieId);

                        try (ResultSet rs2 = pstmt2.executeQuery()) {
                            while (rs2.next()) {
                                int reviewId = rs2.getInt("id");
                                int movieIdFromReview = rs2.getInt("movie_id");
                                String review = rs2.getString("review");
                                String dateWatched = rs2.getString("dateWatched");
                                int rating = rs2.getInt("rating");
                                boolean liked = rs2.getInt("liked") == 1;
                                
                                // Creates ReviewData reviewData objects and adds to Film film Array/List of reviews
                                ReviewData reviewData = new ReviewData(reviewId, movieIdFromReview, review, dateWatched, rating, liked);
                                film.addReview(reviewData);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // Create FilmActual object and add to Film object
                    String findFilmActualStmt = "SELCT * FROM filmActual WHERE movie_id = ?";
                    try (PreparedStatement pstmt3 = conn.prepareStatement(findFilmActualStmt)) {
                        pstmt3.setInt(1, movieId);

                        try (ResultSet rs3 = pstmt3.executeQuery()) {
                            if (rs3.next()) {
                                int reviewId = rs3.getInt("id");
                                int movieIdFromReview = rs3.getInt("movie_id");
                                String filmActualTitle = rs3.getString("Title");
                                int year = rs3.getInt("Year");
                                String runtime = rs3.getString("Runtime");
                                String genre = rs3.getString("Genre");
                                String director = rs3.getString("Director");
                                String actors = rs3.getString("Actors");
                                String plot = rs3.getString("Plot");
                                String poster = rs3.getString("Poster");
                                String imdbID = rs3.getString("imdbID");
                                String type = rs3.getString("Type");

                                // Creates FilmActual object and adds to Film film
                                FilmActual filmActual = new FilmActual(reviewId, movieIdFromReview, filmActualTitle, year, runtime, genre, director, actors, plot, poster, imdbID, type);
                                film.setActual(filmActual);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return film;
    }

    // @addReview - add a new ReviewData object to an existing movie in DB
    // @Parameters - 
    // @Return - 
    public void addReview(Film film, ReviewData reviewData) {
        String insertReview = "INSERT INTO reviewDates(movie_id, review, dateWatched, rating, liked) " +
                            "VALUES(?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertReview)) {
                pstmt.setInt(1, reviewData.getMovieID());
                pstmt.setString(2, reviewData.getReview());
                pstmt.setString(3, reviewData.getDateWatched());
                pstmt.setInt(4, reviewData.getRating());
                pstmt.setInt(5, reviewData.getLiked() ? 1 : 0);
                pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // @updateReview - 
    // @Parameters - 
    // @Return updated movie
    //might need to overload, String name or Film film
    // public Film updateReview(String movieName) {

    // }

    // @removeMovie - Removes a movie object and all accompanying objects from reviewData, filmActual, watchlist
    // @Parameters - int movie_id
    // @Return - void
    public void removeMovie(int movie_id) {
        String removeMovieCascadeStmt = "DELETE FROM movies WHERE movie_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(removeMovieCascadeStmt)) {
            pstmt.setInt(1, movie_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // @addMovieToWatchlist - 
    // @Parameters - 
    // @Return - void
    //Insert into movies, insert into filmActual, insert into watchlist
    //When user watches the movie, remove from watchlist and add to reviewDates 
    public void addMovieToWatchlist(FilmActual actual, WatchlistItem watchlistItem) {
        //Insert Film
        String insertMovie = "INSERT INTO movies(title) VALUES(?)";
        // Insert FilmActual
        String insertFilmActual = "INSERT INTO filmActual(movie_id, title, year, runtime, genre, director, actors, plot, posterURL, imdbID, type) " +
                                    "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        // Insert Watchlist item
        String insertWatchlist = "INSERT INTO watchlist(movie_id, addedDate) VALUES(?,?)";

        try {
            conn.setAutoCommit(false);
            int movie_id = 0;

            // Insert into movies
            try (PreparedStatement stmt = conn.prepareStatement(insertMovie, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, actual.getTitle());
                stmt.executeUpdate();

                // Get MovieID
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        movie_id = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to get movie_id");
                    }
                }
            }

            // Insert into filmActual
            try (PreparedStatement pstmt = conn.prepareStatement(insertFilmActual)) {
                pstmt.setInt(1, movie_id);
                pstmt.setString(2, actual.getTitle());
                pstmt.setInt(3, actual.getYear());
                pstmt.setString(4, actual.getRuntime());
                pstmt.setString(5, actual.getGenre());
                pstmt.setString(6, actual.getDirector());
                pstmt.setString(7, actual.getActors());
                pstmt.setString(8, actual.getPlot());
                pstmt.setString(9, actual.getPosterURL());
                pstmt.setString(10, actual.getImdbID());
                pstmt.setString(11, actual.getType());
                pstmt.executeUpdate();
            }

            // Insert into watchlist 
            try (PreparedStatement pstmt2 = conn.prepareStatement(insertWatchlist)) {
                pstmt2.setInt(1, movie_id);
                pstmt2.setString(2, watchlistItem.getAddedDate());
                pstmt2.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback(); // 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // @watchWatchlistMovie - 
    // @Parameters - 
    // @Return
    public void watchWatchlistMovie(Film film, ReviewData reviewData) {
        //Remove from watchlist
        removeWatchlistMovie(film.getID());
        //Add to ReviewDates
        addReview(film, reviewData);
    }

    // @findWatchlistMovie - 
    // @Parameters - 
    // @Return
    public boolean findWatchlistMovie(int movie_id) {
        String inWatchlistStmt = "SELECT EXISTS (SELECT 1 FROM watchlist WHERE movie_id = ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(inWatchlistStmt)) {
            pstmt.setInt(1, movie_id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt(1) == 1; // 1 if it exists, 0 if not
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // @removeWatchlistMovie - 
    // @Parameters - 
    // @Return
    public void removeWatchlistMovie(int movie_id) {
        String removeWatchlistStmt = "DELETE FROM watchlist WHERE movie_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(removeWatchlistStmt)) {
            pstmt.setInt(1, movie_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
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