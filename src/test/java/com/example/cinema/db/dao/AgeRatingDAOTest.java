package com.example.cinema.db.dao;

import com.example.cinema.db.dao.implementation.AgeRatingDAO;
import com.example.cinema.db.entity.AgeRating;
import com.example.cinema.db.exception.DAOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

public class AgeRatingDAOTest extends AbstractDAOTest{

    private AgeRatingDAO ageRatingDAO;
    private static final String CREATE_AGE_RATINGS_TABLE = "" +
            "create table Age_ratings\n" +
            "(\n" +
            "    Age_rating_id                     int primary key auto_increment,\n" +
            "    Age_rating_title                  varchar(15) unique,\n" +
            "    Minimum_allowed_age               TINYINT UNSIGNED,\n" +
            "    Possibility_of_going_with_parents bool,\n" +
            "\n" +
            "\n" +
            "    Check (Minimum_allowed_age <= 30 and Minimum_allowed_age > 10),\n" +
            "    unique (Minimum_allowed_age, Possibility_of_going_with_parents)\n" +
            ");";
    private static final String DROP_AGE_RATINGS = "Drop table Age_ratings";

    @Before
    public void createTables() throws SQLException {
        super.createTables(CREATE_AGE_RATINGS_TABLE);
        ageRatingDAO = factory.getAgeRatingDao(connection);
    }

    @Test
    public void ageRatingDAO_getRatingByTitle() throws DAOException {
        AgeRatingDAO ageRatingDAO = factory.getAgeRatingDao(connection);
        AgeRating ageRating = new AgeRating().setMinAge((short) 12).setTitle("test").setWithParents(false);

        ageRatingDAO.add(ageRating);

        AgeRating actual = ageRatingDAO.getByTitle(ageRating.getTitle());

        Assert.assertEquals(ageRating,actual);
    }

    @Test
    public void ageRatingDAO_getRatingById_FromEmptyDB() throws DAOException {
        AgeRating actual = ageRatingDAO.getById(1);

        Assert.assertNull(actual);
    }
    @Test
    public void ageRatingDAO_getRatingById() throws DAOException {
        AgeRating expected = new AgeRating().setTitle("12").setMinAge((short) 12).setWithParents(false);

        ageRatingDAO.add(expected);
        AgeRating actual = ageRatingDAO.getById(1);

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void ageRatingDAO_addRating() throws DAOException {
        AgeRating firstRating = new AgeRating().setTitle("12").setMinAge((short) 12).setWithParents(false);
        AgeRating secondRating = new AgeRating().setTitle("122").setMinAge((short) 16).setWithParents(false);

        ageRatingDAO.add(firstRating);
        ageRatingDAO.add(secondRating);
        AgeRating actual = ageRatingDAO.getById(2);

        Assert.assertEquals(secondRating,actual);
    }
    @Test(expected = DAOException.class)
    public void ageRatingDAO_addTheSameRating_Twice() throws DAOException {
        AgeRating firstRating = new AgeRating().setTitle("12").setMinAge((short) 12).setWithParents(false);

        ageRatingDAO.add(firstRating);
        ageRatingDAO.add(firstRating);
    }

    @Test
    public void ageRatingDAO_update() throws DAOException {
        AgeRating ageRating = new AgeRating().setTitle("12").setMinAge((short) 12).setWithParents(false);

        ageRatingDAO.add(ageRating);
        ageRating = ageRatingDAO.getByTitle(ageRating.getTitle());
        ageRating.setMinAge((short) 15);
        ageRatingDAO.update(ageRating);
        AgeRating actual = ageRatingDAO.getByTitle(ageRating.getTitle());

        Assert.assertEquals(ageRating,actual);
    }

    @Test
    public void ageRatingDAO_delete() throws DAOException {
        AgeRating ageRating = new AgeRating().setTitle("12").setMinAge((short) 12).setWithParents(false);

        ageRatingDAO.add(ageRating);
        ageRatingDAO.deleteById(1);
        AgeRating actual = ageRatingDAO.getById(1);

        Assert.assertNull(actual);
    }

    @Test
    public void ageRatingDAO_getAll() throws DAOException {
        AgeRating firstRating = new AgeRating().setTitle("12").setMinAge((short) 12).setWithParents(false);
        AgeRating secondRating = new AgeRating().setTitle("122").setMinAge((short) 13).setWithParents(false);
        Set<AgeRating> expected = new LinkedHashSet<>();

        expected.add(firstRating);
        expected.add(secondRating);
        ageRatingDAO.add(firstRating);
        ageRatingDAO.add(secondRating);
        Set<AgeRating> actual = ageRatingDAO.getAll();

        Assert.assertEquals(expected,actual);
    }


    @After
    public void clearTables() {
        super.clearTables(DROP_AGE_RATINGS);
    }
}
