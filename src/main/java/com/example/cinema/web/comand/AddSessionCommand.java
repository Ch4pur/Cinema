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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddSessionCommand implements Command {
    private static final Logger LOG = Logger.getLogger(AddSessionCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {
        LOG.info("Start add session");
        LOG.info("Current URI -> " + request.getRequestURI());
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) {
            LOG.warn("unauthorized user");
            throw new CommandException("unauthorized user");
        }
        SessionService sessionService = new SessionServiceImpl();
        FilmService filmService = new FilmServiceImpl();


        int filmId;
        filmId = Integer.parseInt(request.getParameter("film_id"));
        LOG.info("Get film id from request -> " + filmId);


        try {
            Film film = filmService.getFilmById(filmId);
            Timestamp dateTime = Timestamp.valueOf(request.getParameter("date") + " " + request.getParameter("time") + ":00");
            boolean threeD = request.getParameter("threeD") != null;
            Session filmSession = new Session()
                    .setFilm(film)
                    .setFullDate(dateTime)
                    .setThreeDSupporting(threeD);

            LOG.info("Set session -> " + filmSession);
            sessionService.addSession(filmSession);
            LOG.info("Add film session");
        } catch (ServiceException e) {
            LOG.info("Can`t add film " + e);
            session.setAttribute("exception", e.getMessage());
            return request.getContextPath() + Pages.ADMIN_SESSION_PAGE + "add" + "&film_id=" + filmId;
        }

        return request.getContextPath() + Pages.FILM + filmId;
    }
}
