package com.example.cinema.web.comand;

import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.web.Pages;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.service.iface.UserService;
import com.example.cinema.db.service.UserServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class LoginCommand implements Command {
    private static final Logger LOG = Logger.getLogger(LoginCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {
        LOG.info("Start logging user");
        LOG.info("Current URI -> " + request.getRequestURI());
        HttpSession session = request.getSession();
        UserService userService = new UserServiceImpl();
        try {
            User user = userService.getUserByMail(request.getParameter("mail"));
            if (user == null || !(User.check(request.getParameter("password"), user.getPassword()))) {
                LOG.warn("Incorrect mail/password");
                session.setAttribute("exception", "Incorrect mail/password");
                return request.getContextPath() + Pages.LOGGING_JSP;
            }
            if (request.getParameter("remember") != null) {
                LOG.info("Set cookie");
                response.addCookie(new Cookie("user_id", Integer.toString(user.getId())));
            }
            session.setAttribute("user", user);
        }  catch (ServiceException e) {
            LOG.error("Can`t logging user " + e);
            throw new CommandException(e.getMessage(),e);
        }

        return request.getContextPath() + Pages.MAIN;
    }
}
