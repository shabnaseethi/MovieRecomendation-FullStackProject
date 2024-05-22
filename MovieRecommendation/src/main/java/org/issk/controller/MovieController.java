package org.issk.controller;

import org.issk.dto.Movie;
import org.issk.service.MovieService;
import org.issk.service.MovieServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(){
        this.movieService= new MovieServiceImpl();
    }

    @GetMapping("/genre/{genre}")
    public List<Movie>  getMoviesByGenre(@PathVariable String genre){
        return movieService.getMoviesByGenre(genre);
    }

    @GetMapping("/rating/{rating}/{sort}")
    public List<Movie>  getMoviesByRating(@PathVariable String rating, @PathVariable String sort){
        return movieService.getMoviesByRating(rating,Boolean.parseBoolean(sort));
    }

    @GetMapping("/preferences/{userid}")
    public List<Movie>  getMoviesByPreferences(@PathVariable String userid){
        return movieService.getMoviesByPreferences(userid);
    }

    @GetMapping("/moviename")
    public List<Movie> getMoviesByName(@RequestParam String name) {
        return movieService.getMoviesByName(name);
    }




}
