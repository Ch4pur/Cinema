package com.example.cinema.db.dao.implementation;

import com.example.cinema.db.dao.iface.FilmDAOInterface;
import com.example.cinema.db.entity.AgeRating;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Genre;
import com.example.cinema.db.exception.DAOException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class FilmDAO extends AbstractDAO<Film> implements FilmDAOInterface<Film> {

    private static final Logger LOG = Logger.getLogger(FilmDAO.class);

    private static final String GET_BY_ID = "Select * From Films Where Film_id = ?";
    private static final String GET_BY_TITLE = "Select * From Films Where Film_name = ?";

    private static final String GET_ALL = "Select * From Films";

    private static final String GET_SET_BY_GENRE = "Select * From Films " +
            "Where Film_id in (Select Film_id " +
                            "From Genres, Films_genres " +
                            "Where Genres.Genre_id = Films_genres.Genre_id and Genres.Genre_id = ?)";
    private static final String GET_SET_BY_AGE_RATING = "Select * From Films Where Age_rating_id = ?";
    private static final String GET_SET_BY_PRODUCER = "Select * From Films Where Producer =?";
    private static final String ADD = "Insert Into " +
            "Films (Film_name,Producer,Age_rating_id,Film_image,Duration,Release_date,Description)" +
            "Value (?,?,?,?,?,?,?)";

    private static final String GET_AGE_RATING_ID_BY_ID = "Select age_rating_id From Films Where Film_id = ?";
    private static final String UPDATE = "Update Films " +
            "Set Film_name = ?,Producer = ?,Age_rating_id = ?,Film_image =?,Duration= ?,Release_date = ?,Description = ?" +
            "Where Film_id = ?";

    private static final String DELETE_BY_ID = "Delete From Films Where Film_id = ?";

    public FilmDAO(Connection connection) {
        super(connection);
        LOG.info("FilmDAO is created with connection -> " + connection);
    }

    @Override
    public Film getById(int id) throws DAOException {
        LOG.info("Getting a film by id -> " + id);
        return getByStatement(GET_BY_ID,
                String.valueOf(id));
    }

    @Override
    public Set<Film> getAll() throws DAOException {
        LOG.info("Getting all films");
        return new HashSet<>(getAllByStatement(GET_ALL));
    }

    @Override
    public void add(Film film) throws DAOException {
        LOG.info("Adding new film -> " + film);
        updateByStatement(ADD,
                film.getTitle(),
                film.getProducersName(),
                String.valueOf(film.getAgeRating().getId()),
                film.getImagePath(),
                film.getDuration().toString(),
                film.getReleaseDate().toString(),
                film.getDescription());
    }

    @Override
    public void update(Film film) throws DAOException {
        LOG.info("Updating the film -> " + film);
        updateByStatement(UPDATE,
                film.getTitle(),
                film.getProducersName(),
                String.valueOf(film.getAgeRating().getId()),
                film.getImagePath(),
                film.getDuration().toString(),
                film.getReleaseDate().toString(),
                film.getDescription(),
                String.valueOf(film.getId()));
    }

    @Override
    public void deleteById(int id) throws DAOException {
        LOG.info("Deleting a film by id -> " + id);
        updateByStatement(DELETE_BY_ID,
                String.valueOf(id));
    }


    @Override
    public Film getByTitle(String title) throws DAOException {
        LOG.info("Getting a film by title -> " + title);
        return getByStatement(GET_BY_TITLE,
                title);
    }

    @Override
    public Set<Film> getSetByGenre(Genre genre) throws DAOException {
        LOG.info("Getting a set of films by genre " + genre);
        return new LinkedHashSet<>(getAllByStatement(GET_SET_BY_GENRE, String.valueOf(genre.getId())));
    }

    @Override
    public int getAgeRatingIdById(int id) throws DAOException {
        LOG.info("Getting age rating id by film id -> " + id);
        return getForeignKeyId(GET_AGE_RATING_ID_BY_ID, id);
    }

    @Override
    protected Film createEntity(ResultSet resultSet) throws SQLException {
        return new Film().setTitle(resultSet.getString("Film_name"))
                .setProducersName(resultSet.getString("Producer"))
                .setImagePath(resultSet.getString("Film_image"))
                .setDuration(resultSet.getTime("Duration"))
                .setReleaseDate(resultSet.getDate("Release_date"))
                .setDescription(resultSet.getString("Description"))
                .setId(resultSet.getInt("Film_id"));
    }
}
