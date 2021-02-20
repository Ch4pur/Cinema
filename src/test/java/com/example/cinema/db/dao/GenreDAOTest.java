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

public class GenreDAOTest extends AbstractDAOTest {

    private static final String CREATE_AGE_RATING_TABLE = "" +
            "create table if not exists Age_ratings\n" +
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

    private AgeRatingDAO ageRatingDAO;
    private FilmDAO filmDAO;
    private GenreDAO genreDAO;

    @Before
    public void createTables() throws SQLException {
        super.createTables(
                CREATE_AGE_RATING_TABLE,
                CREATE_GENRES_TABLE,
                CREATE_FILMS_TABLE,
                CREATE_FILMS_GENRES_TABLE
        );

        ageRatingDAO = factory.getAgeRatingDao(connection);
        filmDAO = factory.getFilmDao(connection);
        genreDAO = factory.getGenreDao(connection);
    }

    @Test
    public void genreDAO_getFromEmptyDB() throws DAOException {
        Genre genre = genreDAO.getById(1);

        Assert.assertNull(genre);
    }

    @Test
    public void genreDAO_getById() throws DAOException {
        Genre firstGenre = new Genre().setName("Genre1");
        Genre secondGenre = new Genre().setName("Genre2");

        genreDAO.add(firstGenre);
        genreDAO.add(secondGenre);

        Genre actual = genreDAO.getById(2);

        Assert.assertEquals(secondGenre, actual);
    }

    @Test
    public void genreDAO_getByName() throws DAOException {
        Genre firstGenre = new Genre().setName("Genre1");
        Genre secondGenre = new Genre().setName("Genre2");

        genreDAO.add(firstGenre);
        genreDAO.add(secondGenre);
        Genre actual = genreDAO.getByName(secondGenre.getName());

        Assert.assertEquals(secondGenre, actual);
    }

    @Test
    public void genreDAO_addGenre() throws DAOException {
        Genre expected = new Genre().setName("genre");

        genreDAO.add(expected);
        Genre actual = genreDAO.getByName(expected.getName());

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = DAOException.class)
    public void genreDAO_addGenre_WithoutName() throws DAOException {
        Genre genre = new Genre();

        genreDAO.add(genre);
    }

    @Test(expected = DAOException.class)
    public void genreDAO_addGenre_twice() throws DAOException {
        Genre genre = new Genre().setName("Genre");

        genreDAO.add(genre);
        genreDAO.add(genre);
    }

    @Test
    public void genreDAO_updateGenre() throws DAOException {
        Genre genre = new Genre().setName("Genre").setId(1);

        genreDAO.add(genre);
        genre.setName("aaaaa");
        genreDAO.update(genre);
        Genre updated = genreDAO.getByName(genre.getName());

        Assert.assertEquals(genre, updated);
    }

    @Test(expected = DAOException.class)
    public void genreDAO_updateGenre_makeExistingGenre() throws DAOException {
        Genre firstGenre = new Genre().setName("Genre").setId(1);
        Genre secondGenre = new Genre().setName("Genre2");

        genreDAO.add(firstGenre);
        genreDAO.add(secondGenre);
        firstGenre.setName(secondGenre.getName());
        genreDAO.update(firstGenre);
    }

    @Test
    public void genreDAO_deleteById() throws DAOException {
        Genre genre = new Genre().setName("asdasd");

        genreDAO.add(genre);
        genre = genreDAO.getByName(genre.getName());
        genreDAO.deleteById(genre.getId());
        Genre actual = genreDAO.getByName(genre.getName());

        Assert.assertNull(null);
    }

    @Test
    public void genreDAO_deleteByName() throws DAOException {
        Genre genre = new Genre().setName("asdasd");

        genreDAO.add(genre);
        genreDAO.deleteByName(genre.getName());
        Genre actual = genreDAO.getByName(genre.getName());
        Assert.assertNull(actual);
    }

    @Test
    public void genreDAO_getAll() throws DAOException {
        Genre firstGenre = new Genre().setName("Genre").setId(1);
        Genre secondGenre = new Genre().setName("Genre2");
        Set<Genre> expected = new LinkedHashSet<>();

        expected.add(firstGenre);
        expected.add(secondGenre);
        genreDAO.add(firstGenre);
        genreDAO.add(secondGenre);
        Set<Genre> actual = genreDAO.getAll();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void genreDAO_addGenreToFilm() throws DAOException {
        Genre firstGenre = new Genre()
                .setName("Genre")
                .setId(1);
        Genre secondGenre = new Genre()
                .setName("Genre2")
                .setId(2);
        Genre thirdGenre = new Genre()
                .setName("Genre3")
                .setId(3);
        AgeRating ageRating = new AgeRating()
                .setTitle("AgeRating")
                .setMinAge((short) 11)
                .setWithParents(false)
                .setId(1);
        Film film = new Film()
                .setTitle("Film")
                .setDuration(Time.valueOf("01:00:00"))
                .setAgeRating(ageRating)
                .setDescription("Description")
                .setReleaseDate(Date.valueOf("2002-01-01"))
                .setProducersName("Producer")
                .setId(1);
        Set<Genre> expected = new LinkedHashSet<>();

        expected.add(firstGenre);
        expected.add(secondGenre);
        expected.add(thirdGenre);
        ageRatingDAO.add(ageRating);
        filmDAO.add(film);
        for(Genre genre : expected) {
            genreDAO.add(genre);
            genreDAO.addGenreToFilm(genre,film);
        }
        Set<Genre> actual = genreDAO.getSetByFilm(film);

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void genreDAO_deleteGenresFromFilm() throws DAOException {
        Genre firstGenre = new Genre()
                .setName("Genre")
                .setId(1);
        Genre secondGenre = new Genre()
                .setName("Genre2")
                .setId(2);
        Genre thirdGenre = new Genre()
                .setName("Genre3")
                .setId(3);
        AgeRating ageRating = new AgeRating()
                .setTitle("AgeRating")
                .setMinAge((short) 11)
                .setWithParents(false)
                .setId(1);
        Film film = new Film()
                .setTitle("Film")
                .setDuration(Time.valueOf("01:00:00"))
                .setAgeRating(ageRating)
                .setDescription("Description")
                .setReleaseDate(Date.valueOf("2002-01-01"))
                .setProducersName("Producer")
                .setId(1);
        Set<Genre> expected = new LinkedHashSet<>();

        expected.add(firstGenre);
        expected.add(secondGenre);
        expected.add(thirdGenre);
        ageRatingDAO.add(ageRating);
        filmDAO.add(film);
        for(Genre genre : expected) {
            genreDAO.add(genre);
            genreDAO.addGenreToFilm(genre,film);
        }
        genreDAO.deleteGenresFromFilm(film);

        Set<Genre> actual = genreDAO.getSetByFilm(film);

        Assert.assertTrue(actual.isEmpty());
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
