package com.krishnapatel.cubed.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.krishnapatel.cubed.model.*;
import com.krishnapatel.cubed.api.*;

public class FilmManager {
    
    //these are going in DB
    private ArrayList<Film> listOfMovies = new ArrayList<>();
    Map<String, FilmActual> watchlist = new HashMap<>();
    
    //These will be loaded into DB on startup  
    private Film[] favorites = new Film[4];
    private ArrayList<Film> likedFilms = new ArrayList<>();

    //make a search method first using getFilmByGeneric, then choose from the returned as an array (use JsonArray), allow to traverse
    // pages, then add
    public ArrayList<FilmActual> searchMovie(String movieName) {
        ArrayList<FilmActual> searchResults = new ArrayList<>();
        // If user searches with ImdbID. 
        if(movieName.startsWith("tt")) { 
            try {
                searchResults.add(FilmApiService.getFilmbyID(movieName)); 
                return searchResults;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        

        return searchResults;
    }

    public void addMovie(String movieName, String movieReview, String movieDate, int movieRating, Boolean like) {
        //Checks if movie already exists in the db, and updates accordingly
        Film doesItExist = findWatched(movieName);
        if (doesItExist != null && !doesItExist.equals(new Film())) {
            updateReview(doesItExist, movieReview, movieDate, movieRating, like);
            return;
        }
        
        Film filmTemp = new Film(movieName, movieReview, movieDate, movieRating, like);
        
        listOfMovies.add(filmTemp);
        if (like) { likedFilms.add(filmTemp); }

        if (watchlist.containsKey(movieName)) {
            filmTemp.setFilmActual(watchlist.get(movieName));
            watchlist.remove(movieName);
        } else {
            FilmActual filmAct = null;
            try {
                filmAct = FilmApiService.getFilmByName(movieName);
                filmTemp.setFilmActual(filmAct);
                filmTemp.setMovie(filmAct.getTitle());
            } catch (Exception e) {
                System.out.println("Catch exception.");
                e.printStackTrace();
            }
        }

        System.out.println(filmTemp.getMovie() + " has been added to your Libaray. \n");
        displayMovie(filmTemp);
        System.out.println();
    }

    public Film findWatched(String movieName) {
        if (listOfMovies.size() == 0) { return null; }
        for(Film movie : listOfMovies) {
            if (movie.getMovie().equalsIgnoreCase(movieName) || movie.getFilmActual().getImdbID().equals(movieName)) {
                return movie;
            }
        }
        return new Film();
    }

    public void updateReview(String movieName, String newMovieReivew, String newDate, int newMovieRating, Boolean like) {
        Film updatedMovie = findWatched(movieName);
        updatedMovie.setMovieReview(newMovieReivew);
        updatedMovie.setDate(newDate);
        updatedMovie.setRating(newMovieRating);
        updatedMovie.setLike(like);
        System.out.println(updatedMovie.getMovie() + " has been updated. ");
        displayMovie(updatedMovie);
    }

    public void updateReview(Film movie, String movieReview, String movieDate, int movieRating, Boolean like) {
        movie.setMovieReview(movieReview);
        movie.setDate(movieDate);
        movie.setRating(movieRating);
        movie.setLike(like);
        System.out.println(movie.getMovie() + " has been updated. ");
        displayMovie(movie);
    }

    public void removeMovie(String movie) {
        for (int i = 0; i < listOfMovies.size(); i++) {
            if (listOfMovies.get(i).getMovie().equals(movie)) {
                System.out.println(listOfMovies.get(i).getMovie() + " has been removed. ");
                listOfMovies.remove(i);
                return;
            }
        }
    }

    public void displayMovieActual(Film movie) {
        FilmActual movieAct = movie.getFilmActual();
        System.out.println("  Director: " + movieAct.getDirector());
        System.out.println("  Actors: " + movieAct.getActors());
        System.out.println("  Plot: " + movieAct.getPlot());
        System.out.println("  Type: " + movieAct.getType());
    }

    public void displayMovie(Film movie) {
        System.out.println("Movie: " + movie.getMovie());
        System.out.println("Reviews: ");
        for (int i = 0; i < movie.getMovieReview().size(); i++) {
            System.out.println("  " + (i+1) + ") " + movie.getMovieReview().get(i));
        }
        System.out.println("Dates watched: ");
        for (int i = 0; i < movie.getDate().size(); i++) {
            System.out.println("  " + (i+1) + ") " + movie.getDate().get(i));
        }
        System.out.println("Rating: " + movie.getRating());
        System.out.println("Movie info: ");
        displayMovieActual(movie);
    }

    public void displayMovieDates(Film movie) {
        for (int i = 0; i < movie.getDate().size(); i++) {
            System.out.println("  " + (i+1) + ") " + movie.getDate().get(i));
        }
    }

    public void displayMovieReviews(Film movie) {
        for (int i = 0; i < movie.getMovieReview().size(); i++) {
            System.out.println("  " + (i+1) + ") " + movie.getMovieReview().get(i));
        }
    }

    public ArrayList<Film> getListOfMovies() { return listOfMovies; }

    public void addMovieWatchList(String name) {
        FilmActual filmAct = null;
        try {
            if (name.startsWith("tt")) {
                filmAct = FilmApiService.getFilmbyID(name);
            } else {
                filmAct = FilmApiService.getFilmByName(name);
            }
        } catch (Exception e) {
            System.out.println("Catch exception. ");
            e.printStackTrace();
        }
        watchlist.put(name, filmAct); 
    }
}
