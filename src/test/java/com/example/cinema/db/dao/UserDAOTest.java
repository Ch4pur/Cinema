package com.example.cinema.db.dao;

import com.example.cinema.db.dao.implementation.UserDAO;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.DAOException;
import org.junit.*;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class UserDAOTest extends AbstractDAOTest {

    private UserDAO userDAO;
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
    private static final String DROP_USERS = "Drop table Users";
    @Before
    public void clearTable() throws SQLException {
        super.createTables(CREATE_USERS_TABLE);
        userDAO = factory.getUserDAO(connection);
    }

    @Test
    public void userDAO_getFromEmptyDB() throws DAOException {

        User user = userDAO.getById(1);

        Assert.assertNull(user);
    }
    @Test
    public void userDAO_getByNegativeId() throws DAOException {
        User testUser = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);

        userDAO.add(testUser);
        User user = userDAO.getById(-1);

        Assert.assertNull(user);
    }
    @Test
    public void userDAO_getByNon_existingId() throws DAOException {
        User testUser = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);

        userDAO.add(testUser);
        User user = userDAO.getById(2);

        Assert.assertNull(user);
    }
    @Test
    public void userDAO_getById() throws DAOException {
        User firstUser = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test1@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);
        User secondUser =new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test2@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(2);

        userDAO.add(firstUser);
        userDAO.add(secondUser);
        User user = userDAO.getById(2);

        Assert.assertEquals(secondUser, user);
    }
    @Test
    public void userDAO_addToDB() throws DAOException {
        User testUser = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);

        userDAO.add(testUser);
        User user = userDAO.getById(1);

        Assert.assertEquals(testUser, user);
    }

    @Test (expected = DAOException.class)
    public void userDAO_addToDB_withoutMail() throws DAOException {
        User testUser = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setId(1);
        userDAO.add(testUser);
    }
    @Test (expected = NullPointerException.class)
    public void userDAO_addToDB_withoutBirthday() throws DAOException {
        User testUser = new User()
                .setAdmin(false)
                .setPassword(User.getSaltedHash("12121212s"))
                .setFirstName("Sdsd")
                .setMail("test@gmail.com")
                .setLastName("Sdsdsd")
                .setId(1);
        userDAO.add(testUser);
    }
    @Test (expected = DAOException.class)
    public void userDAO_addToDB_withoutPassword() throws DAOException {
        User testUser = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);
        userDAO.add(testUser);
    }
    @Test
    public void userDAO_getByMail() throws DAOException {
        User testUser = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);

        userDAO.add(testUser);
        User user = userDAO.getByMail(testUser.getMail());

        Assert.assertEquals(testUser, user);
    }

    @Test
    public void userDAO_deleteById() throws DAOException {
        User testUser = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);

        userDAO.add(testUser);
        userDAO.deleteById(1);
        User user = userDAO.getById(1);

        Assert.assertNull(user);
    }
    @Test
    public void userDAO_deleteByMail() throws DAOException {
        User testUser = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);
        userDAO.deleteByMail(testUser.getMail());
        User user = userDAO.getByMail(testUser.getMail());

        Assert.assertNull(user);
    }

    @Test
    public void userDAO_updateById() throws DAOException {
        User user = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);

        userDAO.add(user);
        user.setFirstName("aaaa").setLastName("asaw");
        userDAO.update(user);
        User updatedUser = userDAO.getById(1);

        Assert.assertEquals(user,updatedUser);
    }

    @Test
    public void userDAO_updateByMail() throws DAOException {
        User user = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);

        userDAO.add(user);
        user.setFirstName("aaaa").setLastName("asaw");
        userDAO.update(user);
        User updatedUser = userDAO.getByMail(user.getMail());

        Assert.assertEquals(user,updatedUser);
    }


    @Test
    public void userDAO_getAll() throws DAOException {
        Set<User> expected = new HashSet<>();
        User firstUser = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test1@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);

        User secondUser =new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test2@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(2);

        expected.add(firstUser);
        expected.add(secondUser);
        userDAO.add(firstUser);
        userDAO.add(secondUser);

        Set<User> actual = userDAO.getAll();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void userDAO_getCount_FromEmptyDB() throws DAOException {
        int expected = 0;

        int count = userDAO.getCount();

        Assert.assertEquals(expected,count);
    }
    @Test
    public void userDAO_getCount() throws DAOException {
        int expected = 2;
        User firstUser = new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test1@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);

        User secondUser =new User()
                .setAdmin(false)
                .setBirthday(Date.valueOf("2001-01-01"))
                .setPassword(User.getSaltedHash("12121212s"))
                .setMail("test2@gmail.com")
                .setFirstName("Sdsd")
                .setLastName("Sdsdsd")
                .setPhoneNumber("8005553434")
                .setId(1);

        userDAO.add(firstUser);
        userDAO.add(secondUser);
        int count = userDAO.getCount();

        Assert.assertEquals(expected,count);
    }

    @After
    public void clearTables() {
        super.clearTables(DROP_USERS);
    }
}
