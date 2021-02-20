package com.example.cinema.db.dao;

import com.example.cinema.db.dao.implementation.AgeRatingDAO;
import com.example.cinema.db.dao.implementation.FilmDAO;
import com.example.cinema.db.dao.implementation.GenreDAO;
import com.example.cinema.db.entity.AgeRating;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Genre;
import com.example.cinema.db.exception.DAOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.LinkedHashSet;
import java.util.Set;

public class FilmDAOTest extends AbstractDAOTest {

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
    private static final String CREATE_GENRES_TABLE = "" +
            "create table if not exists Genres\n" +
            "(\n" +
            "    Genre_id   int primary key auto_increment,\n" +
            "    Genre_name varchar(20) unique not null\n" +
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

    private static final String CREATE_FILMS_GENRES_TABLE = "" +
            "create table if not exists Films_Genres\n" +
            "(\n" +
            "    Film_id  int,\n" +
            "    Genre_id int references Genres (Genre_id) on delete cascade,\n" +
            "\n" +
            "    Foreign key (Film_id) references Films (Film_id) on delete cascade,\n" +
            "    Foreign key (Genre_id) references Genres (Genre_id) on delete cascade,\n" +
            "    Primary key (Film_id, Genre_id)\n" +
            ");";

    private static final String DROP_FILMS_GENRES = "Drop table Films_genres";
    private static final String DROP_GENRES = "Drop table Genres";
    private static final String DROP_AGE_RATINGS = "Drop table Age_ratings";
    private static final String DROP_FILMS = "Drop table Films";

    private FilmDAO filmDAO;
    private AgeRatingDAO ageRatingDAO;
    private GenreDAO genreDAO;

    private void createSupportingTables(AgeRating ageRating) throws DAOException {
        ageRating.setTitle("AgeRating")
                .setMinAge((short) 12)
                .setWithParents(false)
                .setId(1);
        ageRatingDAO.add(ageRating);
    }
    @Before
    public void createTables() throws SQLException {
        super.createTables(
                CREATE_AGE_RATINGS_TABLE,
                CREATE_GENRES_TABLE,
                CREATE_FILMS_TABLE,
                CREATE_FILMS_GENRES_TABLE
        );

        ageRatingDAO = factory.getAgeRatingDao(connection);
        filmDAO = factory.getFilmDao(connection);
        genreDAO = factory.getGenreDao(connection);
    }
    @Test
    public void filmDAO_get_FromEmptyDB() throws DAOException {
        Film film = filmDAO.getById(1);
        Assert.assertNull(film);
    }

    @Test
    public void filmDAO_get_ById() throws DAOException {
        AgeRating ageRating = new AgeRating();
        createSupportingTables(ageRating);
        Film firstFilm = new Film()
                .setProducersName("Producer")
                .setTitle("Film")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(1);
        Film secondFilm = new Film()
                .setProducersName("Producer2")
                .setTitle("Film2")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(2);
        filmDAO.add(firstFilm);
        filmDAO.add(secondFilm);
        Film actual = filmDAO.getById(2);

        Assert.assertEquals(secondFilm,actual);
    }

    @Test
    public void filmDAO_get_ByTitle() throws DAOException {
        AgeRating ageRating = new AgeRating();
        createSupportingTables(ageRating);
        Film firstFilm = new Film()
                .setProducersName("Producer")
                .setTitle("Film")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(1);
        Film secondFilm = new Film()
                .setProducersName("Producer2")
                .setTitle("Film2")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(2);
        filmDAO.add(firstFilm);
        filmDAO.add(secondFilm);
        Film actual = filmDAO.getByTitle(secondFilm.getTitle()).setAgeRating(ageRating);

        Assert.assertEquals(secondFilm,actual);
    }

