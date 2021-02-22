package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.FilmServiceImpl;
import com.example.cinema.db.service.SessionServiceImpl;
import com.example.cinema.db.service.iface.FilmService;
import com.example.cinema.db.service.iface.SessionService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public class CreateAdminSessionPage implements Command {
    private static final Logger LOG = Logger.getLogger(CreateAdminSessionPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException, TransactionException {
        LOG.info("Start create page for edit sessions");
        LOG.info("Current URI -> " + request.getRequestURI());
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //заходить на данную страничку может только админ
        if (user == null || !user.isAdmin()) {
            LOG.warn("Unauthorized login attempt");
            throw new CommandException("Trying to get on " + request.getRequestURI() + " from not admin");
        }

        SessionService sessionService = new SessionServiceImpl();
        FilmService filmService = new FilmServiceImpl();


        String operation = request.getParameter("operation");
        LOG.info("Get operation on session -> " + operation);
        try {
            Film film;
            if ("add".equals(operation)) {
                int filmId = Integer.parseInt(request.getParameter("film_id"));
                LOG.info("Get film id -> " + filmId);
                film = filmService.getFilmById(filmId);
                LOG.info("Get film -> " + film);
                request.setAttribute("film", film);
            } else if ("edit".equals(operation)) {
                int sessionId = Integer.parseInt(request.getParameter("session_id"));
                LOG.info("Get session id ->" + sessionId);
                Session filmSession = sessionService.getSessionById(sessionId);
                LOG.info("Get session -> " + session);
                film = filmSession.getFilm();
                request.setAttribute("currSession", filmSession);
            } else {
                LOG.warn("Illegal operation -> " + operation);
                throw new CommandException("Illegal operation -> " + operation);
            }

            request.setAttribute("operation", operation);
            Map<String, List<Session>> sessions = sessionService.getFilmSessionsGroupedByDate(film);
            LOG.info("Get sessions grouped by date -> " + sessions);
            request.setAttribute("sessions", sessions);
        } catch (ServiceException e) {
            throw new CommandException(e.getMessage(),e);
        }

        return Pages.ADMIN_SESSION_JSP;
    }
}
