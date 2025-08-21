package com.krishnapatel.cubed.model;

public class ReviewData {
    private int id;
    private int movie_id;
    private String review;
    private String dateWatched;
    private int rating;
    private Boolean liked;

    public ReviewData(int id, int movie_id, String review, String dateWatched, int rating, Boolean liked) {
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
    public void setLiked(Boolean liked) { this.liked = liked; }
}
