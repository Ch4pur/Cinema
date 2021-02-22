package com.example.cinema.db.dao;

import com.example.cinema.db.dao.implementation.*;
import com.example.cinema.db.entity.*;
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

public class TicketDAOTest extends AbstractDAOTest {
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

    private static final String CREATE_USERS_TABLE = "" +
            "create  table if not exists Users\n" +
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
    private static final String CREATE_TICKETS_TABLE = "" +
            "create table Tickets\n" +
            "(\n" +
            "    Ticket_id    int primary key auto_increment,\n" +
            "    Customer_id  int  not null,\n" +
            "    Session_id   int  not null,\n" +
            "    Place_column tinyint unsigned not null,\n" +
            "    Place_row    tinyint unsigned not null,\n" +
            "    Price        int unsigned default 0 not null,\n" +
            "    Booking_date date not null,\n" +
            "\n" +
            "    Foreign key (Customer_id) references Users (User_id) on delete cascade,\n" +
            "    Foreign key (Session_id) references Sessions (Session_id) on delete cascade,\n" +
            "    Check (Place_column > 0 and Place_column <= 8),\n" +
            "    Check (Place_row > 0 and Place_row <= 8),\n" +
            "    unique (Session_id, Place_column, Place_row)\n" +
            ");";

    private static final String CREATE_TRIGGER_REDUCE_FREE_SEAT = "" +
            "CREATE trigger `reduce free seat`\n" +
            "    AFTER INSERT\n" +
            "    ON `tickets`\n" +
            "    for each row\n" +
            "begin\n" +
            "    UPDATE sessions\n" +
            "    set Number_of_free_seats = Number_of_free_seats - 1\n" +
            "    where NEW.Session_id = sessions.Session_id;\n" +
            "end;";

    private static final String CREATE_TRIGGER_ADD_FREE_SEAT = "" +
            "CREATE trigger `add free seat`\n" +
            "    BEFORE DELETE\n" +
            "    ON `tickets`\n" +
            "    for each row\n" +
            "begin\n" +
            "    UPDATE sessions\n" +
            "    set Number_of_free_seats = Number_of_free_seats + 1\n" +
            "    where Old.Session_id = sessions.Session_id;\n" +
            "end;";
    private static final String CREATE_TRIGGER_CREATE_PRICE = "" +
            "CREATE TRIGGER `fill price`\n" +
            "    BEFORE INSERT\n" +
            "    ON `tickets`\n" +
            "    FOR EACH ROW\n" +
            "BEGIN\n" +
            "    Set new.Price = 50 + 5 * new.Place_row +\n" +
            "                    5 * (select Hour(Session_full_date) from Sessions where session_id = new.session_id) +\n" +
            "                    20 * (select ThreeD_Supporting from Sessions where session_id = new.session_id);\n" +
            "end;";

    private static final String DROP_TICKETS = "Drop table Tickets";
    private static final String DROP_USERS = "Drop table Users";
    private static final String DROP_SESSIONS = "Drop table Sessions";
    private static final String DROP_AGE_RATINGS = "Drop table Age_ratings";
    private static final String DROP_FILMS = "Drop table Films";

    private static final String DROP_ADDING_TRIGGER = "Drop trigger if exists `add free seat`;";
    private static final String DROP_REDUCING_TRIGGER = "drop trigger if exists `reduce free seat`;";
    private static final String DROP_CREATING_TRIGGER = "Drop trigger if exists `fill price`;";

    private AgeRatingDAO ageRatingDAO;
    private FilmDAO filmDAO;
    private SessionDAO sessionDAO;
    private UserDAO userDAO;
    private TicketDAO ticketDAO;

