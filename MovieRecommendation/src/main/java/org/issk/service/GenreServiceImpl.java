package org.issk.service;

import org.issk.dao.GenreDao;
import org.issk.dto.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class GenreServiceImpl implements GenreService{

    @Autowired
    GenreDao genreDao;

    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public HashMap<Integer, Genre> getGenres() {
        return genreDao.getGenresFromDB();
    }

    @Override
    public int populateGenres() {

        return genreDao.populateGenres() ? 200 : 500;
    }
}
