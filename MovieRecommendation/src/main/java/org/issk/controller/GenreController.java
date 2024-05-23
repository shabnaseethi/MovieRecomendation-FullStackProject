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

    @GetMapping(value="/refreshdb")
    @ResponseBody
    public int refreshDB(){
        return genreService.populateGenres();
    }

    @GetMapping(value="/get")
    @ResponseBody
    public HashMap<Integer, Genre> getAll(){
        return genreService.getGenres();
    }
}



