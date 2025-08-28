package com.krishnapatel.cubed.model;

public class WatchlistItem {
 
    private int id;
    private int movie_id;
    private String addedDate;

    // Constructor for new watchlist item (no id yet, no movie_id yet)
    // Set id and movie_id during insertion into DB
    public WatchlistItem(String addedDate) {
        this.addedDate = addedDate;
    }

    // Constructor for watchlist item loaded from DB (with id and movie_id)
    public WatchlistItem(int id, int movie_id, String addedDate) {
        this.id = id;
        this.movie_id = movie_id;
        this.addedDate = addedDate;
    }

    public int getID() { return id; }
    public void setID(int id) { this.id = id; }

    public int getMovieID() { return movie_id; }
    public void setMovieID(int movie_id) { this.movie_id = movie_id; }

    public String getAddedDate() { return addedDate; }
    public void setAddedDate(String addedDate) { this.addedDate = addedDate; }
}
