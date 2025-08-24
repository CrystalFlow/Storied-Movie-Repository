package com.krishnapatel.cubed.api;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

import com.krishnapatel.cubed.model.FilmActual;
import com.krishnapatel.cubed.util.ConfigLoader;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class FilmApiService {

    private static final String apiKey = ConfigLoader.getApiKey(); 

    // Get film by name
    public static FilmActual getFilmByName(String name) throws Exception {
        String encodedTitle = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String url = "http://www.omdbapi.com/?apikey=" + apiKey + "&t=" + encodedTitle;
        
        String jsonResponse = requestTo(url);

        return jsonInterpretor(jsonResponse);
    }

    // Get film by generic keywords, has pagination
    //check what this actually returns, may need to update this
    public static FilmActual getFilmGeneric(String generic, int page) throws Exception  {
        String encodedGeneric = URLEncoder.encode(generic, StandardCharsets.UTF_8);
        String url = "http://www.omdbapi.com/?apikey=" + apiKey + "&s=" + encodedGeneric + "&page=" + page;
        
        String jsonResponse = requestTo(url);
        JsonArray jsonArray = new JsonArray();
        System.out.println(jsonResponse);

        return null;
        // return requestTo(url);
    }

    // Get film by IMDB ID
    public static FilmActual getFilmbyID(String imdbID) throws Exception  {
        String url = "http://www.omdbapi.com/?apikey=" + apiKey + "&i=" + imdbID;
        
        String jsonResponse = requestTo(url);

        return jsonInterpretor(jsonResponse);
    }

    public static String requestTo(String url) throws Exception {
        HttpRequest filmRequest = HttpRequest.newBuilder()
            .uri(new URI(url))
            .GET()
            .build();
        
        HttpClient clinet = HttpClient.newHttpClient();
        HttpResponse<String> response = clinet.send(filmRequest, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public static FilmActual jsonInterpretor(String respone) {
        Gson gson = new Gson();
        FilmActual film = gson.fromJson(respone, FilmActual.class);
        return film;
    }

}