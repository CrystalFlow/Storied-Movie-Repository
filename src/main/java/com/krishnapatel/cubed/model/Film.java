package com.krishnapatel.cubed.model;

import java.util.ArrayList;
import java.util.List;

public class Film {
    private int id;
    private String title;
    private FilmActual actual;
    private List<ReviewData> reviews;

    // Constructor for new film (no id yet)
    // Set id during insertion into DB
    public Film(String title) {
        this.title = title;
    }

    // Constructor for film loaded from DB (with id)
    public Film(int id, String title) {
        this.id = id;
        this.title = title;
        reviews = new ArrayList<>();
    }

    public int getID() { return id; }
    public void setID(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public FilmActual getActual() { return actual; }
    public void setActual(FilmActual actual) { this.actual = actual; }

    public List<ReviewData> getReviews() { return reviews; }
    public void setReview(List<ReviewData> reviews) { this.reviews = reviews; }

    // Add ReviewData objects to Films
    public void addReview(ReviewData reviewData) { this.reviews.add(reviewData); }
}
