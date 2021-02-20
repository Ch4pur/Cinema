package com.example.cinema.db.dao;

import com.example.cinema.db.dao.implementation.AgeRatingDAO;
import com.example.cinema.db.dao.implementation.FilmDAO;
import com.example.cinema.db.dao.implementation.SessionDAO;
import com.example.cinema.db.entity.AgeRating;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Session;
import com.example.cinema.db.exception.DAOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

public class SessionDAOTest extends AbstractDAOTest {

    private static final String CREATE_AGE_RATINGS_TABLE = "" +
            "create table if not exists Age_ratings\n" +
            "(\n" +
            "    Age_rating_id                     int primary key auto_increment,\n" +
            "    Age_rating_title                  varchar(15) unique not null,\n" +
            "    Minimum_allowed_age               TINYINT UNSIGNED not null,\n" +
            "    Possibility_of_going_with_parents bool,\n" +
            "\n" +
            "\n" +
            "    Check (Minimum_allowed_age <= 30 and Minimum_allowed_age > 10),\n" +
            "    unique (Minimum_allowed_age, Possibility_of_going_with_parents)\n" +
            ");";

    private static final String CREATE_FILMS_TABLE = "" +
            "create table if not exists Films\n" +
            "(\n" +
            "    Film_id       int primary key auto_increment,\n" +
            "    Film_name     varchar(50) not null unique,\n" +
            "    Producer      varchar(20) not null,\n" +
            "    Age_rating_id int         not null,\n" +
            "    Film_image    varchar(40) unique,\n" +
            "    Duration      time        not null,\n" +
            "    Release_date  date        not null,\n" +
            "    description   text        not null,\n" +
            "    Foreign key (Age_rating_id) references Age_ratings (Age_rating_id),\n" +
            "    Unique (Film_name, Producer, Release_date)\n" +
            ");";

    private static final String CREATE_SESSIONS_TABLE = "" +
            "create table if not exists Sessions\n" +
            "(\n" +
            "    Session_id           int primary key auto_increment,\n" +
            "    Film_id              int             not null,\n" +
            "    Session_full_date    datetime unique not null,\n" +
            "    ThreeD_Supporting    boolean         not null default false,\n" +
            "    Number_of_free_seats int unsigned default 64 not null,\n" +
            "\n" +
            "    Foreign Key (Film_id) references Films (Film_id) on delete cascade,\n" +
            "    check ( Number_of_free_seats >= 0 and Number_of_free_seats <= 64 )\n" +
            ");";


    private static final String DROP_SESSIONS = "Drop table Sessions";
    private static final String DROP_AGE_RATINGS = "Drop table Age_ratings";
    private static final String DROP_FILMS = "Drop table Films";


    private AgeRatingDAO ageRatingDAO;
    private FilmDAO filmDAO;
    private SessionDAO sessionDAO;

    @Before
    public void createTables() throws SQLException {
        super.createTables(
                CREATE_AGE_RATINGS_TABLE,
                CREATE_FILMS_TABLE,
                CREATE_SESSIONS_TABLE
        );

        ageRatingDAO = factory.getAgeRatingDao(connection);
        filmDAO = factory.getFilmDao(connection);
        sessionDAO = factory.getSessionDao(connection);
    }

    private void createSupportingTables(AgeRating ageRating, Film film) throws DAOException {
        ageRating.setTitle("AgeRating")
                .setMinAge((short) 12)
                .setWithParents(false)
                .setId(1);
        ageRatingDAO.add(ageRating);
        film.setProducersName("Producer")
                .setTitle("Film")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(1);
        filmDAO.add(film);
    }

    @Test
    public void sessionDAO_get_fromEmptyDB() throws DAOException {
        Session session = sessionDAO.getById(1);
        Assert.assertNull(session);
    }

