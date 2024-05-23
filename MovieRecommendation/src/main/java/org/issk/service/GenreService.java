package org.issk.service;

import org.issk.dto.Genre;

import java.util.HashMap;
import java.util.List;

public interface GenreService {

    HashMap<Integer, Genre> getGenres();

    int populateGenres();
}
