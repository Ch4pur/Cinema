package com.example.cinema.db.dao.implementation;

import com.example.cinema.db.dao.iface.GenreDAOInterface;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Genre;
import com.example.cinema.db.exception.DAOException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class GenreDAO extends AbstractDAO<Genre> implements GenreDAOInterface<Genre> {

    private static final Logger LOG = Logger.getLogger(GenreDAO.class);

    private static final String GET_BY_ID = "Select * From Genres Where Genre_id =?";
    private static final String GET_BY_NAME = "Select * From Genres Where Genre_name =?";
    private static final String GET_ALL = "Select * from Genres";

    private static final String ADD = "Insert Into Genres (Genre_Name) Values (?)";

    private static final String UPDATE_BY_ID = "Update Genres Set Genre_Name = ? Where Genre_id = ?";

    private static final String DELETE_BY_ID = "Delete From Genres Where Genre_id = ?";
    private static final String DELETE_BY_NAME = "Delete From Genres Where Genre_name =?";

    private static final String GET_SET_BY_FILM = "Select * From Genres " +
            "Where Genre_id in  (Select " +
                                "Genre_id From Films_genres, Films " +
                                "Where Films_genres.Film_id = Films.Film_id and Films.Film_id = ?)";

    private static final String ADD_GENRE_TO_FILM = "Insert Into Films_genres (Film_id,Genre_id) Value (?,?)";

    private static final String DELETE_GENRES_FROM_FILM = "Delete From Films_genres Where Film_id = ?";

    public GenreDAO(Connection connection) {
        super(connection);
        LOG.info("GenreDAO is created with connection -> " + connection);
    }

    @Override
    public Genre getById(int id) throws DAOException {
        LOG.info("Getting a genre by id -> " + id);
        return getByStatement(GET_BY_ID,
                String.valueOf(id));
    }

    @Override
    public Set<Genre> getAll() throws DAOException {
        LOG.info("Getting all genres");
        return new LinkedHashSet<>(getAllByStatement(GET_ALL));
    }

    @Override
    public void add(Genre genre) throws DAOException {
        LOG.info("Adding new genre -> " + genre);
        updateByStatement(ADD,
                genre.getName());
    }

    @Override
    public void update(Genre genre) throws DAOException {
        LOG.info("Updating the genre -> " + genre);
        updateByStatement(UPDATE_BY_ID,
                genre.getName(),
                String.valueOf(genre.getId()));
    }

    @Override
    public void deleteById(int id) throws DAOException {
        LOG.info("Deleting a genre by id -> " + id);
        updateByStatement(DELETE_BY_ID,
                String.valueOf(id));
    }


    @Override
    public Genre getByName(String name) throws DAOException {
        LOG.info("Getting a genre by name -> " + name);
        return getByStatement(GET_BY_NAME, name);
    }

    @Override
    public Set<Genre> getSetByFilm(Film film) throws DAOException {
        LOG.info("Getting a set of genres by film -> "+ film );
        return new LinkedHashSet<>(getAllByStatement(GET_SET_BY_FILM, String.valueOf(film.getId())));
    }

    @Override
    public void addGenreToFilm(Genre genre, Film film) throws DAOException {
        LOG.info("Adding genre " + genre + " to the film " + film);
        updateByStatement(ADD_GENRE_TO_FILM,
                String.valueOf(film.getId()),
                String.valueOf(genre.getId()));
    }

    @Override
    public void deleteByName(String name) throws DAOException {
        LOG.info("Deleting a genre by name -> " + name);
        updateByStatement(DELETE_BY_NAME,
                name);
    }

    @Override
    public void deleteGenresFromFilm(Film film) throws DAOException {
        LOG.info("Deleting all genres from film -> " + film);
        updateByStatement(DELETE_GENRES_FROM_FILM,
                String.valueOf(film.getId()));
    }

    @Override
    protected Genre createEntity(ResultSet resultSet) throws SQLException {
        return new Genre()
                .setName(resultSet.getString("Genre_name"))
                .setId(resultSet.getInt("Genre_id"));
    }

}
