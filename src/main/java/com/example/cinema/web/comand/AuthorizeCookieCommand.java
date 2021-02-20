package com.example.cinema.web.comand;

import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.UserServiceImpl;
import com.example.cinema.db.service.iface.UserService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class AuthorizeCookieCommand implements Command {
    private static final Logger LOG = Logger.getLogger(AuthorizeCookieCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {
        LOG.info("Start check cookie for autho");
        LOG.info("Current URI -> " + request.getRequestURI());

        UserService userService = new UserServiceImpl();
        Cookie[] cookies = request.getCookies();
        LOG.info("Get cookies");
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user_id")) {
                try {
                    LOG.info("Find user cookie");
                    HttpSession session = request.getSession();

                    User user = userService.getUserById(Integer.parseInt(cookie.getValue()));
                    LOG.info("Get user ->" + user);
                    if (user == null) {
                        LOG.warn("User from cookie was deleted");
                        Cookie cookie1 = new Cookie("user_id", "");
                        cookie1.setMaxAge(0);
                        response.addCookie(cookie1);
                        return Pages.LOGGING_JSP;
                    }
                    session.setAttribute("user", user);
                    LOG.info("Set user to session");
                } catch (ServiceException e) {
                    LOG.error("Can`t authorize user");
                    throw new CommandException(e.getMessage(), e);
                }
                return Pages.MAIN;
            }
        }
        return Pages.LOGGING_JSP;
    }
}
