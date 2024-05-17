package org.issk.dto;

import java.util.ArrayList;

public class User {
    private int userId;
    private String username;
    private String password;
    private ArrayList<Genre> preferredGenres;
    private ArrayList<Movie> favouriteMovies;

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Genre> getPreferredGenres() {
        return preferredGenres;
    }

    public void setPreferredGenres(ArrayList<Genre> preferredGenres) {
        this.preferredGenres = preferredGenres;
    }

    public ArrayList<Movie> getFavouriteMovies() {
        return favouriteMovies;
    }

    public void setFavouriteMovies(ArrayList<Movie> favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }
}
