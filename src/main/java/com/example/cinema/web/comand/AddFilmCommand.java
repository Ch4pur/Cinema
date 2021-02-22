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
import java.util.Locale;
import java.util.Set;

class AddFilmCommand implements Command {
    private static final Logger LOG = Logger.getLogger(AddFilmCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException, TransactionException, ServletException{
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //только админ может добавлять фильм
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
                //проверяем чекбоксы в форме
                //если они включены то мы добавляем соответсвенный жанр в фильм
                if (request.getParameter(String.valueOf(genre.getId())) != null) {
                    filmGenres.add(genre);
                }
            }

            Film film = new Film()
                    .setTitle(request.getParameter("filmTitle"))
                    .setProducersName(request.getParameter("producer"))
                    .setReleaseDate(Date.valueOf(request.getParameter("releaseDate")))
                    .setDescription(request.getParameter("description"))
                    .setDuration(Time.valueOf(request.getParameter("duration").substring(0,5) + ":00"))
                    .setGenre(filmGenres);
            AgeRating ageRating = filmService.getAgeRatingByTitle(request.getParameter("ageRating"));

            film.setAgeRating(ageRating);
            Part filmImage = request.getPart("image-file");

            //если был добавлен файл для фильма
            if (!filmImage.getSubmittedFileName().isEmpty()) {
                LOG.info("Get image file -> " + filmImage.getSubmittedFileName());
                //проверка на формат jpg
                if (!getFileExtension(filmImage.getSubmittedFileName()).equals("jpg")) {
                    session.setAttribute("exception", "Wrong file extension");
                    return request.getContextPath() + Pages.ADD_FILM_PAGE;
                }
                //установка названия файла
                String fileName = film.getTitle().toLowerCase(Locale.ROOT).replace(" ", "_") + ".jpg";
                filmImage.write(programPath + fileName);
                LOG.info("Write image file to the project");
                film.setImagePath(fileName);
            }
            LOG.info("Get edited film -> " + film);
            filmService.addFilm(film);
        } catch (ServiceException e) {
            LOG.error("Can`t add the film "+ e);
            session.setAttribute("exception", e.getMessage());
            return request.getContextPath() + Pages.ADD_FILM_PAGE;
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
