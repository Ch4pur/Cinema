package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Comment;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.CommentServiceImpl;
import com.example.cinema.db.service.UserServiceImpl;
import com.example.cinema.db.service.iface.CommentService;
import com.example.cinema.db.service.iface.UserService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


class DeleteCommentCommand implements Command {
    private static final Logger LOG = Logger.getLogger(DeleteCommentCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException, TransactionException {
        LOG.info("Start delete comment");
        LOG.info("Current URI -> " + request.getRequestURI());

        CommentService commentService = new CommentServiceImpl();


        try {
            int commentId = Integer.parseInt(request.getParameter("comment_id"));
            LOG.info("Get comment id -> " + commentId);
            Comment comment = commentService.getCommentById(commentId);
            LOG.info("Get comment -> " + comment);
            commentService.deleteComment(comment);
            LOG.info("Delete comment");

            return request.getContextPath() + Pages.FILM + comment.getFilm().getId();
        } catch (ServiceException e) {
            LOG.error("Can`t delete comment");
            throw new CommandException(e.getMessage(),e);
        }

    }
}
