package com.example.cinema.db.dao.implementation;

import com.example.cinema.db.dao.iface.AgeRatingDAOInterface;
import com.example.cinema.db.entity.AgeRating;
import com.example.cinema.db.exception.DAOException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class AgeRatingDAO extends AbstractDAO<AgeRating> implements AgeRatingDAOInterface<AgeRating> {


    private static final Logger LOG = Logger.getLogger(AgeRatingDAO.class);

    private static final String GET_BY_ID = "Select * From Age_ratings where age_rating_id = ?";
    private static final String GET_ALL = "Select * From Age_ratings ";
    private static final String GET_BY_TITLE = "Select * From Age_ratings where age_rating_title = ?";

    private static final String ADD_AGE_RATING = "insert into age_ratings (Age_rating_title, Minimum_allowed_age, Possibility_of_going_with_parents) Value (?,?,?)";

    private static final String UPDATE = "Update age_ratings set Age_rating_title = ?, Minimum_allowed_age = ?, Possibility_of_going_with_parents = ? Where age_rating_id = ?";
    private static final String DELETE_BY_ID = "Delete From Age_ratings Where age_rating_id = ?";


    public AgeRatingDAO(Connection connection) {
        super(connection);
        LOG.info("Creating ageRatingDao with connection ->" + connection);
    }

    @Override
    public AgeRating getById(int id) throws DAOException {
        LOG.info("Getting age rating with id ->" + id);
        return getByStatement(GET_BY_ID, String.valueOf(id));
    }

    @Override
    public Set<AgeRating> getAll() throws DAOException {
        LOG.info("Getting all age ratings");
        return new LinkedHashSet<>(getAllByStatement(GET_ALL));
    }

    @Override
    public void add(AgeRating ageRating) throws DAOException {
        LOG.info("Adding new age rating -> " + ageRating);
        updateByStatement(ADD_AGE_RATING,
                ageRating.getTitle(),
                String.valueOf(ageRating.getMinAge()),
                ageRating.isWithParents()? "1" : "0");
    }

    @Override
    public void update(AgeRating ageRating) throws DAOException {
        LOG.info("Updating age rating with id -> " +ageRating.getId() + " to " + ageRating);
        updateByStatement(UPDATE,
                ageRating.getTitle(),
                String.valueOf(ageRating.getMinAge()),
                ageRating.isWithParents()? "1" : "0",
                String.valueOf(ageRating.getId()));
    }

    @Override
    public void deleteById(int id) throws DAOException {
        LOG.info("Deleting age rating with id -> " + id);
        updateByStatement(DELETE_BY_ID,
                String.valueOf(id));
    }

    @Override
    protected AgeRating createEntity(ResultSet resultSet) throws SQLException {
        return new AgeRating()
                .setTitle(resultSet.getString("Age_rating_title"))
                .setMinAge(resultSet.getShort("Minimum_allowed_age"))
                .setWithParents(resultSet.getBoolean("Possibility_of_going_with_parents"))
                .setId(resultSet.getInt("age_rating_id"));
    }

    @Override
    public AgeRating getByTitle(String title) throws DAOException {
        return getByStatement(GET_BY_TITLE,
                title);
    }
}
