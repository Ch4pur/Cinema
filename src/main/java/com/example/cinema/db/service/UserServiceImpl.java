package com.example.cinema.db.service;

import com.example.cinema.db.dao.MyDAOFactory;
import com.example.cinema.db.dao.implementation.UserDAO;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.DAOException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.iface.UserService;
import org.apache.log4j.Logger;

import java.util.Set;

public class UserServiceImpl implements UserService {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

    private final MyDAOFactory factory = MyDAOFactory.getInstance();

    private UserDAO userDao;

    @Override
    public User getUserByMail(String mail) throws ServiceException, TransactionException {
        LOG.info("Getting user by mail ->" + mail);
        try (Transaction transaction = Transaction.createTransaction()) {
            userDao = factory.getUserDAO(transaction.getConnection());

            User user = userDao.getByMail(mail);
            LOG.info("Got user -> " + user);
            return user;
        } catch (DAOException e) {
            LOG.error("Can`t get user ", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public User getUserById(int id) throws ServiceException, TransactionException {
        LOG.info("Getting user by id ->" + id);
        try (Transaction transaction = Transaction.createTransaction()) {
            userDao = factory.getUserDAO(transaction.getConnection());
            User user = userDao.getById(id);
            LOG.info("Got user ->" + user);
            return user;
        } catch (DAOException e) {
            LOG.error("Can`t get user " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Set<User> getAll() throws ServiceException, TransactionException {
        LOG.info("Getting all users");
        try (Transaction transaction = Transaction.createTransaction()) {
            userDao = factory.getUserDAO(transaction.getConnection());
            Set<User> users = userDao.getAll();

            LOG.info("Got users -> " + users);
            return users;
        } catch (DAOException e) {
            LOG.error("Can`t get users ", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void addUser(User user) throws ServiceException, TransactionException {
        LOG.info("Adding user ->" + user);
        try (Transaction transaction = Transaction.createTransaction()) {
            userDao = factory.getUserDAO(transaction.getConnection());

            userDao.add(user);
            LOG.info("Adding user has been finished");
        } catch (DAOException e) {
            e.printStackTrace();
            LOG.error("Can`t add user " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteUserById(int id) throws ServiceException, TransactionException {
        LOG.info("Deleting user by id -> " + id);
        try (Transaction transaction = Transaction.createTransaction()) {
            userDao = factory.getUserDAO(transaction.getConnection());

            userDao.deleteById(id);
            LOG.info("User has been deleted");
        } catch (DAOException e) {
            LOG.error("Can`t delete user " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteUserByMail(String mail) throws ServiceException, TransactionException {
        LOG.info("Deleting user by mail -> " + mail);
        try (Transaction transaction = Transaction.createTransaction()) {
            userDao = factory.getUserDAO(transaction.getConnection());

            userDao.deleteByMail(mail);
            LOG.info("User has been deleted");
        } catch (DAOException e) {
            LOG.error("Can`t delete user " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void updateUser(User user) throws ServiceException, TransactionException {
        LOG.info("Updating user -> " + user);
        try (Transaction transaction = Transaction.createTransaction()) {
            userDao = factory.getUserDAO(transaction.getConnection());

            userDao.update(user);
            LOG.info("User has been updated");
        } catch (DAOException e) {
            LOG.error("Can`t update user" + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void addCoins(int userId, int coins) throws ServiceException, TransactionException {
        LOG.info("Adding " + coins + " coins to user with id " + userId);
        Transaction transaction = Transaction.createTransaction();
        try {
            userDao = factory.getUserDAO(transaction.getConnection());
            User userFromDB = userDao.getById(userId);
            LOG.info("Get user -> " + userFromDB);
            transaction.startTransaction();
            //mocked bank-card transaction
            //wheep - wheep we got money
            LOG.info("Bank transaction has been finished");
            userFromDB.setCoins(userFromDB.getCoins() + coins);
            userDao.update(userFromDB);
//            userDao.updateCoinsById(userFromDB.getCoins() + coins, userId);
            transaction.commit();
            LOG.info("Adding coins has to " + userFromDB + " been finished");
        } catch (DAOException e) {
            LOG.error("Can`t add coins " + e);
            transaction.rollback();
            throw new ServiceException("You have limit of coins", e);
        } finally {
            transaction.endTransaction();
        }
    }

    @Override
    public void deductCoins(int userId, int coins) throws ServiceException, TransactionException {
        LOG.info("Deduction of " + coins + " coins to user with id " + userId);
        try (Transaction transaction = Transaction.createTransaction()) {
            userDao = factory.getUserDAO(transaction.getConnection());
            User userFromDB = userDao.getById(userId);
            LOG.info("Got user -> " + userFromDB);
            userFromDB.setCoins(userFromDB.getCoins() - coins);
            userDao.update(userFromDB);
            //userDao.updateCoinsById(userFromDB.getCoins() - coins, userId);
            LOG.info("Deduction has been finished");
        } catch (DAOException e) {
            LOG.error("Can`t deduct coins ", e);
            throw new ServiceException("You have not enough money", e);
        }
    }
}
