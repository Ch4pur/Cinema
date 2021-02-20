package com.example.cinema.db.dao.iface;

import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Genre;
import com.example.cinema.db.exception.DAOException;

import java.util.Set;

public interface GenreDAOInterface<T extends Genre> extends DAOInterface<T> {
    T getByName(String name) throws DAOException;

    Set<T> getSetByFilm(Film film) throws DAOException;

    void addGenreToFilm(Genre genre, Film film) throws DAOException;

    void deleteByName(String name) throws DAOException;

    void deleteGenresFromFilm(Film film) throws DAOException;
}
