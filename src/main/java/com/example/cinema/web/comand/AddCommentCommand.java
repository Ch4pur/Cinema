package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Comment;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.CommentServiceImpl;
import com.example.cinema.db.service.FilmServiceImpl;
import com.example.cinema.db.service.iface.CommentService;
import com.example.cinema.db.service.iface.FilmService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;

class AddCommentCommand implements Command {
    private static final Logger LOG = Logger.getLogger(AddCommentCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException,TransactionException {
        LOG.info("Start add comment");
        LOG.info("Current URI -> " + request.getRequestURI());
        HttpSession session = request.getSession();
        LOG.info("Adding comment starts");
        User user = (User) session.getAttribute("user");
        LOG.info("Get author -> " + user);
        //неавторизированному пользователю нельзя писать комментарий
        if (user == null) {
            LOG.warn("Unregister guest try to make a comment");
            session.setAttribute("exception", "you must to authorize/register to make a comment");
            return request.getContextPath()  + Pages.AUTHORIZATION;
        }
        CommentService commentService = new CommentServiceImpl();
        FilmService filmService = new FilmServiceImpl();

        try {
            Film film = filmService.getFilmById(Integer.parseInt(request.getParameter("filmId")));
            LOG.info("Get film of comment -> " + film);
            Comment comment = new Comment()
                    .setAuthor(user)
                    .setDateOfWriting(Date.valueOf(LocalDate.now()))
                    .setContent(request.getParameter("comment"))
                    .setFilm(film);
            LOG.info("Create comment -> " + comment);
            commentService.addComment(comment);
            LOG.info("Add comment has been finished");
        } catch (ServiceException e) {
            LOG.error("Can`t add comment ", e);
            throw new CommandException(e.getMessage(), e);
        }
        return request.getContextPath() + Pages.FILM + request.getParameter("filmId");
    }
}