    @Test
    public void filmDAO_add() throws DAOException {
        AgeRating ageRating = new AgeRating();
        createSupportingTables(ageRating);
        Film expected = new Film()
                .setProducersName("Producer")
                .setTitle("Film")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(1);

        filmDAO.add(expected);
        Film actual = filmDAO.getByTitle(expected.getTitle());

        Assert.assertEquals(expected,actual);
    }
    @Test(expected = DAOException.class)
    public void filmDAO_add_existingFilm() throws DAOException {
        AgeRating ageRating = new AgeRating();
        createSupportingTables(ageRating);
        Film expected = new Film()
                .setProducersName("Producer")
                .setTitle("Film")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(1);

        filmDAO.add(expected);
        filmDAO.add(expected);
    }
    @Test
    public void filmDAO_update() throws DAOException {
        AgeRating ageRating = new AgeRating();
        createSupportingTables(ageRating);
        Film expected = new Film()
                .setProducersName("Producer")
                .setTitle("Film")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(1);

        filmDAO.add(expected);
        expected.setProducersName("Pr2");
        filmDAO.update(expected);
        Film actual = filmDAO.getByTitle(expected.getTitle());

        Assert.assertEquals(expected,actual);
    }

    @Test(expected = DAOException.class)
    public void filmDAO_update_toExistingProducer() throws DAOException {
        AgeRating ageRating = new AgeRating();
        createSupportingTables(ageRating);
        Film firstFilm = new Film()
                .setProducersName("Producer")
                .setTitle("Film")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(1);
        Film secondFilm = new Film()
                .setProducersName("Producer2")
                .setTitle("Film2")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(2);

        filmDAO.add(firstFilm);
        filmDAO.add(secondFilm);
        firstFilm.setTitle(secondFilm.getTitle());
        filmDAO.update(firstFilm);
    }

    @Test
    public void filmDAO_delete() throws DAOException {
        AgeRating ageRating = new AgeRating();
        createSupportingTables(ageRating);
        Film expected = new Film()
                .setProducersName("Producer")
                .setTitle("Film")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(1);

        filmDAO.add(expected);

        filmDAO.deleteById(expected.getId());
        Film actual = filmDAO.getByTitle(expected.getTitle());

        Assert.assertNull(actual);
    }

    @Test
    public void filmDAO_get_AgeRatingId() throws DAOException {
        AgeRating ageRating = new AgeRating();
        createSupportingTables(ageRating);
        Film expected = new Film()
                .setProducersName("Producer")
                .setTitle("Film")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(1);

        filmDAO.add(expected);

        AgeRating actual = ageRatingDAO.getById(filmDAO.getAgeRatingIdById(expected.getId()));

        Assert.assertEquals(ageRating, actual);
    }

    @Test
    public void filmDAO_get_SetByGenre() throws DAOException {
        AgeRating ageRating = new AgeRating();
        createSupportingTables(ageRating);
        Film firstFilm = new Film()
                .setProducersName("Producer")
                .setTitle("Film")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(1);
        Film secondFilm = new Film()
                .setProducersName("Producer2")
                .setTitle("Film2")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(2);

        Genre genre = new Genre()
                .setName("genre")
                .setId(1);
        Set<Film> expected = new LinkedHashSet<>();
        filmDAO.add(firstFilm);
        filmDAO.add(secondFilm);
        genreDAO.add(genre);
        genreDAO.addGenreToFilm(genre,firstFilm);
        expected.add(firstFilm);
        Set<Film> actual = filmDAO.getSetByGenre(genre);
        actual.forEach(film -> film.setAgeRating(ageRating));
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void filmDAO_get_All() throws DAOException {
        AgeRating ageRating = new AgeRating();
        createSupportingTables(ageRating);
        Film firstFilm = new Film()
                .setProducersName("Producer")
                .setTitle("Film")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(1);
        Film secondFilm = new Film()
                .setProducersName("Producer2")
                .setTitle("Film2")
                .setReleaseDate(Date.valueOf("2000-12-12"))
                .setDescription("Description")
                .setAgeRating(ageRating)
                .setDuration(Time.valueOf("01:00:00"))
                .setId(2);
        Set<Film> expected = new LinkedHashSet<>();

        filmDAO.add(firstFilm);
        filmDAO.add(secondFilm);
        expected.add(firstFilm);
        expected.add(secondFilm);

        Set<Film> actual = filmDAO.getAll();
        actual.forEach(film -> film.setAgeRating(ageRating));

        Assert.assertEquals(expected,actual);
    }
    @After
    public void clearTables() {
        super.clearTables(
                DROP_FILMS_GENRES,
                DROP_FILMS,
                DROP_GENRES,
                DROP_AGE_RATINGS
        );
    }
}
