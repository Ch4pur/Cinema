package com.example.cinema.db.service.iface;

import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;

import java.util.Collection;
import java.util.Set;

public interface TicketService {
    void addTickets(Collection<Ticket> ticket) throws ServiceException, TransactionException;

    Set<Ticket> getTicketsBySession(Session session) throws ServiceException, TransactionException;
    Set<Ticket> getPartOfTicketsByCustomer(User user, int startWith, int partSize) throws ServiceException, TransactionException;
    Set<Ticket> getTicketsByCustomer(User user) throws ServiceException, TransactionException;
}
