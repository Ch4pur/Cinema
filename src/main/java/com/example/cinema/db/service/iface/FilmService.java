package com.example.cinema.db.service.iface;

import com.example.cinema.db.entity.AgeRating;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Genre;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;

import java.util.Set;

public interface FilmService {
    Set<Film> getAll() throws ServiceException, TransactionException;

    Film getFilmByTitle(String name) throws ServiceException, TransactionException;

    Film getFilmById(int id) throws ServiceException, TransactionException;

    AgeRating getAgeRatingById(int id) throws ServiceException, TransactionException;

    Set<Genre> getAllGenres() throws ServiceException, TransactionException;

    void addFilm(Film film) throws ServiceException, TransactionException;

    void updateFilm(Film film) throws ServiceException, TransactionException;

    void deleteFilm(Film film) throws ServiceException, TransactionException;

    void setGenresToFilm(Film film, Set<Genre> genres) throws ServiceException, TransactionException;

    Set<AgeRating> getAllAgeRatings() throws ServiceException, TransactionException;

    AgeRating getAgeRatingByTitle(String title) throws ServiceException, TransactionException;

}
