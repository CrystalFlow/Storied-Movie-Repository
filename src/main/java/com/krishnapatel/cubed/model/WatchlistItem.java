package com.krishnapatel.cubed.model;

public class WatchlistItem {
 
    private int id;
    private int movie_id;
    private String title; 
    private String addedDate;

    public WatchlistItem(int id, int movie_id, String title, String addedDate) {
        this.id = id;
        this.movie_id = movie_id;
        this.title = title;
        this.addedDate = addedDate;
    }

    public int getID() { return id; }
    public void setID(int id) { this.id = id; }

    public int getMovieID() { return movie_id; }
    public void setMovieID(int movie_id) { this.movie_id = movie_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAddedDate() { return addedDate; }
    public void setAddedDate(String addedDate) { this.addedDate = addedDate; }
}
