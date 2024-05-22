package org.issk.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class User {
    private int userId;
    private String username;
    private String password;
    private HashMap<Integer, Genre> preferredGenres;
    private HashMap<Integer, Movie> favouriteMovies;

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

    public HashMap<Integer, Genre> getPreferredGenres() {
        return preferredGenres;
    }

    public void setPreferredGenres(HashMap<Integer, Genre> preferredGenres) {
        this.preferredGenres = preferredGenres;
    }

    public HashMap<Integer, Movie> getFavouriteMovies() {
        return favouriteMovies;
    }

    public void setFavouriteMovies(HashMap<Integer, Movie> favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }
}
