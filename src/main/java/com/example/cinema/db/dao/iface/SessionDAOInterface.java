package com.example.cinema.db.dao.iface;


import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Session;
import com.example.cinema.db.exception.DAOException;

import java.sql.Date;
import java.util.Set;

public interface SessionDAOInterface<T extends Session> extends DAOInterface<T> {
    Set<T> getSetByFilm(Film film) throws DAOException;
    Set<T> getSetByDate(Date date) throws DAOException;
    Set<T> getSetBy3DSupporting(boolean threeDSupporting) throws DAOException;

    int getFilmIdById(int id) throws DAOException;
}
