package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Comment;
import com.example.cinema.db.entity.Session;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.CommentServiceImpl;
import com.example.cinema.db.service.SessionServiceImpl;
import com.example.cinema.db.service.iface.CommentService;
import com.example.cinema.db.service.iface.SessionService;
import com.example.cinema.web.Pages;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.service.iface.FilmService;
import com.example.cinema.db.service.FilmServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.Set;

class CreateFilmPageCommand implements Command {
    private static final Logger LOG = Logger.getLogger(CreateFilmPageCommand.class);
    private static final int PAGE_SIZE = 8;
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {
        LOG.info("Start create film page");
        LOG.info("Current URI -> " + request.getRequestURI());

        FilmService filmService = new FilmServiceImpl();
        CommentService commentService = new CommentServiceImpl();
        SessionService sessionService = new SessionServiceImpl();
        HttpSession session = request.getSession();
        try {
            int id = Integer.parseInt(request.getParameter("film_id"));
            LOG.info("Get film id -> " + id);
            //пагинация страницы
            int currentPage = request.getParameter("curr_page") == null? 0 : Integer.parseInt(request.getParameter("curr_page"));
            LOG.info("Get current pagination page -> " + currentPage);
            Film film = filmService.getFilmById(id);
            LOG.info("Get film -> " + film);
            int numberOfElements = commentService.getSetOfCommentsByFilm(film).size();
            LOG.info("Get number of pagination elements");
            Map<String, List<Session>> sessions = sessionService.getFilmSessionsGroupedByDate(film);
            LOG.info("Get sessions grouped by date -> " + sessions);

            Set<Comment> comments = commentService.getPartOfCommentsByFilm(currentPage * PAGE_SIZE,PAGE_SIZE, film);
            LOG.info("Get comments -> " + comments);

            session.setAttribute("pageSize",PAGE_SIZE);
            session.setAttribute("numberOfElements", numberOfElements);
            request.setAttribute("curr_page", currentPage);
            request.setAttribute("film", film);
            request.setAttribute("sessions", sessions);
            request.setAttribute("comments", comments);
        } catch (ServiceException e) {
            LOG.error("Can`t create film page");
            throw new CommandException(e.getMessage(),e);
        }
        return Pages.FILM_JSP;
    }
}