    @Test
    public void sessionDAO_get_ById() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        createSupportingTables(ageRating, film);
        Session firstSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);
        Session secondSession = new Session()
                .setFullDate(Timestamp.valueOf("2001-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(2);

        sessionDAO.add(firstSession);
        sessionDAO.add(secondSession);

        Session actual = sessionDAO.getById(2);

        Assert.assertEquals(secondSession, actual);
    }

    @Test
    public void sessionDAO_add() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        createSupportingTables(ageRating, film);
        Session firstSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);

        System.out.println(firstSession);
        sessionDAO.add(firstSession);

        Session actual = sessionDAO.getById(1);

        Assert.assertEquals(firstSession, actual);
    }

    @Test(expected = DAOException.class)
    public void sessionDAO_add_twice() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        createSupportingTables(ageRating, film);
        Session firstSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);

        sessionDAO.add(firstSession);
        sessionDAO.add(firstSession);
    }

    @Test
    public void sessionDAO_update() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        createSupportingTables(ageRating, film);
        Session firstSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);

        sessionDAO.add(firstSession);
        firstSession.setThreeDSupporting(true);
        sessionDAO.update(firstSession);
        Session actual = sessionDAO.getById(1);

        Assert.assertEquals(firstSession, actual);
    }

    @Test(expected = DAOException.class)
    public void sessionDAO_update_toExistingDate() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        createSupportingTables(ageRating, film);
        Session firstSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);
        Session secondSession = new Session()
                .setFullDate(Timestamp.valueOf("2001-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(2);

        sessionDAO.add(firstSession);
        sessionDAO.add(secondSession);
        firstSession.setFullDate(secondSession.getFullDate());
        sessionDAO.update(firstSession);
    }

    @Test
    public void sessionDAO_delete() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        createSupportingTables(ageRating, film);
        Session firstSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);

        sessionDAO.add(firstSession);
        firstSession.setThreeDSupporting(true);
        sessionDAO.deleteById(firstSession.getId());
        Session actual = sessionDAO.getById(1);

        Assert.assertNull(actual);
    }

    @Test
    public void sessionDAO_get_SetByFilm() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        createSupportingTables(ageRating, film);
        Session firstSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);
        Session secondSession = new Session()
                .setFullDate(Timestamp.valueOf("2001-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(2);
        Set<Session> expected = new LinkedHashSet<>();

        sessionDAO.add(firstSession);
        sessionDAO.add(secondSession);
        expected.add(firstSession);
        expected.add(secondSession);

        Set<Session> actual = sessionDAO.getSetByFilm(film);
        actual.forEach(session -> session.setFilm(film));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void sessionDAO_get_setByDate() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        createSupportingTables(ageRating, film);
        Session firstSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);
        Session secondSession = new Session()
                .setFullDate(Timestamp.valueOf("2001-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(2);
        Session thirdSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 03:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(3);
        Set<Session> expected = new LinkedHashSet<>();

        sessionDAO.add(firstSession);
        sessionDAO.add(secondSession);
        sessionDAO.add(thirdSession);

        expected.add(firstSession);
        expected.add(thirdSession);

        Set<Session> actual = sessionDAO.getSetByDate(firstSession.getDate());
        actual.forEach(session -> session.setFilm(film));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void sessionDAO_get_setByThreeD() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        createSupportingTables(ageRating, film);
        Session firstSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);
        Session secondSession = new Session()
                .setFullDate(Timestamp.valueOf("2001-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(true)
                .setId(2);
        Session thirdSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 03:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(3);
        Set<Session> expected = new LinkedHashSet<>();

        sessionDAO.add(firstSession);
        sessionDAO.add(secondSession);
        sessionDAO.add(thirdSession);

        expected.add(firstSession);
        expected.add(thirdSession);

        Set<Session> actual = sessionDAO.getSetBy3DSupporting(false);
        actual.forEach(session -> session.setFilm(film));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void sessionDAO_get_filmId() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        createSupportingTables(ageRating, film);
        Session firstSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);

        sessionDAO.add(firstSession);
        Film actual = filmDAO.getById(sessionDAO.getFilmIdById(firstSession.getId()));

        Assert.assertEquals(film, actual);
    }

    @Test
    public void sessionDAO_get_all() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        createSupportingTables(ageRating, film);
        Session firstSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);
        Session secondSession = new Session()
                .setFullDate(Timestamp.valueOf("2001-01-01 01:01:00"))
                .setFilm(film)
                .setThreeDSupporting(true)
                .setId(2);
        Session thirdSession = new Session()
                .setFullDate(Timestamp.valueOf("2000-01-01 03:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(3);
        Set<Session> expected = new LinkedHashSet<>();

        sessionDAO.add(firstSession);
        sessionDAO.add(secondSession);
        sessionDAO.add(thirdSession);

        expected.add(firstSession);
        expected.add(secondSession);
        expected.add(thirdSession);

        Set<Session> actual = sessionDAO.getAll();
        actual.forEach(session -> session.setFilm(film));
        Assert.assertEquals(expected, actual);
    }
    @After
    public void clearTables() {
        super.clearTables(
                DROP_SESSIONS,
                DROP_FILMS,
                DROP_AGE_RATINGS
        );
    }
}
