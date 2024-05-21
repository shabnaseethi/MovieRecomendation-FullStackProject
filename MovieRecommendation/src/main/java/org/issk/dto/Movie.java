package org.issk.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Movie {

    private int id;
    private String title;
    //private ArrayList<Genre> genres;

    public Movie() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /*public int getMovieId() {
        return movievId;
    }

    public void setMovieId(int movieId) {
        this.movievId = movieId;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

/*public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }*/


}
