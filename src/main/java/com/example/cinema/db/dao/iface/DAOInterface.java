package com.example.cinema.db.dao.iface;

import com.example.cinema.db.entity.Entity;
import com.example.cinema.db.exception.DAOException;

import java.sql.Connection;
import java.util.Collection;


public interface DAOInterface<T extends Entity> {

    T getById(int id) throws DAOException;
    Collection<T> getAll() throws DAOException;

    void add(T entity) throws DAOException;
    void update(T entity) throws DAOException;
    void deleteById(int id) throws DAOException;

    int getCount() throws DAOException;
}
