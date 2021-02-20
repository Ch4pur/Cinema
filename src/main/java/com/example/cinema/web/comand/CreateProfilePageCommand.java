package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.TicketServiceImpl;
import com.example.cinema.db.service.UserServiceImpl;
import com.example.cinema.db.service.iface.TicketService;
import com.example.cinema.db.service.iface.UserService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;

class CreateProfilePageCommand implements Command{
    private static final Logger LOG = Logger.getLogger(CreateProfilePageCommand.class);
    private static final int PAGE_SIZE = 5;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException, TransactionException {
        LOG.info("Start create profile page");
        LOG.info("Current URI -> " + request.getRequestURI());

        TicketService ticketService = new TicketServiceImpl();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            LOG.warn("Unauthorized login attempt");
            throw new CommandException("Trying to get on " + request.getRequestURI() + " from not admin");
        }

        try {
            int currentPage = request.getParameter("curr_page") == null? 0 : Integer.parseInt(request.getParameter("curr_page"));
            LOG.info("Get pagination current page -> " + currentPage);
            int numberOfElements = ticketService.getTicketsByCustomer(user).size();
            LOG.info("Get number of pagination elements -> " + numberOfElements);
            Set<Ticket> tickets = ticketService.getPartOfTicketsByCustomer(user, currentPage * PAGE_SIZE, PAGE_SIZE);
            LOG.info("Get tickets -> " + tickets);

            request.setAttribute("tickets", tickets);
            request.setAttribute("curr_page" , currentPage);
            session.setAttribute("pageSize",PAGE_SIZE);
            session.setAttribute("numberOfElements", numberOfElements);
        } catch (ServiceException e) {
            LOG.error("Can`t create profile " + e);
            throw new CommandException(e.getMessage(),e);
        }

        return Pages.PROFILE_JSP;
    }
}
