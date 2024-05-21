package org.issk.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Movie {

    private String id;
    private String title;
    private float vote_average;
    //private ArrayList<Genre> genres;

    public Movie() {

    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


/*public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }*/
}
