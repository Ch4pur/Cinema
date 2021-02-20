package com.example.cinema.web.comand;

import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class LogoutCommand implements Command {
    private static final Logger LOG = Logger.getLogger(LogoutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Start logout user");
        LOG.info("Current URI -> " + request.getRequestURI());
        HttpSession session = request.getSession();
        session.invalidate();
        LOG.info("Invalidate session");
        Cookie cookieRemove = new Cookie("user_id", "");
        cookieRemove.setMaxAge(0);
        response.addCookie(cookieRemove);
        LOG.info("Clean cookie");

        return Pages.MAIN;
    }
}
