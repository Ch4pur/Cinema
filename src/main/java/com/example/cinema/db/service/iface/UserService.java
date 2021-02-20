package com.example.cinema.db.service.iface;

import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;


import java.util.Set;

public interface UserService {
    User getUserByMail(String mail) throws ServiceException, TransactionException;
    User getUserById(int id) throws ServiceException, TransactionException;

    Set<User> getAll() throws ServiceException, TransactionException;

    void addUser(User user) throws ServiceException, TransactionException;
    void deleteUserById(int id) throws ServiceException, TransactionException;
    void deleteUserByMail(String mail) throws ServiceException, TransactionException;

    void updateUser(User user) throws ServiceException, TransactionException;

    void addCoins(int userId, int coins) throws ServiceException, TransactionException;
    void deductCoins(int userId,int coins) throws ServiceException, TransactionException;

}
