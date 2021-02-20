package com.example.cinema.db.dao.implementation;

import com.example.cinema.db.dao.iface.UserDAOInterface;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.DAOException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

public class UserDAO extends AbstractDAO<User> implements UserDAOInterface<User> {

    private static final Logger LOG = Logger.getLogger(UserDAO.class);

    private static final String GET_USER_BY_MAIL = "Select * from Users where Mail = ?";
    private static final String GET_USER_BY_ID = "Select * From Users where User_id =?";
    private static final String GET_ALL = "Select * From Users";
    private static final String ADD_USER = "Insert Into Users " +
            "(Mail,First_name,Last_name,Phone_number,is_Admin,Birthday, Password) " +
            "Values (?,?,?,?,?,?,?)";
    private static final String UPDATE_USER = "Update Users Set " +
            "Mail =?,First_name=?,Last_name=?,Phone_number=?,Is_admin=?,Birthday= ?" +
            ",Password=?, Coins =? Where User_id=?";
    private static final String UPDATE_USERS_WALLET = "Update Users Set Coins =? Where User_id =?";

    private static final String DELETE_USER_BY_ID = "Delete From Users Where User_id=?";
    private static final String DELETE_USER_BY_MAIL = "Delete From Users Where Mail =?";

    public UserDAO(Connection connection) {
        super(connection);
        LOG.info("UserDAO is created with connection -> " + connection);
    }


    @Override
    public User getByMail(String mail) throws DAOException {
        LOG.info("Getting a user by mail -> " + mail);
        return getByStatement(GET_USER_BY_MAIL,
                mail);
    }

    @Override
    public User getById(int id) throws DAOException {
        LOG.info("Getting a user by id -> " + id);
        return getByStatement(GET_USER_BY_ID,
                String.valueOf(id));
    }

    @Override
    public Set<User> getAll() throws DAOException {
        LOG.info("Getting all users");
        return new HashSet<>(getAllByStatement(GET_ALL));
    }

    @Override
    public void add(User user) throws DAOException {
        LOG.info("Adding new user -> " + user);
        updateByStatement(ADD_USER,
                user.getMail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.isAdmin() ? "1" : "0",
                user.getBirthday().toString(),
                user.getPassword());
    }

    @Override
    public void update(User user) throws DAOException {
        LOG.info("Updating the user -> " + user);
        updateByStatement(UPDATE_USER,
                user.getMail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.isAdmin() ? "1" : "0",
                user.getBirthday().toString(),
                user.getPassword(),
                String.valueOf(user.getCoins()),
                String.valueOf(user.getId()));
    }

    @Override
    public void deleteById(int id) throws DAOException {
        LOG.info("Deleting a user by id -> " + id);
        updateByStatement(DELETE_USER_BY_ID,
                String.valueOf(id));
    }


    @Override
    public void deleteByMail(String mail) throws DAOException {
        LOG.info("Deleting a user by mail -> " + mail);
        updateByStatement(DELETE_USER_BY_MAIL,
                mail);
    }

    @Override
    protected User createEntity(ResultSet resultSet) throws SQLException {
        return new User().setAdmin(resultSet.getBoolean("is_admin"))
                .setBirthday(resultSet.getDate("birthday"))
                .setFirstName(resultSet.getString("First_name"))
                .setLastName(resultSet.getString("Last_name"))
                .setMail(resultSet.getString("Mail"))
                .setPassword(resultSet.getString("Password"))
                .setPhoneNumber(resultSet.getString("Phone_number"))
                .setCoins(resultSet.getInt("Coins"))
                .setId(resultSet.getInt("User_id"));
    }

}

