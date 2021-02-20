package com.example.cinema.web.comand;

import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.web.Pages;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.service.iface.UserService;
import com.example.cinema.db.service.UserServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;

class RegisterCommand implements Command {
    private static final Logger LOG = Logger.getLogger(RegisterCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException {
        LOG.info("Start register user");
        LOG.info("Current URI -> " + request.getRequestURI());

        UserService userService = new UserServiceImpl();
        User user = new User()
                .setMail(request.getParameter("mail"))
                .setFirstName(request.getParameter("first_name"))
                .setLastName(request.getParameter("last_name"))
                .setPhoneNumber(request.getParameter("phone"))
                .setPassword(User.getSaltedHash(request.getParameter("password")))
                .setAdmin(false)
                .setBirthday(Date.valueOf(request.getParameter("birthday")));
        LOG.info("Create user -> " + user);
        try {
            userService.addUser(user);
            LOG.info("Add user");
            HttpSession session = request.getSession();
            session.setAttribute("user", userService.getUserByMail(request.getParameter("mail")));
        } catch (ServiceException e) {
            LOG.info("Main is used -> "  + user.getMail());
            HttpSession session = request.getSession();
            session.setAttribute("exception","This mail is used");
            return request.getContextPath() + Pages.REGISTRATION_JSP;
        }
        return request.getContextPath()  + Pages.MAIN;
    }
}
