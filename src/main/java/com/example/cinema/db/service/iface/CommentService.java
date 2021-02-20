package com.example.cinema.db.service.iface;

import com.example.cinema.db.entity.Comment;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;

import java.util.Set;

public interface CommentService {

    void addComment(Comment comment) throws ServiceException, TransactionException;
    void deleteComment(Comment comment) throws ServiceException, TransactionException;
    Comment getCommentById(int id) throws ServiceException, TransactionException;
    Set<Comment> getPartOfCommentsByFilm(int startWith, int pageSize, Film film) throws ServiceException, TransactionException;
    Set<Comment> getSetOfCommentsByFilm(Film film) throws ServiceException, TransactionException;
    Set<Comment> getSetOfCommentsByAuthor(User author) throws ServiceException;

}
