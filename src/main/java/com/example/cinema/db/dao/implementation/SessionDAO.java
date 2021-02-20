package com.example.cinema.db.dao.implementation;

import com.example.cinema.db.dao.iface.SessionDAOInterface;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Session;
import com.example.cinema.db.exception.DAOException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class SessionDAO extends AbstractDAO<Session> implements SessionDAOInterface<Session> {

    private static final Logger LOG = Logger.getLogger(SessionDAO.class);

    private static final String GET_BY_ID = "Select * From Sessions Where Session_id = ?";
    private static final String GET_ALL = "Select * From Sessions";

    private static final String ADD = "Insert into Sessions(Session_full_date, ThreeD_Supporting, Film_id) Value (?,?,?)";

    private static final String UPDATE = "Update Sessions Set Session_full_date = ?, ThreeD_Supporting = ?, Film_id = ?" +
            "Where Session_id =?";

    private static final String DELETE_BY_ID = "Delete From Sessions Where Session_id = ?";
    private static final String GET_SET_BY_FILM = "Select * From Sessions Where Film_id = ?";
    private static final String GET_SET_BY_DATE = "Select * From Sessions Where Date(Session_full_date) = Date(?)";

    private static final String GET_SET_BY_3D_SUPPORTING = "Select * From Sessions Where ThreeD_Supporting = ?";

    private static final String GET_FILM_ID_BY_ID = "Select Film_id From Sessions Where Session_id =?";

    public SessionDAO(Connection connection) {
        super(connection);
        LOG.info("SessionDAO is created with connection -> " + connection);
    }

    @Override
    public Session getById(int id) throws DAOException {
        LOG.info("Getting a session by id -> " + id);
        return getByStatement(GET_BY_ID,
                String.valueOf(id));
    }

    @Override
    public Set<Session> getAll() throws DAOException {
        LOG.info("Getting all sessions");
        return new LinkedHashSet<>(getAllByStatement(GET_ALL));
    }
    @Override
    public void add(Session session) throws DAOException {
        LOG.info("Adding new session -> " + session);
        try {
            updateByStatement(ADD,
                    session.getFullDate().toString(),
                    session.isThreeDSupporting() ? "1" : "0",
                    String.valueOf(session.getFilm().getId()));
        } catch (DAOException e) {
            throw new DAOException("This session is already exists",e);
        }
    }

    @Override
    public void update(Session session) throws DAOException {
        LOG.info("Updating the session -> " + session);
        updateByStatement(UPDATE,
                session.getFullDate().toString(),
                session.isThreeDSupporting() ? "1" : "0",
                String.valueOf(session.getFilm().getId()),
                String.valueOf(session.getId()));
    }

    @Override
    public void deleteById(int id) throws DAOException {
        LOG.info("Deleting the session by id -> " + id);
        updateByStatement(DELETE_BY_ID,
                String.valueOf(id));
    }


    @Override
    public Set<Session> getSetByFilm(Film film) throws DAOException {
        LOG.info("Getting a set of session by the film -> " + film);
        Set<Session> sessions = new LinkedHashSet<>(getAllByStatement(GET_SET_BY_FILM,
                String.valueOf(film.getId())));
        sessions.forEach(session -> session.setFilm(film));
        return sessions;
    }

    @Override
    public Set<Session> getSetByDate(Date date) throws DAOException {
        LOG.info("Getting a genre by date -> " + date);
        Set<Session> sessions = new TreeSet<>(Comparator.comparing(Session::getFullDate));
        sessions.addAll(getAllByStatement(GET_SET_BY_DATE,
                        date.toString()));
        return sessions;
    }

    @Override
    public Set<Session> getSetBy3DSupporting(boolean threeDSupporting) throws DAOException {
        LOG.info("Getting a set of sessions by 3d supporting");
        return new LinkedHashSet<>(getAllByStatement(GET_SET_BY_3D_SUPPORTING,
                threeDSupporting ? "1" : "0"));
    }

    @Override
    public int getFilmIdById(int id) throws DAOException {
        LOG.info("Getting a film id by sessions id -> " + id);
        return getForeignKeyId(GET_FILM_ID_BY_ID,
                id);
    }

    @Override
    protected Session createEntity(ResultSet resultSet) throws SQLException {
        return new Session().setFullDate(resultSet.getTimestamp("Session_full_date"))
                .setThreeDSupporting(resultSet.getBoolean("ThreeD_Supporting"))
                .setId(resultSet.getInt("Session_id"))
                .setNumberOfFreeSeats(resultSet.getInt("Number_of_free_seats"));
    }
}
