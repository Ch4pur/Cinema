package com.example.cinema.db.dao.iface;

import com.example.cinema.db.entity.AgeRating;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Genre;
import com.example.cinema.db.exception.DAOException;

import java.util.Set;

public interface FilmDAOInterface<T extends Film> extends DAOInterface<T> {
    T getByTitle(String title) throws DAOException;

    Set<T> getSetByGenre(Genre genre) throws DAOException;

    int getAgeRatingIdById(int id) throws DAOException;
}
