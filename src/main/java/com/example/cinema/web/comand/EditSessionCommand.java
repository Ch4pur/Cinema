package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.SessionServiceImpl;
import com.example.cinema.db.service.iface.SessionService;
import com.example.cinema.web.Pages;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

public class EditSessionCommand implements Command {
    private static final Logger LOG = Logger.getLogger(EditSessionCommand.class);

    @Override

    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {
        LOG.info("Start edit session");
        LOG.info("Current URI -> " + request.getRequestURI());
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isAdmin()) {
            LOG.warn("Unauthorized login attempt");
            throw new CommandException("Trying to get on " + request.getRequestURI() + " from not admin");
        }

        SessionService sessionService = new SessionServiceImpl();
        int sessionId;

        sessionId = Integer.parseInt(request.getParameter("session_id"));

        LOG.info("Get session id -> " + sessionId);
        Session filmSession = null;
        try {
            Timestamp dateTime = Timestamp.valueOf(request.getParameter("date") + " " + request.getParameter("time") + ":00");
            boolean threeD = request.getParameter("threeD") != null;
            filmSession = sessionService.getSessionById(sessionId)
                    .setFullDate(dateTime)
                    .setThreeDSupporting(threeD);
            LOG.info("Create session -> " + filmSession);
            sessionService.updateSession(filmSession);
            LOG.info("Update session");
        } catch (ServiceException e) {
            session.setAttribute("exception", e.getMessage());
            LOG.warn("Can`t update session " + e.getMessage());
            return request.getContextPath() + Pages.ADMIN_SESSION_PAGE + "edit&session_id=" + filmSession.getId() + "&film_id=" + filmSession.getFilm().getId();
        }

        return request.getContextPath() + Pages.FILM + filmSession.getFilm().getId();
    }
}
