package com.example.cinema.db.service;

import com.example.cinema.db.dao.DAOFactory;
import com.example.cinema.db.dao.MyDAOFactory;
import com.example.cinema.db.dao.implementation.TicketDAO;
import com.example.cinema.db.dao.implementation.UserDAO;
import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.DAOException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.iface.SessionService;
import com.example.cinema.db.service.iface.TicketService;
import com.example.cinema.db.service.iface.UserService;
import org.apache.log4j.Logger;


import java.util.Collection;
import java.util.Set;

public class TicketServiceImpl implements TicketService {

    private static final Logger LOG = Logger.getLogger(TicketServiceImpl.class);
    private final DAOFactory factory = MyDAOFactory.getInstance();

    private final SessionService sessionService = new SessionServiceImpl();
    private final UserService userService = new UserServiceImpl();

    private TicketDAO ticketDao;
    private UserDAO userDao;

    @Override
    public void addTickets(Collection<Ticket> tickets) throws ServiceException, TransactionException {
        LOG.info("Adding tickets starts");
        Transaction transaction = Transaction.createTransaction();
        try {
            ticketDao = factory.getTicketDao(transaction.getConnection());
            userDao = factory.getUserDAO(transaction.getConnection());
            transaction.startTransaction();

            for (Ticket ticket : tickets) {
                ticketDao.add(ticket);
                User user = userDao.getById(ticket.getCustomer().getId());
                LOG.info("Customer of ticket -> " + ticket + " is " + user);
                user.setCoins(user.getCoins() - ticket.getPrice());
                userDao.update(user);
                //userDao.updateCoinsById(user.getCoins() - ticket.getPrice(), ticket.getCustomer().getId());
            }

            transaction.commit();
            LOG.info("Tickets have been bought");
        } catch (DAOException e) {
            LOG.error("Can`t add tickets " + e);
            transaction.rollback();
            throw new ServiceException("Unfortunately, some of the selected tickets have already been purchased", e);
        } finally {
            transaction.endTransaction();
            transaction.close();
        }
    }

    @Override
    public Set<Ticket> getTicketsBySession(Session session) throws ServiceException, TransactionException {
        LOG.info("Getting tickets by session -> " + session);
        try (Transaction transaction = Transaction.createTransaction()) {
            ticketDao = factory.getTicketDao(transaction.getConnection());
            userDao = factory.getUserDAO(transaction.getConnection());
            Set<Ticket> tickets = ticketDao.getSetBySession(session);

            for (Ticket ticket : tickets) {
                ticket.setSession(session);
                ticket.setCustomer(userDao.getById(ticketDao.getCustomerId(ticket.getId())));
            }
            LOG.info("GOT tickets -> " + tickets);
            LOG.info("Getting tickets has been finished");
            return tickets;
        } catch (DAOException e) {
            LOG.error("Can`t get tickets ", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Ticket> getPartOfTicketsByCustomer(User user, int startWith, int partSize) throws ServiceException, TransactionException {
        LOG.info("Getting tickets by customer -> " + user + " from " + startWith + " to " + (startWith + partSize) + "starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            ticketDao = factory.getTicketDao(transaction.getConnection());
            userDao = factory.getUserDAO(transaction.getConnection());

            Set<Ticket> tickets = ticketDao.getSetByCustomer(user, startWith, partSize);

            for (Ticket ticket : tickets) {
                ticket.setCustomer(userDao.getById(ticketDao.getCustomerId(ticket.getId())))
                        .setSession(sessionService.getSessionById(ticketDao.getSessionId(ticket.getId())));
            }
            LOG.info("Got tickets -> " + tickets);
            LOG.info("Getting tickets has been finished");
            return tickets;
        } catch (DAOException e) {
            LOG.error("Can`t get tickets ", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Ticket> getTicketsByCustomer(User user) throws ServiceException, TransactionException {
        LOG.info("Getting tickets by customer starts -> " + user);
        try (Transaction transaction = Transaction.createTransaction()) {
            ticketDao = factory.getTicketDao(transaction.getConnection());
            userDao = factory.getUserDAO(transaction.getConnection());

            Set<Ticket> tickets = ticketDao.getSetByCustomer(user);

            for (Ticket ticket : tickets) {
                ticket.setCustomer(userDao.getById(ticketDao.getCustomerId(ticket.getId())))
                        .setSession(sessionService.getSessionById(ticketDao.getSessionId(ticket.getId())));
            }

            LOG.info("Got tickets -> " + tickets);
            LOG.info("Getting tickets has been finished");
            return tickets;
        } catch (DAOException e) {
            LOG.error("Can`t get tickets " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
