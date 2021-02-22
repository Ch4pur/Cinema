package com.example.cinema.db.service;

import com.example.cinema.db.dao.MyDAOFactory;
import com.example.cinema.db.dao.implementation.AgeRatingDAO;
import com.example.cinema.db.dao.implementation.FilmDAO;
import com.example.cinema.db.dao.implementation.GenreDAO;
import com.example.cinema.db.entity.AgeRating;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Genre;
import com.example.cinema.db.exception.DAOException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.iface.FilmService;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class FilmServiceImpl implements FilmService {

    private static final Logger LOG = Logger.getLogger(FilmServiceImpl.class);

    private final MyDAOFactory factory = MyDAOFactory.getInstance();

    private FilmDAO filmDao;
    private GenreDAO genreDao;
    private AgeRatingDAO ageRatingDao;

    @Override
    public Set<Film> getAll() throws ServiceException, TransactionException {
        LOG.info("Getting all films starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            filmDao = factory.getFilmDao(transaction.getConnection());
            genreDao = factory.getGenreDao(transaction.getConnection());
            ageRatingDao = factory.getAgeRatingDao(transaction.getConnection());

            Set<Film> films = new TreeSet<>(Comparator.comparing(Film::getTitle));
            films.addAll(filmDao.getAll());

            for (Film film : films) {
                int ageRatingId = filmDao.getAgeRatingIdById(film.getId());
                film.setAgeRating(ageRatingDao.getById(ageRatingId))
                        .setGenre(genreDao.getSetByFilm(film));
            }
            LOG.info("Got films -> " + films);
            LOG.info("Getting all films has been finished");
            return films;
        } catch (DAOException e) {
            LOG.error("Can`t get all films " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Film getFilmByTitle(String name) throws ServiceException, TransactionException {
        LOG.info("Getting set of films starts");
        try (Transaction transaction = Transaction.createTransaction()){
            filmDao = factory.getFilmDao(transaction.getConnection());
            genreDao = factory.getGenreDao(transaction.getConnection());
            ageRatingDao = factory.getAgeRatingDao(transaction.getConnection());

            Film film = filmDao.getByTitle(name);
            film.setAgeRating(ageRatingDao.getById(filmDao.getAgeRatingIdById(film.getId())))
                    .setGenre(genreDao.getSetByFilm(film));
            LOG.info("Got a film -> " + film);
            LOG.info("Getting set of films has been finished");
            return film;
        } catch (DAOException e) {
            LOG.error("Can`t get set of films " + e);
            throw new ServiceException(e.getMessage(), e);
        }

    }

    @Override
    public Film getFilmById(int id) throws ServiceException, TransactionException {
        LOG.info("Getting film by id starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            filmDao = factory.getFilmDao(transaction.getConnection());
            genreDao = factory.getGenreDao(transaction.getConnection());
            ageRatingDao = factory.getAgeRatingDao(transaction.getConnection());

            Film film = filmDao.getById(id);
            film.setAgeRating(ageRatingDao.getById(filmDao.getAgeRatingIdById(film.getId())))
                    .setGenre(genreDao.getSetByFilm(film));

            LOG.info("Got film -> " + film);
            LOG.info("Getting a film has been finished");
            return film;
        } catch (DAOException e) {
            LOG.error("Can`t get a film " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public AgeRating getAgeRatingById(int id) throws ServiceException, TransactionException {
        LOG.info("Getting age rating");
        try (Transaction transaction = Transaction.createTransaction()) {
            ageRatingDao = factory.getAgeRatingDao(transaction.getConnection());
            AgeRating ageRating = ageRatingDao.getById(id);

            LOG.info("Got age rating -> " + ageRating);
            return ageRating;
        } catch (DAOException e) {
            LOG.error("Can`t get age rating " + e);
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public Set<Genre> getAllGenres() throws ServiceException, TransactionException {
        LOG.info("Start get all genres");
        try (Transaction transaction = Transaction.createTransaction()) {
            genreDao = factory.getGenreDao(transaction.getConnection());


            Set<Genre> genres = genreDao.getAll();
            LOG.info("Get genres -> " + genres);
            return genres;
        } catch (DAOException e) {
            LOG.error("Can`t get all genres " + e);
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public void addFilm(Film film) throws ServiceException, TransactionException {
        LOG.info("Adding the film starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            filmDao = factory.getFilmDao(transaction.getConnection());
            genreDao = factory.getGenreDao(transaction.getConnection());

            //добавление нового фильма
            filmDao.add(film);
            film = filmDao.getByTitle(film.getTitle()).setGenre(film.getGenres());
            //установка жанров в БД
            setGenresToFilm(film,film.getGenres());

        } catch (DAOException e) {
            LOG.error("Can`t add the film " + e);
            throw new ServiceException(e.getMessage(), e);
        }
        LOG.info("Adding the film has been finished");
    }

    @Override
    public void updateFilm(Film film) throws ServiceException, TransactionException {
        LOG.info("Updating the film starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            filmDao = factory.getFilmDao(transaction.getConnection());
            genreDao = factory.getGenreDao(transaction.getConnection());

            filmDao.update(film);
            setGenresToFilm(film,film.getGenres());
        }catch (DAOException e) {
            LOG.error("Can`t update the film " + e);
            throw new ServiceException(e.getMessage(), e);
        }
        LOG.info("Updating the film has been finished");
    }

    @Override
    public void deleteFilm(Film film) throws ServiceException, TransactionException {
        LOG.info("Deleting the film starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            filmDao = factory.getFilmDao(transaction.getConnection());

            filmDao.deleteById(film.getId());
        } catch (DAOException e) {
            LOG.error("Can`t delete the film " + e);
            throw new ServiceException(e.getMessage(), e);
        }
        LOG.info("Deleting the film has been finished");
    }

    @Override
    public void setGenresToFilm(Film film, Set<Genre> genres) throws ServiceException, TransactionException {
        LOG.info("Start set genres to film");
        try (Transaction transaction = Transaction.createTransaction()){
            genreDao = factory.getGenreDao(transaction.getConnection());

            genreDao.deleteGenresFromFilm(film);
            LOG.info("Delete old film`s genres");
            for (Genre genre : genres) {
                genreDao.addGenreToFilm(genre,film);
            }
            LOG.info("Set genres to film");
        } catch (DAOException e) {
            LOG.error("Can`t set genres " + e);
            throw new ServiceException(e.getMessage(),e);
        }

    }

    @Override
    public Set<AgeRating> getAllAgeRatings() throws ServiceException, TransactionException {
        LOG.info("Getting all age rating starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            ageRatingDao = factory.getAgeRatingDao(transaction.getConnection());
            Set<AgeRating> ageRatings = ageRatingDao.getAll();

            LOG.info("Got age ratings -> " + ageRatings);
            LOG.info("Getting all age ratings has been finished");
            return ageRatings;
        } catch (DAOException e) {
            LOG.error("Can`t get all age ratings");
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public AgeRating getAgeRatingByTitle(String title) throws ServiceException, TransactionException {
        LOG.info("Start get age rating by title -> " + title);
        try(Transaction transaction = Transaction.createTransaction()) {
            ageRatingDao = factory.getAgeRatingDao(transaction.getConnection());

            AgeRating ageRating = ageRatingDao.getByTitle(title);
            LOG.info("Get age rating -> " + ageRating);
            return ageRating;
        } catch (DAOException e) {
            LOG.error("Can`t get age rating " + e);
            throw new ServiceException(e.getMessage(),e);
        }

    }
}
