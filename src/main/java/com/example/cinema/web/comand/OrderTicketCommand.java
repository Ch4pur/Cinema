package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;

import com.example.cinema.db.service.TicketServiceImpl;

import com.example.cinema.db.service.iface.TicketService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;



public class OrderTicketCommand implements Command {
    private static final Logger LOG = Logger.getLogger(OrderTicketCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException {
        LOG.info("Start logging user");
        LOG.info("Current URI -> " + request.getRequestURI());

        HttpSession session = request.getSession();
        TicketService ticketService = new TicketServiceImpl();

        List<Ticket> tickets = (List<Ticket>) session.getAttribute("tickets");
        LOG.info("Get tickets -> " + tickets);
        try {
            ticketService.addTickets(tickets);
            LOG.info("Add ticket");
        } catch (ServiceException e) {
              session.setAttribute("exception", e.getMessage());
              return request.getContextPath() + Pages.TICKET_ORDER_PANEL+ tickets.get(0).getSession().getId();
        }
        return request.getContextPath() + Pages.MAIN;
    }
}
