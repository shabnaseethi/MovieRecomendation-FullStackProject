package org.issk.controller;

import org.issk.dto.Genre;
import org.issk.dto.User;
import org.issk.service.GenreServiceImpl;
import org.issk.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/genre")
@CrossOrigin(origins = "http://localhost:3000")
public class GenreController {
    @Autowired
    GenreServiceImpl genreService;

    @GetMapping(value="/refreshdb", consumes= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String refreshDB(){
        return "Genres refreshed";
    }

    @GetMapping(value="/get", consumes= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<Integer, Genre> getAll(){
        return genreService.getGenres();
    }
}



