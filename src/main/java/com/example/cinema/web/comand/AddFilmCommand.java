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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

class AddFilmCommand implements Command {
    private static final Logger LOG = Logger.getLogger(AddFilmCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException, TransactionException, ServletException{
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) {
            LOG.warn("unauthorized user");
            throw new CommandException("unauthorized user");
        }

        FilmService filmService = new FilmServiceImpl();

        try {
            String programPath = request.getServletContext().getRealPath("/image/film/");

            Set<Genre> genres = filmService.getAllGenres();
            Set<Genre> filmGenres = new HashSet<>();
            for (Genre genre : genres) {
                if (request.getParameter(String.valueOf(genre.getId())) != null) {
                    filmGenres.add(genre);
                }
            }

            Film film = new Film()
                    .setTitle(request.getParameter("filmTitle"))
                    .setProducersName(request.getParameter("producer"))
                    .setReleaseDate(Date.valueOf(request.getParameter("releaseDate")))
                    .setDescription(request.getParameter("description"))
                    .setGenre(filmGenres);
            if (!(request.getParameter("duration") + ":00").equals("00:00")) {
                film.setDuration(Time.valueOf(request.getParameter("duration") + ":00"));
            }
            AgeRating ageRating = filmService.getAgeRatingByTitle(request.getParameter("ageRating"));

            film.setAgeRating(ageRating);
            Part filmImage = request.getPart("image-file");

            if (!filmImage.getSubmittedFileName().isEmpty()) {
                LOG.info("Get image file -> " + filmImage.getSubmittedFileName());
                if (!getFileExtension(filmImage.getSubmittedFileName()).equals("jpg")) {
                    session.setAttribute("exception", "Wrong file extension");
                    return request.getContextPath() + Pages.ADD_FILM_PAGE;
                }

                filmImage.write(programPath + filmImage.getSubmittedFileName());
                LOG.info("Write image file to the project");
                film.setImagePath(filmImage.getSubmittedFileName());
            }
            LOG.info("Get edited film -> " + film);
            filmService.addFilm(film);
        } catch (ServiceException e) {
            LOG.error("Can`t add the film "+ e);
            throw new CommandException(e.getMessage(), e);
        } catch (IOException e) {
            LOG.error("Error with downloading file -> " + e);
            throw new ServletException(e);
        }
        return request.getContextPath() + Pages.MAIN;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.indexOf(".") + 1);
    }
}
