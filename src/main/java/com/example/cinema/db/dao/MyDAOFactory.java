package com.example.cinema.db.dao;

import com.example.cinema.db.dao.implementation.*;

import java.sql.Connection;

public class MyDAOFactory implements DAOFactory {

    private static MyDAOFactory instance;
    private MyDAOFactory() {}

    public static synchronized MyDAOFactory getInstance() {
        if (instance == null) {
            instance = new MyDAOFactory();
        }
        return instance;
    }

    @Override
    public UserDAO getUserDAO(Connection connection) {
        return new UserDAO(connection);
    }
    @Override
    public FilmDAO getFilmDao(Connection connection) {
        return new FilmDAO(connection);
    }

    @Override
    public SessionDAO getSessionDao(Connection connection) {
        return new SessionDAO(connection);
    }

    @Override
    public TicketDAO getTicketDao(Connection connection) {
        return new TicketDAO(connection);
    }

    @Override
    public CommentDAO getCommentDao(Connection connection) {
        return new CommentDAO(connection);
    }

    @Override
    public GenreDAO getGenreDao(Connection connection) {
        return new GenreDAO(connection);
    }

    @Override
    public AgeRatingDAO getAgeRatingDao(Connection connection) {
        return new AgeRatingDAO(connection);
    }
}
