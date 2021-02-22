package com.example.cinema.web.comand;

import com.example.cinema.db.entity.AgeRating;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Genre;
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
import java.util.Set;

public class CreateEditFilmPage implements Command {

    private static final Logger LOG = Logger.getLogger(CreateEditFilmPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException, TransactionException {
        LOG.info("Start create page for editing films");
        LOG.info("Current URI -> " + request.getRequestURI());
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //заходить на данную страничку может только админ
        if (user == null || !user.isAdmin()) {
            LOG.warn("Unauthorized login attempt");
            throw new CommandException("Trying to get on " + request.getRequestURI() + " from not admin");
        }

        FilmService filmService = new FilmServiceImpl();
        int filmId;

        filmId = Integer.parseInt(request.getParameter("film_id"));
        LOG.info("Get film id -> " + filmId);


        try {
            Film film = filmService.getFilmById(filmId);
            LOG.info("Get film -> " + film);
            request.setAttribute("film", film);
            Set<Genre> genres = filmService.getAllGenres();
            LOG.info("Get all genres without film`s -> " + genres);
            Set<AgeRating> ageRatings = filmService.getAllAgeRatings();
            LOG.info("Get age rating -> " + ageRatings);

            request.setAttribute("genres", genres);
            request.setAttribute("ageRatings", ageRatings);
        } catch (ServiceException e) {
            LOG.error("Can`t create page " + e);
            throw new CommandException(e.getMessage(), e);
        }
        return Pages.EDIT_FILM_JSP;
    }
}
