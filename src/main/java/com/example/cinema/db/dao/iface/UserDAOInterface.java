package com.example.cinema.db.dao.iface;

import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.DAOException;

public interface UserDAOInterface<T extends User> extends DAOInterface<T> {
    T getByMail(String mail) throws DAOException;
    void deleteByMail(String mail) throws DAOException;

}
