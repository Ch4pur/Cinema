package com.example.cinema.db.service;

import com.example.cinema.db.dao.DAOFactory;
import com.example.cinema.db.dao.MyDAOFactory;
import com.example.cinema.db.dao.implementation.CommentDAO;
import com.example.cinema.db.dao.implementation.UserDAO;
import com.example.cinema.db.entity.Comment;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.DAOException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.iface.CommentService;
import com.example.cinema.db.service.iface.FilmService;
import com.example.cinema.db.service.iface.UserService;
import org.apache.log4j.Logger;

import java.util.Set;

public class CommentServiceImpl implements CommentService {

    private static final Logger LOG = Logger.getLogger(CommentServiceImpl.class);
    private static final FilmService filmService = new FilmServiceImpl();

    private final DAOFactory factory = MyDAOFactory.getInstance();
    private CommentDAO commentDao;
    private UserDAO userDao;

    @Override
    public void addComment(Comment comment) throws ServiceException, TransactionException {
        LOG.info("Adding comment starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            commentDao = factory.getCommentDao(transaction.getConnection());
            commentDao.add(comment);

        } catch (DAOException e) {
            LOG.error("Can`t add comment " + e);
            throw new ServiceException(e.getMessage(), e);
        }
        LOG.info("Adding comment has been finished");
    }

    @Override
    public void deleteComment(Comment comment) throws ServiceException, TransactionException {
        LOG.info("Deleting of comments starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            commentDao = factory.getCommentDao(transaction.getConnection());
            commentDao.deleteById(comment.getId());
        } catch (DAOException e) {
            LOG.error("Can`t delete comment " + e);
            throw new ServiceException(e.getMessage(), e);
        }
        LOG.info("Deleting of comments has been finished");
    }

    @Override
    public Comment getCommentById(int id) throws ServiceException, TransactionException {
        LOG.info("Getting comment by id -> " + id);
        try (Transaction transaction = Transaction.createTransaction()) {
            commentDao = factory.getCommentDao(transaction.getConnection());
            userDao = factory.getUserDAO(transaction.getConnection());
            Comment comment = commentDao.getById(id);
            comment.setAuthor(userDao.getById(commentDao.getAuthorId(comment.getId())));
            comment.setFilm(filmService.getFilmById(commentDao.getFilmId(comment.getId())));

            LOG.info("Get comment -> " + comment);
            LOG.info("Getting comment has been finished");
            return comment;
        } catch (DAOException e) {
            LOG.error("Can`t get comment " + e);
            throw new ServiceException(e.getMessage(), e);
        }

    }

    @Override
    public Set<Comment> getPartOfCommentsByFilm(int startWith, int pageSize, Film film) throws ServiceException, TransactionException {
        LOG.info("Getting part of comments starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            commentDao = factory.getCommentDao(transaction.getConnection());
            userDao = factory.getUserDAO(transaction.getConnection());
            Set<Comment> comments = commentDao.getSetByFilm(film, startWith, pageSize);

            for (Comment comment : comments) {
                comment.setAuthor(userDao.getById(commentDao.getAuthorId(comment.getId())));
                comment.setFilm(filmService.getFilmById(commentDao.getFilmId(comment.getId())));
            }

            LOG.info("Got comments -> " + comments);
            LOG.info("Getting part of comments has been finished");
            return comments;
        } catch (DAOException e) {
            LOG.error("Can`t get part of comments " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Comment> getSetOfCommentsByFilm(Film film) throws ServiceException, TransactionException {
        LOG.info("Getting set of comments starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            commentDao = factory.getCommentDao(transaction.getConnection());
            userDao = factory.getUserDAO(transaction.getConnection());
            Set<Comment> comments = commentDao.getSetByFilm(film);

            for (Comment comment : comments) {
                comment.setAuthor(userDao.getById(commentDao.getAuthorId(comment.getId())));
                comment.setFilm(filmService.getFilmById(commentDao.getFilmId(comment.getId())));
            }
            LOG.info("Got comments -> " + comments);
            LOG.info("Getting set of comments has been finished");
            return comments;
        } catch (DAOException e) {
            LOG.error("Can`t get set of comments  " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Comment> getSetOfCommentsByAuthor(User author) throws ServiceException {
        LOG.info("Getting set of comments starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            commentDao = factory.getCommentDao(transaction.getConnection());
            Set<Comment> comments = commentDao.getSetBySender(author);
            for (Comment comment : comments) {
                comment.setAuthor(userDao.getById(commentDao.getAuthorId(comment.getId())));
                comment.setFilm(filmService.getFilmById(commentDao.getFilmId(comment.getId())));
            }
            LOG.info("Got comments -> " + comments);
            LOG.info("Getting set of comments has been finished");
            return comments;
        } catch (DAOException | TransactionException e) {
            LOG.info("Can`t get set of comments  " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

}
