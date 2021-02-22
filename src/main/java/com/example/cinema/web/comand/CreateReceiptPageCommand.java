package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.SessionServiceImpl;
import com.example.cinema.db.service.UserServiceImpl;
import com.example.cinema.db.service.iface.SessionService;
import com.example.cinema.db.service.iface.UserService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class CreateReceiptPageCommand implements Command {
    private static final Logger LOG = Logger.getLogger(CreateReceiptPageCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {
        LOG.info("Start create receipt page");
        LOG.info("Current URI -> " + request.getRequestURI());

        SessionService sessionService = new SessionServiceImpl();
        UserService userService = new UserServiceImpl();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Ticket> tickets = new ArrayList<>();
        //можно зайти только зарегистрированному пользователю
        if (user == null) {
            LOG.warn("Unauthorized login attempt");
            throw new CommandException("Trying to get on " + request.getRequestURI() + " from not admin");
        }
        session.removeAttribute("tickets");
        session.removeAttribute("total");
        try {
            user.setCoins(userService.getUserById(user.getId()).getCoins());
            LOG.info("Update users coin balance");
            Session filmSession = sessionService.getSessionById(Integer.parseInt(request.getParameter("session_id")));
            LOG.info("Get session -> " + filmSession);
            int resultPrice = 0;
            for (int i = 1; i <= Session.NUMBER_OF_ROWS; i++) {
                for (int j = 1; j <= Session.NUMBER_OF_COLUMNS; j++) {
                    if (request.getParameter(i +"-"+ j) != null) {
                        Ticket ticket = new Ticket().setCustomer(user)
                                .setOrderDate(Date.valueOf(LocalDate.now()))
                                .setPlaceRow(i).setPlaceColumn(j)
                                .setSession(filmSession);
                        ticket.computePrice();
                        LOG.info("Create ticket -> " + ticket);
                        resultPrice += ticket.getPrice();
                        tickets.add(ticket);
                    }
                }
            }
            request.setAttribute("session_id", request.getParameter("session_id"));
            session.setAttribute("tickets",tickets);
            session.setAttribute("total", resultPrice);
            LOG.info("Set total price -> " + resultPrice);
        } catch (ServiceException e) {
            LOG.error("Can`t create receipt " + e);
            throw new CommandException(e.getMessage(),e);
        }
        return Pages.RECEIPT_JSP;
    }
}
