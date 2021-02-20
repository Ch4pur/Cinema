package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.SessionServiceImpl;
import com.example.cinema.db.service.iface.SessionService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

class CreateTicketPanelCommand implements Command {
    private static final Logger LOG = Logger.getLogger(CreateTicketPanelCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {
        LOG.info("Start create ticket panel");
        LOG.info("Current URI -> " + request.getRequestURI());

        SessionService sessionService = new SessionServiceImpl();
        try {
            Session session = sessionService.getSessionById(Integer.parseInt(request.getParameter("session_id")));
            LOG.info("Get session -> " + session);
            List<List<Ticket>> seats = sessionService.getSeatsById(session.getId());

            request.setAttribute("seats", seats);
            request.setAttribute("session",session);
        } catch (ServiceException e) {
            LOG.error("Can`t create ticket panel " + e);
            throw new CommandException(e.getMessage(),e);
        }

        return Pages.TICKET_ORDER_JSP;
    }
}
