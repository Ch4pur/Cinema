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
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;


public class EditFilmCommand implements Command {
    private static final Logger LOG = Logger.getLogger(EditFilmCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isAdmin()) {
            LOG.warn("Unauthorized login attempt");
            throw new CommandException("Trying to get on " + request.getRequestURI() + " from not admin");
        }
        FilmService filmService = new FilmServiceImpl();

        try {
            int filmId = Integer.parseInt(request.getParameter("film_id"));

            String programPath = request.getServletContext().getRealPath("/image/film/");

            Set<Genre> genres = filmService.getAllGenres();
            Set<Genre> filmGenres = new HashSet<>();
            for (Genre genre : genres) {
                if (request.getParameter(String.valueOf(genre.getId())) != null) {
                    filmGenres.add(genre);
                }
            }
            Film film = filmService.getFilmById(Integer.parseInt(request.getParameter("film_id")))
                    .setTitle(request.getParameter("filmTitle"))
                    .setProducersName(request.getParameter("producer"))
                    .setReleaseDate(Date.valueOf(request.getParameter("releaseDate")))
                    .setDuration(Time.valueOf(request.getParameter("duration") + ":00"))
                    .setDescription(request.getParameter("description"))
                    .setGenre(filmGenres);

            AgeRating ageRating = filmService.getAgeRatingByTitle(request.getParameter("ageRating"));

            film.setAgeRating(ageRating);
            Part filmImage = request.getPart("image-file");

            if (!filmImage.getSubmittedFileName().isEmpty()) {
                System.out.println(getFileExtension(filmImage.getSubmittedFileName()));
                if (!getFileExtension(filmImage.getSubmittedFileName()).equals("jpg")) {
                    session.setAttribute("exception", "Wrong file extension");
                    return request.getContextPath() + Pages.EDIT_FILM_PAGE + filmId;
                }

                filmImage.write(programPath + filmImage.getSubmittedFileName());
                film.setImagePath(filmImage.getSubmittedFileName());
            }

            filmService.updateFilm(film);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return request.getContextPath() + Pages.MAIN;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.indexOf(".") + 1);
    }
}
