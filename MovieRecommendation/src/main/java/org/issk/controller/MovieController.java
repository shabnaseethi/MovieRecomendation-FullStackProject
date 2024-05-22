package org.issk.controller;

import org.issk.dto.Movie;
import org.issk.exceptions.GenreNotFoundException;
import org.issk.exceptions.InvalidRatingsException;
import org.issk.service.MovieService;
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
    public MovieController(MovieService movieService){
        this.movieService= movieService;
    }
    @GetMapping("/genre/{genre}")
    public List<Movie>  getMoviesByGenre(@PathVariable String genre) throws GenreNotFoundException {
        return movieService.getMoviesByGenre(genre);
    }

    @GetMapping("/rating/{ratingfrom}/{ratingto}/{sort}")
    public List<Movie>  getMoviesByRating(@PathVariable String ratingfrom,@PathVariable String ratingto, @PathVariable String sort) throws InvalidRatingsException {
        return movieService.getMoviesByRating(ratingfrom,ratingto,sort.equals("1"));
    }

    @GetMapping("/preferences/{userid}")
    public List<Movie>  getMoviesByPreferences(@PathVariable String userid) throws GenreNotFoundException {
        return movieService.getMoviesByPreferences(userid);
    }

    @GetMapping("/moviename")
    public List<Movie> getMoviesByName(@RequestParam String name) {
        return movieService.getMoviesByName(name);
    }




}
