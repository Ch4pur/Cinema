package com.example.cinema.web.comand;

import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.iface.SessionService;
import com.example.cinema.db.service.SessionServiceImpl;
import com.example.cinema.web.Pages;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.service.iface.FilmService;
import com.example.cinema.db.service.FilmServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

class CreateFilmsPageCommand implements Command {
    private static final Logger LOG = Logger.getLogger(CreateFilmsPageCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {

        LOG.info("Start create main page");
        LOG.info("Current URI -> " + request.getRequestURI());

        FilmService filmService = new FilmServiceImpl();
        SessionService sessionService = new SessionServiceImpl();

        try {
            Set<Film> films = filmService.getAll();

            LOG.info("Get films -> " + films);
            Map<Integer,Integer> filmsSessions = sessionService.getNumberOfSessionsOfAllFilms();
            LOG.info("Get session number of each film -> " + filmsSessions);

            request.setAttribute("lang2", request.getAttribute("lang"));
            request.setAttribute("films", films);
            request.setAttribute("sessions",filmsSessions);
        } catch (ServiceException e) {
            LOG.error("Can`t create page " + e);
            throw new CommandException(e.getMessage(),e);
        }
        return Pages.MAIN_JSP;
    }
}
