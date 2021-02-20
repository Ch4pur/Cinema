package com.example.cinema.db.dao;

import com.example.cinema.db.dao.implementation.AgeRatingDAO;
import com.example.cinema.db.dao.implementation.CommentDAO;
import com.example.cinema.db.dao.implementation.FilmDAO;
import com.example.cinema.db.dao.implementation.UserDAO;
import com.example.cinema.db.entity.AgeRating;
import com.example.cinema.db.entity.Comment;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.DAOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

public class CommentDAOTest extends AbstractDAOTest {

    private static final String CREATE_USERS_TABLE = "" +
            "create table if not exists Users\n" +
            "(\n" +
            "    User_id      int primary key auto_increment,\n" +
            "    Mail         varchar(30) unique     not null,\n" +
            "    First_name   varchar(20),\n" +
            "    Last_name    varchar(20),\n" +
            "    Phone_number varchar(15),\n" +
            "    Is_Admin     boolean      default 0 not null,\n" +
            "    Coins        int unsigned default 0 not null,\n" +
            "    Birthday     date                   not null,\n" +
            "    Password     varchar(50)            not null\n" +
            ");";
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
    private static final String CREATE_COMMENTS_TABLE = "" +
            "create table if not exists Comments\n" +
            "(\n" +
            "    Comment_id      int primary key auto_increment,\n" +
            "    Film_id         int  not null,\n" +
            "    Author_id       int  not null,\n" +
            "    Content         text not null,\n" +
            "    Date_of_writing date not null,\n" +
            "\n" +
            "    FOREIGN KEY (Film_id) references Films (Film_id) on delete cascade,\n" +
            "    FOREIGN KEY (Author_id) references Users (User_id) on delete cascade\n" +
            ");";

    private static final String DROP_COMMENTS = "Drop table Comments";
    private static final String DROP_AGE_RATINGS = "Drop table Age_ratings";
    private static final String DROP_FILMS = "Drop table Films";
    private static final String DROP_USERS = "Drop table Users";

    private UserDAO userDao;
    private CommentDAO commentDAO;
    private FilmDAO filmDAO;
    private AgeRatingDAO ageRatingDAO;

    @Before
    public void createTables() throws SQLException {
        super.createTables(
                CREATE_USERS_TABLE,
                CREATE_AGE_RATING_TABLE,
                CREATE_FILMS_TABLE,
                CREATE_COMMENTS_TABLE
        );

        userDao = factory.getUserDAO(connection);
        commentDAO = factory.getCommentDao(connection);
        filmDAO = factory.getFilmDao(connection);
        ageRatingDAO = factory.getAgeRatingDao(connection);
    }

    private void createSecondaryTables(User author, Film film) throws DAOException {
        author.setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test1@gmail.com")
                .setPhoneNumber("8005553434")
                .setId(1);
        userDao.add(author);
        AgeRating ageRating = new AgeRating()
                .setTitle("AgeRating")
                .setMinAge((short) 11)
                .setWithParents(false)
                .setId(1);
        ageRatingDAO.add(ageRating);
        film.setTitle("Film")
                .setDuration(Time.valueOf("01:00:00"))
                .setAgeRating(ageRating)
                .setDescription("Description")
                .setReleaseDate(Date.valueOf("2002-01-01"))
                .setProducersName("Producer")
                .setId(1);
        filmDAO.add(film);
    }

    @Test
    public void commentDAO_get_FromEmptyDB() throws DAOException {
        Comment comment = commentDAO.getById(1);

        Assert.assertNull(comment);
    }

    @Test
    public void commentDAO_getById() throws DAOException {
        Film film = new Film();
        User user = new User();
        createSecondaryTables(user,film);

        Comment firstComment = new Comment()
                .setFilm(film)
                .setAuthor(user)
                .setContent("content")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(1);
        Comment secondComment = new Comment()
                .setFilm(film)
                .setAuthor(user)
                .setContent("content2")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(2);
        commentDAO.add(firstComment);
        commentDAO.add(secondComment);
        Comment actual = commentDAO.getById(2).setAuthor(user).setFilm(film);

        Assert.assertEquals(secondComment,actual);
    }

    @Test
    public void commentDAO_add() throws DAOException {
        Film film = new Film();
        User user = new User();
        createSecondaryTables(user,film);

        Comment firstComment = new Comment()
                .setFilm(film)
                .setAuthor(user)
                .setContent("content")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(1);
        commentDAO.add(firstComment);
        Comment actual = commentDAO.getById(1).setFilm(film).setAuthor(user);

        Assert.assertEquals(firstComment,actual);
    }

    @Test
    public void commentDAO_delete() throws DAOException {
        Film film = new Film();
        User user = new User();
        createSecondaryTables(user,film);

        Comment firstComment = new Comment()
                .setFilm(film)
                .setAuthor(user)
                .setContent("content")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(1);

        commentDAO.add(firstComment);
        commentDAO.deleteById(1);

        Comment actual = commentDAO.getById(1);

        Assert.assertNull(actual);
    }

