package com.example.cinema.db.dao.implementation;

import com.example.cinema.db.dao.iface.TicketDAOInterface;
import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.DAOException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class TicketDAO extends AbstractDAO<Ticket> implements TicketDAOInterface<Ticket> {

    private static final Logger LOG = Logger.getLogger(TicketDAO.class);
    private static final String GET_BY_ID = "Select * From Tickets Where Ticket_id = ?";

    private static final String GET_ALL = "Select * From Tickets";

    private static final String ADD = "Insert Into Tickets (Customer_id,Session_id,Place_column,Place_row, Booking_date)" +
            "Value (?,?,?,?,?)";

    private static final String UPDATE = "Update Tickets " +
            "Set Customer_id = ?,Session_id = ?,Place_column =?,Place_row = ? , Booking_date = ?" +
            "Where Ticket_id = ?";

    private static final String DELETE_BY_ID = "Delete From Tickets Where Ticket_id = ?";

    private static final String GET_SET_BY_CUSTOMER = "Select * From Tickets Where Customer_id = ?";
    private static final String GET_SET_BY_CUSTOMER_WITH_PAGINATION = "Select * From Tickets Where Customer_id =? Limit ? Offset ?";
    private static final String GET_SET_BY_SESSION = "Select * From Tickets Where Session_id = ?";

    private static final String GET_CUSTOMER_ID = "Select Customer_id From Tickets Where Ticket_id = ?";
    private static final String GET_SESSION_ID = "Select Session_id From Tickets Where Ticket_id =?";

    public TicketDAO(Connection connection) {
        super(connection);
        LOG.info("TicketDAO is created with connection " + connection);
    }

    @Override
    public Ticket getById(int id) throws DAOException {
        LOG.info("Getting a ticket by id -> " + id);
        return getByStatement(GET_BY_ID, String.valueOf(id));
    }

    @Override
    public Set<Ticket> getAll() throws DAOException {
        LOG.info("Getting all tickets");
        return new LinkedHashSet<>(getAllByStatement(GET_ALL));
    }

    @Override
    public void add(Ticket ticket) throws DAOException {
        LOG.info("Adding new ticket - > " + ticket);
        updateByStatement(ADD,
                String.valueOf(ticket.getCustomer().getId()),
                String.valueOf(ticket.getSession().getId()),
                String.valueOf(ticket.getPlaceColumn()),
                String.valueOf(ticket.getPlaceRow()),
                ticket.getOrderDate().toString());
    }

    @Override
    public void update(Ticket ticket) throws DAOException {
        LOG.info("Updating the ticket -> " + ticket);
        updateByStatement(UPDATE, String.valueOf(ticket.getCustomer().getId()),
                String.valueOf(ticket.getSession().getId()),
                String.valueOf(ticket.getPlaceColumn()),
                String.valueOf(ticket.getPlaceRow()),
                ticket.getOrderDate().toString(),
                String.valueOf(ticket.getId()));
    }

    @Override
    public void deleteById(int id) throws DAOException {
        LOG.info("Deleting a ticket with id -> " + id);
        updateByStatement(DELETE_BY_ID,
                String.valueOf(id));
    }


    @Override
    public Set<Ticket> getSetByCustomer(User user) throws DAOException {
        LOG.info("Getting a set of tickets by customer -> " + user);
        return new LinkedHashSet<>(getAllByStatement(GET_SET_BY_CUSTOMER,
                String.valueOf(user.getId())));
    }

    @Override
    public Set<Ticket> getSetByCustomer(User user, int startWith, int pageSize) throws DAOException {
        LOG.info("Getting a set of tickets by customer -> " + user + " from " + startWith + " to " + (startWith + pageSize));
        return new LinkedHashSet<>(getPartByStatement(
                startWith,
                pageSize,
                GET_SET_BY_CUSTOMER_WITH_PAGINATION,
                String.valueOf(user.getId())));
    }

    @Override
    public Set<Ticket> getSetBySession(Session session) throws DAOException {
        LOG.info("Getting a set of tickets by session -> " + session);
        return new LinkedHashSet<>(getAllByStatement(GET_SET_BY_SESSION,
                String.valueOf(session.getId())));
    }

    @Override
    public int getCustomerId(int id) throws DAOException {
        LOG.info("Getting a customers id by ticket id -> " + id);
        return getForeignKeyId(GET_CUSTOMER_ID, id);
    }

    @Override
    public int getSessionId(int id) throws DAOException {
        LOG.info("Getting a sessions id by ticket id -> " + id);
        return getForeignKeyId(GET_SESSION_ID, id);
    }

    @Override
    protected Ticket createEntity(ResultSet resultSet) throws SQLException {
        return new Ticket()
                .setPlaceColumn(resultSet.getInt("place_column"))
                .setPlaceRow(resultSet.getInt("place_row"))
                .setOrderDate(resultSet.getDate("Booking_date"))
                .setPrice(resultSet.getInt("Price"))
                .setId(resultSet.getInt("Ticket_id"));
    }
}
