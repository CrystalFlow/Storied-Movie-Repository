package com.krishnapatel.cubed.model;

public class FilmActual {
    private int id;
    private int movie_id;
    private String Title;
    private int Year;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Actors;
    private String Plot;
    private String Poster;
    private String imdbID;
    private String Type; 


    public FilmActual(int id, int movie_id, String Title, int Year, String Runtime, String Genre, String Director, String Actors, String Plot, String Poster, String imdbID, String Type) {
        this.id = id;
        this.movie_id = movie_id;
        this.Title = Title;
        this.Year = Year;
        this.Runtime = Runtime;
        this.Genre = Genre;
        this.Director = Director;
        this.Actors = Actors;
        this.Plot = Plot;
        this.Poster = Poster;
        this.imdbID = imdbID;
        this.Type = Type;
    }

    public int getID() { return id; }
    public void setID(int id) { this.id = id; }

    public int getMovieID() { return movie_id; }
    public void setMovieID(int movie_id) { this.movie_id = movie_id; }

    public String getTitle() { return this.Title; }
    public void setTitle(String Title) { this.Title = Title; }

    public int getYear() { return this.Year; }
    public void setYear(int Year) { this.Year = Year; }

    public String getRuntime() { return this.Runtime; }
    public void setRuntime(String Runtime) { this.Runtime = Runtime; }

    public String getGenre() { return this.Genre; }
    public void setGenre(String Genre) { this.Genre = Genre; }

    public String getDirector() { return this.Director; }
    public void setDirector(String Director) { this.Director = Director; }

    public String getActors() { return this.Actors; }
    public void setActors(String Actors) { this.Actors = Actors; }

    public String getPlot() { return this.Plot; }
    public void setPlot(String Plot) { this.Plot = Plot; }

    public String getPosterURL() { return this.Poster; }
    public void setPosterURL(String Poster) { this.Poster = Poster; }

    public String getImdbID() {return this.imdbID; }
    public void setImdbID(String imdbID) { this.imdbID = imdbID; }

    public String getType() { return this.Type; }
    public void setType(String Type) {this.Type = Type; }
}
