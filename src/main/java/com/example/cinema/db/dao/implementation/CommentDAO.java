package com.example.cinema.db.dao.implementation;

import com.example.cinema.db.dao.iface.CommentDAOInterface;
import com.example.cinema.db.entity.Comment;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.DAOException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class CommentDAO extends AbstractDAO<Comment> implements CommentDAOInterface<Comment> {

    private static final Logger LOG = Logger.getLogger(CommentDAO.class);

    private static final String GET_BY_ID = "Select * From Comments Where Comment_id = ?";

    private static final String GET_ALL = "Select * From Comments";
    private static final String GET_SET_BY_SENDER = "Select * From Comments Where Author_id = ?";
    private static final String GET_SET_BY_FILM = "Select * From Comments Where Film_id = ?";
    private static final String GET_SET_BY_FILM_WITH_PAGINATION = "Select * From Comments Where Film_id = ? Limit ? Offset ?";
    private static final String ADD = "Insert Into Comments (Film_id, Author_id, Content, Date_of_writing) Value (?,?,?,?)";

    private static final String UPDATE_BY_ID = "Update Comments " +
            "Set Film_id = ?, Author_id = ?, Content = ?, Date_of_writing = ? " +
            "Where Comment_id = ?";
    private static final String DELETE_BY_ID = "Delete From Comments Where Comment_id = ?";
    private static final String GET_AUTHOR_ID = "Select Author_id From Comments Where Comment_id =?";
    private static final String GET_FILM_ID = "Select Film_id From Comments Where Comment_id = ?";
    public CommentDAO(Connection connection) {
        super(connection);
        LOG.info("CommentDAO is created with connection ->" + connection);
    }

    @Override
    public Set<Comment> getSetBySender(User sender) throws DAOException {
        LOG.info("Getting set of comments by sender -> " + sender);
        return new LinkedHashSet<>(getAllByStatement(GET_SET_BY_SENDER,
                String.valueOf(sender.getId())));
    }

    @Override
    public Set<Comment> getSetByFilm(Film film) throws DAOException {
        LOG.info("Getting set of comments by film -> " + film);
        return new LinkedHashSet<>(getAllByStatement(GET_SET_BY_FILM,
                String.valueOf(film.getId())));
    }

    @Override
    public Set<Comment> getSetByFilm(Film film, int startWith, int pageSize) throws DAOException {
        LOG.info("Getting part of comments by film -> " + film + " from " + startWith + " to " + (startWith + pageSize));
        return new LinkedHashSet<>(getPartByStatement(startWith,
                pageSize, GET_SET_BY_FILM_WITH_PAGINATION,
                String.valueOf(film.getId())));
    }

    @Override
    public int getAuthorId(int id) throws DAOException {
        LOG.info("Getting an author of comment by id -> " + id);
        return getForeignKeyId(GET_AUTHOR_ID, id);
    }

    @Override
    public int getFilmId(int id) throws DAOException {
        LOG.info("Getting a film of comment by id -> " + id);
        return getForeignKeyId(GET_FILM_ID, id);
    }

    @Override
    public Comment getById(int id) throws DAOException {
        LOG.info("Getting the comment by id -> " + id);
        return getByStatement(GET_BY_ID, String.valueOf(id));
    }

    @Override
    public Set<Comment> getAll() throws DAOException {
        LOG.info("Getting all comments");
        return new LinkedHashSet<>(getAllByStatement(GET_ALL));
    }

    @Override
    public void add(Comment comment) throws DAOException {
        LOG.info("Adding new comment -> " + comment);
        updateByStatement(ADD,
                String.valueOf(comment.getFilm().getId()),
                String.valueOf(comment.getAuthor().getId()),
                comment.getContent(),
                comment.getDateOfWriting().toString());
    }

    @Override
    public void update(Comment comment) throws DAOException {
        LOG.info("Updating the comment -> " + comment);
        updateByStatement(UPDATE_BY_ID,
                String.valueOf(comment.getFilm().getId()),
                String.valueOf(comment.getAuthor().getId()),
                comment.getContent(),
                comment.getDateOfWriting().toString(),
                String.valueOf(comment.getId()));

    }

    @Override
    public void deleteById(int id) throws DAOException {
        LOG.info("Deleting a comment by id -> " + id);
        updateByStatement(DELETE_BY_ID,
                String.valueOf(id));
    }

    @Override
    protected Comment createEntity(ResultSet resultSet) throws SQLException {
        return new Comment()
                .setContent(resultSet.getString("Content"))
                .setDateOfWriting(resultSet.getDate("Date_of_writing"))
                .setId(resultSet.getInt("Comment_id"));
    }

}
