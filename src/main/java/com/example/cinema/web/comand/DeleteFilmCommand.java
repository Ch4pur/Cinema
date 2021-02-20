package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.FilmServiceImpl;
import com.example.cinema.db.service.iface.FilmService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeleteFilmCommand implements Command {
    private static final Logger LOG = Logger.getLogger(DeleteFilmCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isAdmin()) {
            LOG.warn("Unauthorized login attempt");
            throw new CommandException("Trying to get on " + request.getRequestURI() + " from not admin");
        }

        LOG.info("Start delete film");
        LOG.info("Current URI -> " + request.getRequestURI());

        FilmService filmService = new FilmServiceImpl();
        int filmId;

        filmId = Integer.parseInt(request.getParameter("filmId"));
        LOG.info("Get film id -> " + filmId);

        try {
            Film film = filmService.getFilmById(filmId);
            LOG.info("Get film -> " + film);
            filmService.deleteFilm(film);
            LOG.info("delete film");
        } catch (ServiceException e) {
            session.setAttribute("exception", e.getMessage());
            return request.getContextPath() + Pages.EDIT_FILM_PAGE + filmId;
        }

        return request.getContextPath() + Pages.MAIN;
    }
}
