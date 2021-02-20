package com.example.cinema.db.dao;

import com.example.cinema.db.dao.implementation.*;

import java.sql.Connection;

public interface DAOFactory {
    UserDAO getUserDAO(Connection connection);
    FilmDAO getFilmDao(Connection connection);
    SessionDAO getSessionDao(Connection connection);
    TicketDAO getTicketDao(Connection connection);
    CommentDAO getCommentDao(Connection connection);
    GenreDAO getGenreDao(Connection connection);
    AgeRatingDAO getAgeRatingDao(Connection connection);
}