    @Before
    public void createTables() throws SQLException {
        super.createTables(
                CREATE_AGE_RATINGS_TABLE,
                CREATE_FILMS_TABLE,
                CREATE_SESSIONS_TABLE,
                CREATE_USERS_TABLE,
                CREATE_SESSIONS_TABLE,
                CREATE_TICKETS_TABLE,
                CREATE_TRIGGER_ADD_FREE_SEAT,
                CREATE_TRIGGER_REDUCE_FREE_SEAT,
                CREATE_TRIGGER_CREATE_PRICE
        );

        ageRatingDAO = factory.getAgeRatingDao(connection);
        filmDAO = factory.getFilmDao(connection);
        sessionDAO = factory.getSessionDao(connection);
        userDAO = factory.getUserDAO(connection);
        ticketDAO = factory.getTicketDao(connection);
    }

    private void createSupportingTables(AgeRating ageRating, Film film, Session session, User user) throws DAOException {
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
        session.setFullDate(Timestamp.valueOf("2000-01-01 13:01:00"))
                .setFilm(film)
                .setThreeDSupporting(false)
                .setId(1);
        sessionDAO.add(session);
        user.setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test1@gmail.com")
                .setPhoneNumber("8005553434")
                .setId(1);
        userDAO.add(user);
    }

    @Test
    public void ticketDAO_get_fromEmptyDB() throws DAOException {
        Ticket ticket = ticketDAO.getById(1);

        Assert.assertNull(ticket);
    }

