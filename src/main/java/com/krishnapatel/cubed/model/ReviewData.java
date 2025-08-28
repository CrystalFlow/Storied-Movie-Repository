package com.krishnapatel.cubed.model;

public class ReviewData {
    private int id;
    private int movie_id;
    private String review;
    private String dateWatched;
    private int rating;
    private boolean liked;

    // Constructor for new review (no id yet, no movie_id yet)
    // Set id and movie_id during insertion into DB
    public ReviewData(String review, String dateWatched, int rating, boolean liked) {
        this.review = review;
        this.dateWatched = dateWatched;
        this.rating = rating;
        this.liked = liked;
    }

    // Constructor for review loaded from DB (with id and movie_id)
    public ReviewData(int id, int movie_id, String review, String dateWatched, int rating, boolean liked) {
        this.id = id;
        this.movie_id = movie_id;
        this.review = review;
        this.dateWatched = dateWatched;
        this.rating = rating;
        this.liked = liked;
    }

    public int getID() { return id; }
    public void setID(int id) { this.id = id; }

    public int getMovieID() { return movie_id; }
    public void setMovieID(int movie_id) { this.movie_id = movie_id; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public String getDateWatched() { return dateWatched; }
    public void setDateWatched(String dateWatched) { this.dateWatched = dateWatched; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public boolean getLiked() { return liked; }
    public void setLiked(boolean liked) { this.liked = liked; }
}
