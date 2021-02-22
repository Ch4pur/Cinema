package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Entity;
import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.SessionServiceImpl;
import com.example.cinema.db.service.TicketServiceImpl;
import com.example.cinema.db.service.UserServiceImpl;
import com.example.cinema.db.service.iface.SessionService;
import com.example.cinema.db.service.iface.TicketService;
import com.example.cinema.db.service.iface.UserService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;

public class DeleteSessionCommand implements Command{
    private static final Logger LOG = Logger.getLogger(DeleteSessionCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {
        LOG.info("Start delete session");
        LOG.info("Current URI -> " + request.getRequestURI());

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //только админ может удалять сеанс
        if (user == null || !user.isAdmin()) {
            LOG.warn("Unauthorized login attempt");
            throw new CommandException("Trying to get on " + request.getRequestURI() + " from not admin");
        }
        SessionService sessionService = new SessionServiceImpl();
        TicketService ticketService = new TicketServiceImpl();
        UserService userService = new UserServiceImpl();
        try {
            int sessionId = Integer.parseInt(request.getParameter("session_id"));
            LOG.info("Get session id -> " + sessionId);
            Session filmSession = sessionService.getSessionById(sessionId);
            LOG.info("Get session -> " + filmSession);
            if (filmSession == null) {
                LOG.warn("Session not found");
                return request.getContextPath() + Pages.MAIN;
            }
            Set<Ticket> tickets = ticketService.getTicketsBySession(filmSession);
            LOG.info("Get session tickets -> " + tickets);
            for (Ticket ticket : tickets) {
                userService.addCoins(ticket.getCustomer().getId(),ticket.getPrice());
                LOG.info("Return " + ticket.getPrice() + " to user -> " + ticket.getCustomer());
            }
            sessionService.deleteSession(filmSession);
            LOG.info("Delete session");
        } catch (ServiceException e) {
            LOG.error("Can`t delete session " + e );
            throw new CommandException(e.getMessage(),e);
        }
        return request.getContextPath() + Pages.MAIN;
    }
}