    @Test
    public void ticketDAO_get_byId() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);
        Ticket secondTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(2)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(2);
        ticketDAO.add(firstTicket);
        ticketDAO.add(secondTicket);
        Ticket actual = ticketDAO.getById(2);

        Assert.assertEquals(secondTicket, actual);
    }

    @Test
    public void ticketDAO_get_setByCustomer() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);
        Ticket secondTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(2)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(2);
        Set<Ticket> expected = new LinkedHashSet<>();

        ticketDAO.add(firstTicket);
        ticketDAO.add(secondTicket);
        expected.add(firstTicket);
        expected.add(secondTicket);

        Set<Ticket> actual = ticketDAO.getSetByCustomer(user);
        actual.forEach(ticket -> ticket.setCustomer(user).setSession(session));

        Assert.assertEquals(expected, actual);
    }
    @Test
    public void ticketDAO_get_setBySession() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);
        Ticket secondTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(2)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(2);
        Set<Ticket> expected = new LinkedHashSet<>();

        ticketDAO.add(firstTicket);
        ticketDAO.add(secondTicket);
        expected.add(firstTicket);
        expected.add(secondTicket);

        Set<Ticket> actual = ticketDAO.getSetBySession(session);
        actual.forEach(ticket -> ticket.setCustomer(user).setSession(session));

        Assert.assertEquals(expected, actual);
    }
    @Test
    public void ticketDAO_get_customerId() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket ticket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);

        ticketDAO.add(ticket);
        User actual = userDAO.getById(ticketDAO.getCustomerId(ticket.getId()));
        Assert.assertEquals(user, actual);
    }
    @Test
    public void ticketDAO_get_sessionId() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket ticket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);

        ticketDAO.add(ticket);
        Session actual = sessionDAO.getById(ticketDAO.getSessionId(ticket.getId()));
        Assert.assertEquals(session, actual);
    }

    @Test
    public void ticketDAO_get_PartByCustomer() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Set<Ticket> expected = new LinkedHashSet<>();
        for (int i =0; i <5; i++) {
            Ticket ticket = new Ticket()
                    .setSession(session)
                    .setCustomer(user)
                    .setPlaceColumn(i + 1)
                    .setPlaceRow(i + 1)
                    .setOrderDate(Date.valueOf("2000-01-01"))
                    .setId(i + 1);
            if (i > 2) {
                expected.add(ticket);
            }
            ticketDAO.add(ticket);
        }

        Set<Ticket> actual = ticketDAO.getSetByCustomer(user,3,2);
        actual.forEach(ticket -> ticket.setCustomer(user).setSession(session));

        Assert.assertEquals(expected, actual);
    }
    @Test
    public void ticketDAO_add() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);

        ticketDAO.add(firstTicket);

        Ticket actual = ticketDAO.getById(1);

        Assert.assertEquals(firstTicket, actual);
    }

    @Test(expected = DAOException.class)
    public void ticketDAO_add_twice() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);

        ticketDAO.add(firstTicket);
        ticketDAO.add(firstTicket);
    }

    @Test
    public void ticketDAO_update() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);

        ticketDAO.add(firstTicket);
        firstTicket.setPlaceColumn(2);
        ticketDAO.update(firstTicket);
        Ticket actual = ticketDAO.getById(1);

        Assert.assertEquals(firstTicket, actual);
    }

    @Test(expected = DAOException.class)
    public void ticketDAO_update_toExistingPlace() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);
        Ticket secondTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(2)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(2);
        ticketDAO.add(firstTicket);
        ticketDAO.add(secondTicket);
        secondTicket.setPlaceRow(firstTicket.getPlaceRow());
        ticketDAO.update(secondTicket);
    }

    @Test
    public void ticketDAO_delete() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);

        ticketDAO.add(firstTicket);
        ticketDAO.deleteById(firstTicket.getId());
        Ticket actual = ticketDAO.getById(firstTicket.getId());

        Assert.assertNull(actual);
    }

    @Test
    public void ticketDAO_get_numberOfFreeSeats() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        int expected = Session.NUMBER_OF_COLUMNS * Session.NUMBER_OF_ROWS;

        session = sessionDAO.getById(session.getId());
        int actual = session.getNumberOfFreeSeats();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void ticketDAO_get_numberOfFreeSeats_withTwoTickets() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);
        Ticket secondTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(2)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(2);
        int expected = Session.NUMBER_OF_COLUMNS * Session.NUMBER_OF_ROWS - 2;

        ticketDAO.add(firstTicket);
        ticketDAO.add(secondTicket);
        session = sessionDAO.getById(session.getId());
        int actual = session.getNumberOfFreeSeats();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void ticketDAO_get_numberOfFreeSeats_withTwoTickets_oneOfThatWasDeleted() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);
        Ticket secondTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(2)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(2);
        int expected = Session.NUMBER_OF_COLUMNS * Session.NUMBER_OF_ROWS - 1;

        ticketDAO.add(firstTicket);
        ticketDAO.add(secondTicket);
        ticketDAO.deleteById(secondTicket.getId());
        session = sessionDAO.getById(session.getId());
        int actual = session.getNumberOfFreeSeats();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void ticket_computePrice() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);
        firstTicket.computePrice();
        int expected = 120;

        Assert.assertEquals(expected, firstTicket.getPrice());
    }

    @Test
    public void ticket_get_Price() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);
        int expected = 120;
        ticketDAO.add(firstTicket);
        firstTicket = ticketDAO.getById(firstTicket.getId());

        Assert.assertEquals(expected,firstTicket.getPrice());
    }

    @Test
    public void ticketDAO_get_all() throws DAOException {
        AgeRating ageRating = new AgeRating();
        Film film = new Film();
        Session session = new Session();
        User user = new User();
        createSupportingTables(ageRating, film, session, user);
        Ticket firstTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(1)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(1);
        Ticket secondTicket = new Ticket()
                .setSession(session)
                .setCustomer(user)
                .setPlaceColumn(1)
                .setPlaceRow(2)
                .setOrderDate(Date.valueOf("2000-01-01"))
                .setId(2);
        Set<Ticket> expected = new LinkedHashSet<>();

        ticketDAO.add(firstTicket);
        ticketDAO.add(secondTicket);
        expected.add(firstTicket);
        expected.add(secondTicket);

        Set<Ticket> actual = ticketDAO.getAll();
        actual.forEach(ticket -> ticket.setCustomer(user).setSession(session));

        Assert.assertEquals(expected, actual);
    }

    @After
    public void clearTables() {
        super.clearTables(
                DROP_TICKETS,
                DROP_USERS,
                DROP_SESSIONS,
                DROP_FILMS,
                DROP_AGE_RATINGS,
                DROP_ADDING_TRIGGER,
                DROP_REDUCING_TRIGGER,
                DROP_CREATING_TRIGGER
        );
    }
}
