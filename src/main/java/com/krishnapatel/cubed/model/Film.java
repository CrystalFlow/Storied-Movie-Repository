package com.krishnapatel.cubed.model;

import java.util.ArrayList;
import java.util.Objects;

public class Film {
    private String movie; //The name of the movie
    private ArrayList<String> movieReview = new ArrayList<>(); //A review of the movie
    private ArrayList<String> date = new ArrayList<>(); //Date the movie was seen
    private int rating; //Rating of the movie out of 10
    private Boolean like = false; //Did you like the movie?
    private FilmActual actual; //for API info, 

    // private int id;
    // private String title;
    // private FilmActual actual;
    // private List<Review> reviews;

    public Film(String movie, String movieReview, String date, int rating, Boolean like) {
        this.movie = movie;
        this.movieReview.add(movieReview); 
        this.date.add(date);
        this.rating = rating;
        this.like = like;
        this.actual = new FilmActual(); 
    }
    // public Film(int id, String title) {
    //     this.id = id;
    //     this.title = title;
    //     this.reviews = new ArrayList<>();
    // }

    public String getMovie() { return this.movie; }
    public void setMovie(String movie) { this.movie = movie; }

    public ArrayList<String> getMovieReview() { 
        return this.movieReview; 
    }
    public void setMovieReview(String newMovieReview) { 
        this.movieReview.add(newMovieReview); 
    }
    public void setMovieReview(String newMovieReview, int i) {
        if (i >= 0 && i < date.size()) { 
            this.movieReview.set(i, newMovieReview); 
        } 
    }
    public void removeReview(int index, boolean fullRemove) { 
        if (fullRemove) { 
            movieReview.remove(index - 1); return; 
        }
        movieReview.set(index - 1, "No written review."); 
    }
    public void updateReview(int index, String newReview) { movieReview.set(index - 1, newReview); }

    public ArrayList<String> getDate() { 
        return this.date; 
    }
    public void setDate(String newDate) { 
        this.date.add(newDate); 
    }
    public void setDate(String newDate, int i) { 
        if (i >= 0 && i < date.size()) { 
            this.date.set(i, newDate); 
        } 
    }
    public void removeDate(int index) {
        removeReview(index, true); 
        date.remove(index - 1); 
    }
    public void updateDate(int index, String newDate) { 
        date.set(index - 1, newDate); 
    }

    public int getRating() { return this.rating; }
    public void setRating(int rating) { this.rating = rating; }

    public Boolean getLike() { return this.like; }
    public void setLike(Boolean like) { this.like = like; }

    public FilmActual getFilmActual() { return this.actual; }
    public void setFilmActual(FilmActual actual) { this.actual = actual; }

    @Override
    public boolean equals(Object other) {
        if (this == other) { return true; }
        if (other == null || getClass() != other.getClass()) { return false; }
        Film film = (Film) other;
        return film.getMovie().equals(this.getMovie()) 
            && film.getMovieReview().equals(this.getMovieReview())
            && film.getDate().equals(this.getDate())
            && film.getRating() == this.getRating();
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, movieReview, date, rating);
    }
}
