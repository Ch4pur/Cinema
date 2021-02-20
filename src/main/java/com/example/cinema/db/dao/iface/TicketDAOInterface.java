package com.example.cinema.db.dao.iface;

import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.DAOException;

import java.util.Set;

public interface TicketDAOInterface<T extends Ticket> extends DAOInterface<T> {
    Set<T> getSetByCustomer(User user) throws DAOException;
    Set<T> getSetByCustomer(User user, int startWith, int pageSize) throws DAOException;
    Set<T> getSetBySession(Session session) throws DAOException;

    int getCustomerId(int id) throws DAOException;
    int getSessionId(int id) throws DAOException;
}
