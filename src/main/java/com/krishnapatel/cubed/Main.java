package com.krishnapatel.cubed;

import java.util.Scanner;
import com.krishnapatel.cubed.service.*;
import com.krishnapatel.cubed.api.FilmApiService;
import com.krishnapatel.cubed.model.*;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        FilmManager test = new FilmManager();

        System.out.println("Welcome to Cubed.");
        System.out.println("If you want to end session, type '-1' to end. ");
        System.out.println();

        int userInput = 0;
        while (!(userInput == -1)) {
            System.out.println("What would you like to do? \n 1) Add a movie? \n 2) Find a movie? \n 3) Update a review? \n 4) Remove a movie? \n 5) Display all movies?");
            System.out.println(" 7) Add to watchlist? ");
            System.out.print("=> ");
            userInput = input.nextInt(); // StdIn.readInt();
            input.nextLine(); // StdIn.readLine();
            if (userInput == 1) {
                System.out.println();
                System.out.print("Movie name: ");
                String name = input.nextLine(); // StdIn.readLine();

                System.out.print("Movie review: ");
                String review = input.nextLine(); // StdIn.readLine();

                System.out.print("Date watched: ");
                String date = input.nextLine(); // StdIn.readLine();

                System.out.print("Rating: ");
                int rating = input.nextInt(); // StdIn.readInt();
                input.nextLine(); // StdIn.readLine();

                System.out.print("Like? (Y/N) ");
                Boolean liked = false;
                String choice = input.nextLine();
                if (choice.equalsIgnoreCase("Y")) { liked = true; }

                test.addMovie(name, review, date, rating, liked);
            } else if (userInput == 2) {
                System.out.print("What movie do you want to find? ");
                String movie = input.nextLine(); // StdIn.readLine();
                Film movieFound = test.findWatched(movie);
                if (movieFound == null) { System.out.println("You have no movies in your list. "); continue; }
                if (movieFound.equals(new Film())) { System.out.println("Movie was not found. "); continue; }
                test.displayMovie(movieFound);
            } else if (userInput == 3) {
                System.out.print("What movie do you want to update? ");
                String movie = input.nextLine(); // StdIn.readLine();
                Film movieFound = test.findWatched(movie);
                if (!(movieFound.equals(new Film()))) {
                    System.out.print("Update name (Y/N)? ");
                    String upN = input.nextLine(); // StdIn.readLine();
                    if (upN.equals("Y")) {
                        System.out.print("Enter the new name: ");
                        String newName = input.nextLine(); // StdIn.readLine();
                        movieFound.setMovie(newName);
                        System.out.println("Movie name updated. ");
                    }
                    
                    System.out.print("Update review (Y/N)? ");
                    String upRe = input.nextLine(); // StdIn.readLine();
                    if (upRe.equals("Y")) {
                        test.displayMovieReviews(movieFound);
                        System.out.print("Which review do you want to update? ");
                        int reviewToChange = input.nextInt(); // StdIn.readInt();
                        input.nextLine(); // StdIn.readLine();
                        System.out.print("Enter the new review: ");
                        String newReview = input.nextLine(); // StdIn.readLine();
                        movieFound.setMovieReview(newReview, reviewToChange - 1);
                        System.out.println("Movie review updated. ");
                    }
                    
                    System.out.print("Update date (Y/N)? ");
                    String upD = input.nextLine(); // StdIn.readLine();
                    if (upD.equals("Y")) {
                        test.displayMovieDates(movieFound);
                        System.out.print("Which date do you want to update? ");
                        int dateToChange = input.nextInt(); // StdIn.readInt();
                        input.nextLine(); // StdIn.readLine();
                        System.out.print("Enter the new date: ");
                        String newDate = input.nextLine(); // StdIn.readLine();
                        movieFound.setDate(newDate, dateToChange - 1);
                        System.out.println("Movie date updated. ");
                    }

                    System.out.print("Update rating (Y/N)? ");
                    String upRa = input.nextLine(); // StdIn.readLine();
                    if (upRa.equals("Y")) {
                        System.out.print("Enter the new rating: ");
                        int newRating = input.nextInt(); // StdIn.readInt();
                        movieFound.setRating(newRating);
                        System.out.println("Movie rating updated. ");
                    }

                    System.out.println("Update like? (Y/N)? ");
                    //finsih updating like here
                    
                    test.displayMovie(movieFound);
                } else {
                    System.out.print("Movie not found. ");
                }
            } else if (userInput == 4) {
                System.out.print("What movie do you want to remove? ");
                String movToRemove = input.nextLine(); // StdIn.readLine();
                test.removeMovie(movToRemove);
            } else if (userInput == 5) {
                for (int i = 0; i < test.getListOfMovies().size(); i++) {
                    System.out.println((i+1) + ": ");
                    test.displayMovie(test.getListOfMovies().get(i));
                    System.out.println();
                }
            } else if (userInput == 8) {
                //print favs
            } else if (userInput == -1) {
                continue;
            } else {
                System.out.println("Please choose a valid option.");
                continue;
            }
        }
        System.out.println("Session ended. \n");
        input.close();
    }
}
