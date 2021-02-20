package com.example.cinema.db.dao.iface;

import com.example.cinema.db.entity.Comment;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.DAOException;

import java.util.Set;

public interface CommentDAOInterface<T extends Comment> extends DAOInterface<T> {
    Set<T> getSetBySender(User sender) throws DAOException;
    Set<T> getSetByFilm(Film film) throws DAOException;
    Set<T> getSetByFilm(Film film, int startWith, int pageSize) throws DAOException;
    int getAuthorId(int id) throws DAOException;
    int getFilmId(int id ) throws DAOException;
}
