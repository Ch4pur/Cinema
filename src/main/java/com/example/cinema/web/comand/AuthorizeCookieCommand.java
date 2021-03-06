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
        String mail = "";
        String password = "";
        //в куки хранятся почта и пароль
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user_mail")) {
                mail = cookie.getValue();
            }
            if (cookie.getName().equals("user_password")) {
                password = cookie.getValue();
            }
        }
        try {
            LOG.info("Find user cookie");
            HttpSession session = request.getSession();

            User user = userService.getUserByMail(mail);
            LOG.info("Get user ->" + user);
            //валидация на юзера
            if (user != null && password.equals(user.getPassword())) {
                session.setAttribute("user", user);
                LOG.info("Set user to session");
                return Pages.MAIN;
            }
            LOG.warn("User from cookie was deleted");
            //если куки неправильные, то они удаляются.
            Cookie deletingCookieMail = new Cookie("user_mail", "");
            Cookie deletingCookiePassword = new Cookie("user_password", "");
            deletingCookieMail.setMaxAge(0);
            deletingCookiePassword.setMaxAge(0);
            response.addCookie(deletingCookieMail);
            response.addCookie(deletingCookiePassword);

            return Pages.LOGGING_JSP;
        } catch (ServiceException e) {
            LOG.error("Can`t authorize user");
            throw new CommandException(e.getMessage(), e);
        }
    }
}