    @Test
    public void commentDAO_update() throws DAOException {
        Film film = new Film();
        User user = new User();
        createSecondaryTables(user,film);

        Comment expected = new Comment()
                .setFilm(film)
                .setAuthor(user)
                .setContent("content")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(1);
        commentDAO.add(expected);
        expected.setContent("content2");
        commentDAO.update(expected);
        Comment actual = commentDAO.getById(expected.getId());
        actual.setAuthor(user).setFilm(film);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void commentDAO_getAuthorId() throws DAOException {
        Film film = new Film();
        User expected = new User();
        createSecondaryTables(expected,film);

        Comment comment = new Comment()
                .setFilm(film)
                .setAuthor(expected)
                .setContent("content")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(1);
        commentDAO.add(comment);
        User actual = userDao.getById(commentDAO.getAuthorId(comment.getId()));

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void commentDAO_getFilmId() throws DAOException {
        Film expected = new Film();
        User author = new User();
        createSecondaryTables(author,expected);

        Comment comment = new Comment()
                .setFilm(expected)
                .setAuthor(author)
                .setContent("content")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(1);
        commentDAO.add(comment);
        Film actual = filmDAO.getById(commentDAO.getFilmId(comment.getId())).setAgeRating(expected.getAgeRating());

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void commentDAO_getSetByFilm() throws DAOException {
        Film film = new Film();
        User author = new User();
        createSecondaryTables(author,film);
        Set<Comment> expected = new LinkedHashSet<>();
        Comment firstComment = new Comment()
                .setFilm(film)
                .setAuthor(author)
                .setContent("content2")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(1);
        Comment secondComment = new Comment()
                .setFilm(film)
                .setAuthor(author)
                .setContent("content2")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(2);
        expected.add(firstComment);
        expected.add(secondComment);
        commentDAO.add(firstComment);
        commentDAO.add(secondComment);
        Set<Comment> actual = commentDAO.getSetByFilm(film);
        actual.forEach(comment -> {
            comment.setFilm(film);
            comment.setAuthor(author);
        });
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void commentDAO_getPartByFilm() throws DAOException {
        Film film = new Film();
        User author = new User();
        createSecondaryTables(author,film);
        Set<Comment> expected = new LinkedHashSet<>();
        for (int i = 0 ; i < 5; i++) {
            Comment comment = new Comment()
                    .setFilm(film)
                    .setAuthor(author)
                    .setContent("content" + i)
                    .setDateOfWriting(Date.valueOf(LocalDate.now()))
                    .setId(i + 1);
            if (i > 2) {
                expected.add(comment);
            }

            commentDAO.add(comment);
        }

        Set<Comment> actual = commentDAO.getSetByFilm(film,3,2);
        actual.forEach(comment -> {
            comment.setFilm(film);
            comment.setAuthor(author);
        });
        Assert.assertEquals(expected,actual);
    }
    @Test
    public void commentDAO_getSetByAuthor() throws DAOException {
        Film film = new Film();
        User author = new User();
        createSecondaryTables(author,film);
        Set<Comment> expected = new LinkedHashSet<>();
        Comment firstComment = new Comment()
                .setFilm(film)
                .setAuthor(author)
                .setContent("content2")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(1);
        Comment secondComment = new Comment()
                .setFilm(film)
                .setAuthor(author)
                .setContent("content2")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(2);
        expected.add(firstComment);
        expected.add(secondComment);
        commentDAO.add(firstComment);
        commentDAO.add(secondComment);
        Set<Comment> actual = commentDAO.getSetBySender(author);
        actual.forEach(comment -> {
            comment.setFilm(film);
            comment.setAuthor(author);
        });
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void commentDAO_getAll() throws DAOException {
        Film film = new Film();
        User author = new User();
        createSecondaryTables(author,film);
        Set<Comment> expected = new LinkedHashSet<>();
        Comment firstComment = new Comment()
                .setFilm(film)
                .setAuthor(author)
                .setContent("content2")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(1);
        Comment secondComment = new Comment()
                .setFilm(film)
                .setAuthor(author)
                .setContent("content2")
                .setDateOfWriting(Date.valueOf(LocalDate.now()))
                .setId(2);
        expected.add(firstComment);
        expected.add(secondComment);
        commentDAO.add(firstComment);
        commentDAO.add(secondComment);
        Set<Comment> actual = commentDAO.getAll();
        actual.forEach(comment -> {
            comment.setFilm(film);
            comment.setAuthor(author);
        });
        Assert.assertEquals(expected,actual);
    }
    @After
    public void clearTables() {
        super.clearTables(
                DROP_COMMENTS,
                DROP_FILMS,
                DROP_AGE_RATINGS,
                DROP_USERS
        );
    }

}
