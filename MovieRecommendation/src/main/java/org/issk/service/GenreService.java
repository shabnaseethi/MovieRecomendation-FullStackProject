package org.issk.service;

import org.issk.dto.Genre;
import java.util.List;

public interface GenreService {

    List<Genre> getGenres();

    void populateGenres();
}
