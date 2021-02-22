package com.example.cinema.db.service;

import com.example.cinema.db.dao.DAOFactory;
import com.example.cinema.db.dao.MyDAOFactory;
import com.example.cinema.db.dao.implementation.FilmDAO;
import com.example.cinema.db.dao.implementation.SessionDAO;
import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.exception.DAOException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.iface.FilmService;
import com.example.cinema.db.service.iface.SessionService;
import com.example.cinema.db.service.iface.TicketService;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class SessionServiceImpl implements SessionService {

    private static final Logger LOG = Logger.getLogger(SessionServiceImpl.class);

    private final DAOFactory factory = MyDAOFactory.getInstance();

    private final FilmService filmService = new FilmServiceImpl();

    private FilmDAO filmDao;
    private SessionDAO sessionDao;

    @Override
    public Session getSessionById(int id) throws ServiceException, TransactionException {
        LOG.info("Getting session starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            sessionDao = factory.getSessionDao(transaction.getConnection());

            Session session = sessionDao.getById(id);
            session.setFilm(filmService.getFilmById((sessionDao.getFilmIdById(id))));

            LOG.info("Got session -> " + session);
            LOG.info("Getting sessions has been finished");
            return session;
        } catch (DAOException e) {
            LOG.error("Can`t get session by id " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Session> getAll() throws ServiceException, TransactionException {
        LOG.info("Getting all sessions");
        try (Transaction transaction = Transaction.createTransaction()) {
            sessionDao = factory.getSessionDao(transaction.getConnection());
            Set<Session> sessions = sessionDao.getAll();
            for (Session session : sessions) {
                session.setFilm(filmService.getFilmById(sessionDao.getFilmIdById(session.getId())));
            }
            LOG.info("Got sessions -> " + sessions);
            LOG.info("Getting all session has been finished");
            return sessions;
        } catch (DAOException e) {
            LOG.error("Can`t get sessions ", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Session> getSessionsByFilm(Film film) throws ServiceException, TransactionException {
        LOG.info("Getting sessions starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            sessionDao = factory.getSessionDao(transaction.getConnection());

            Set<Session> sessions = sessionDao.getSetByFilm(film);

            for (Session session : sessions) {
                session.setFilm(filmService.getFilmById(sessionDao.getFilmIdById(session.getId())));
            }
            LOG.info("Got sessions -> " + sessions);
            LOG.info("Getting sessions has been finished");
            return sessions;
        } catch (DAOException e) {
            LOG.info("Can`t get sessions ", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Session> getSessionsByThreeDSupporting(boolean threeDSupporting) throws ServiceException, TransactionException {
        LOG.info("Getting sessions starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            sessionDao = factory.getSessionDao(transaction.getConnection());

            Set<Session> sessions = sessionDao.getSetBy3DSupporting(threeDSupporting);
            for (Session session : sessions) {
                session.setFilm(filmService.getFilmById(sessionDao.getFilmIdById(session.getId())));
            }

            LOG.info("Got sessions -> " + sessions);
            LOG.info("Getting sessions has been finished -> " + sessions);
            return sessions;
        } catch (DAOException e) {
            LOG.error("Can`t get sessions " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Session> getSessionsByDate(Date date) throws ServiceException, TransactionException {
        LOG.info("Getting sessions starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            sessionDao = factory.getSessionDao(transaction.getConnection());

            Set<Session> sessions = sessionDao.getSetByDate(date);
            for (Session session : sessions) {
                session.setFilm(filmService.getFilmById(sessionDao.getFilmIdById(session.getId())));
            }
            LOG.info("Got sessions -> " + sessions);
            LOG.info("Getting sessions has been finished");
            return sessions;
        } catch (DAOException e) {
            LOG.error("Can`t get sessions " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    //получение кол-ва сеансов всех фильмов
    //хранение в мапе (id фильма, кол-во сеансов)
    @Override
    public Map<Integer, Integer> getNumberOfSessionsOfAllFilms() throws ServiceException, TransactionException {
        LOG.info("Getting number of sessions starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            filmDao = factory.getFilmDao(transaction.getConnection());
            sessionDao = factory.getSessionDao(transaction.getConnection());

            Map<Integer, Integer> res = new HashMap<>();
            Set<Film> films = filmDao.getAll();

            for (Film film : films) {
                int numberOfSessions = sessionDao.getSetByFilm(film).size();
                res.put(film.getId(), numberOfSessions);
            }
            LOG.info("got number of sessions -> " + films);
            return res;
        } catch (DAOException e) {
            //logging
            throw new ServiceException(e.getMessage(), e);
        }
    }

    //получение сесансов сгруппрованных по датам
    //хранится в мапе(дата сенса, сенса отсортированные по времени показа)
    @Override
    public Map<String, List<Session>> getFilmSessionsGroupedByDate(Film film) throws ServiceException, TransactionException {
        LOG.info("Getting session grouped by date starts");
        try (Transaction transaction = Transaction.createTransaction()) {
            sessionDao = factory.getSessionDao(transaction.getConnection());
            Set<Session> sessions = sessionDao.getSetByFilm(film);
            LOG.info("Got all sessions -> " + sessions);
            Map<String, List<Session>> res = new LinkedHashMap<>();
            for (Session session : sessions) {
                String date = session.getDate().toString();

                if (!res.containsKey(date)) {
                    res.put(date, new ArrayList<>());
                }
                res.get(date).add(session);
            }
            LOG.info("Unsorted sessions map -> " + res);
            res = sortByDate(res);
            //сортировка по времени
            res.values().forEach(times -> times.sort(Comparator.comparing(Session::getTime)));
            LOG.info("Sessions map sorted by date -> " + res);
            LOG.info("Getting sessions grouped by date has been finished");
            return res;
        } catch (DAOException e) {
            LOG.error("Can`t get sessions grouped by date " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private Map<String, List<Session>> sortByDate(Map<String, List<Session>> sessions) {
        LOG.info("Start sort by date");
        List<Map.Entry<String, List<Session>>> entries = new ArrayList<>(sessions.entrySet());
        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        entries.sort((entry1, entry2) -> {
            try {
                return dateFormat.parse(entry1.getKey()).before(dateFormat.parse(entry2.getKey())) ? -1 : 1;
            } catch (ParseException e) {
                LOG.error(e);
            }
            return 0;
        });
        LOG.info("Sorted by date map -> " + entries);
        LOG.info("Finish sort by date");
        return entries.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
    }

    //создание листа кол-во рядов х кол-во сидений
    // где все сиденья свободные
    private List<List<Ticket>> createEmptyHall() {
        List<List<Ticket>> ticketList = new ArrayList<>();
        for (int i = 0; i < Session.NUMBER_OF_ROWS; i++) {
            List<Ticket> sessionList = new ArrayList<>(8);
            for (int j = 0; j < Session.NUMBER_OF_COLUMNS; j++) {
                sessionList.add(new Ticket());
            }
            ticketList.add(sessionList);
        }
        return ticketList;
    }

    // получения состояний каждого места по id сеанса
    // если id тикета в листе равен -1, то сиденье свободно
    @Override
    public List<List<Ticket>> getSeatsById(int id) throws ServiceException, TransactionException {
        LOG.info("Start get seats by sessions id -> " + id);
        List<List<Ticket>> seats = createEmptyHall();
        try (Transaction transaction = Transaction.createTransaction()) {
            sessionDao = factory.getSessionDao(transaction.getConnection());
            TicketService ticketService = new TicketServiceImpl();

            Session session = sessionDao.getById(id);
            LOG.info("Got session -> " + session);
            ticketService.getTicketsBySession(session)
                    .forEach(ticket -> seats.get(ticket.getPlaceRow() - 1)
                            .set(ticket.getPlaceColumn() - 1, ticket));


            StringBuilder log = new StringBuilder();
            for (List<Ticket> seatCols : seats) {
                for (Ticket seatState : seatCols) {
                    log.append(seatState.getId() == -1 ? "FREE" : "ORDERED").append("\t");
                }
                log.append(System.lineSeparator());
            }

            LOG.info("Seats of session -> " + session + System.lineSeparator() + log);
            return seats;
        } catch (DAOException e) {
            LOG.info("Can`t get seats of sessions " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    //получение конца сенанса
    private Time getEndTimeOfSession(Session session) {
        LOG.info("Start getting endtime of the session -> " + session);
//        System.out.println(session);
        long startingOfFilm = session.getFullDate().getTime();
        long filmDuration = session.getFilm().getDuration().getTime();
        //время на рекламу
        long addTime = Time.valueOf("00:20:00").getTime();
        //время промежутка между сенсами
        long sessionGap = Time.valueOf("00:20:00").getTime();
        //при каждом суммировании времен результат становиться равным на timeZoneOffset времени дольше
        // правильного
        //  поэтому отмимается timeZoneOffset от результата столько раз, сколько было произведено суммирование
        long timeZoneOffset = -(Time.valueOf("00:00:00").getTime());

        Time res = new Time(startingOfFilm + filmDuration + addTime + sessionGap + timeZoneOffset * 3);
        LOG.info("Got time -> " + res);
//        System.out.println("End -> " + res);
        return res;
    }

    @Override
    public void addSession(Session newSession) throws ServiceException, TransactionException {
        LOG.info("Adding session starts");
        try (Transaction transaction = Transaction.createTransaction()) {

            //сеанс добавить нельзя, если
            //дата сеанса в прошедшем времени
            if (isInThePast(newSession)) {
                LOG.warn("Session date is in the past -> " + newSession.getDate());
                throw new ServiceException("session date is in the past");
            }
            //дата находится вне промежутка рабочего дня (9:00 - 22:00)
            if (isOutOfTime(newSession)) {
                LOG.warn("Session outside the cinema working time -> " + newSession.getDate());
                throw new ServiceException("Session outside the cinema working time");
            }
            //дата попадает в промежуток другого сеанса
            if (!isFittedInTime(newSession, getSessionsByDate(newSession.getDate()))) {
                LOG.warn("Another session is in progress during this one -> " + newSession);
                throw new ServiceException("Another session is in progress during this one");
            }
            sessionDao = factory.getSessionDao(transaction.getConnection());
            sessionDao.add(newSession);
            LOG.info("Adding session has been finished");
        } catch (DAOException e) {
            LOG.error("Can`t add session " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private boolean isFittedInTime(Session session, Set<Session> oldSessions) {
        LOG.info("Start checking does session affect the other`s schedule");
        Session buff = null;
        //если сенсов в этот день нет, то дата точно не попадает в чужие рамки
        if (oldSessions == null || oldSessions.isEmpty()) {
            return true;
        }

        for (Session sessionWithTheSameDate : oldSessions) {
            if (buff == null) {
                System.out.println("session 1 time -> " + sessionWithTheSameDate.getTime() + " - " + getEndTimeOfSession(sessionWithTheSameDate));
                System.out.println("newSessions time ->" + session.getTime() + " - " + getEndTimeOfSession(session));
                LOG.info("session 1 time -> " + sessionWithTheSameDate.getTime() + " - " + getEndTimeOfSession(sessionWithTheSameDate));
                LOG.info("newSessions time ->" + session.getTime() + " - " + getEndTimeOfSession(session));
                //если конец нового сеанса до начала существующего, учитывая что сеанс попал в рабочее время
                // то его можно добавить
                if (getEndTimeOfSession(session).toLocalTime().isBefore(sessionWithTheSameDate.getTime().toLocalTime())) {
                    return true;
                }
            } else {
                LOG.info("session 1 time -> " + buff.getTime() + " - " + getEndTimeOfSession(buff));
                LOG.info("newSessions time ->" + session.getTime() + " - " + getEndTimeOfSession(session));
                LOG.info("session 2 time -> " + sessionWithTheSameDate.getTime() + " - " + getEndTimeOfSession(sessionWithTheSameDate));
                System.out.println("----------------------------------------------------------");
                // если начало нового сеанса после конца предидущего и конец - перед следущего
                // то его можно добавить
                if (getEndTimeOfSession(buff).toLocalTime().isBefore(session.getTime().toLocalTime()) &&
                        getEndTimeOfSession(session).toLocalTime().isBefore(sessionWithTheSameDate.getTime().toLocalTime())) {
                    return true;
                }
            }
            buff = sessionWithTheSameDate;
        }
        LOG.info("Checking fit in time has been finished");
        // если конец самого позднего существующего сеанса до начала нового
        // и если конец последний больше начала (22:00 - 00:00. при этом полночь раньше любого сеанса, но
        // это означает что больше сеансов на этот день добавить нельзя)
        // то можно добавить
        return getEndTimeOfSession(buff).toLocalTime().isAfter(buff.getTime().toLocalTime())
                && getEndTimeOfSession(buff).toLocalTime().isBefore(session.getTime().toLocalTime());
    }

    private boolean isInThePast(Session session) {
        return session.getFullDate().before(Timestamp.valueOf(LocalDateTime.now()));
    }

    private boolean isOutOfTime(Session session) {
        return !(LocalTime.of(9, 0).isBefore(session.getTime().toLocalTime())
                && LocalTime.of(22, 0).isAfter(session.getTime().toLocalTime()));
    }

    @Override
    public void deleteSession(Session session) throws ServiceException, TransactionException {
        LOG.info("Deleting the session starts -> " + session);
        try (Transaction transaction = Transaction.createTransaction()) {
            sessionDao = factory.getSessionDao(transaction.getConnection());

            sessionDao.deleteById(session.getId());
            LOG.info("Deleting has been finished");
        } catch (DAOException e) {
            LOG.error("Can`t delete session");
            throw new ServiceException(e.getMessage(), e);
        }
    }

    // идет проверка на совместимость по времени, как и в методе добавления
    @Override
    public void updateSession(Session session) throws ServiceException, TransactionException {
        LOG.info("Updating the session with id " + session.getId() + " starts -> " + session);
        try (Transaction transaction = Transaction.createTransaction()) {

            if (isInThePast(session)) {
                LOG.warn("Session date is in the past -> " + session.getDate());
                throw new ServiceException("session date is in the past");
            }
            if (isOutOfTime(session)) {
                LOG.warn("Session outside the cinema working time -> " + session.getDate());
                throw new ServiceException("session outside the cinema working time");
            }
            LOG.info("Getting old dates");
            Set<Session> oldDates = getSessionsByDate(session.getDate());
            LOG.info("Got dates -> " + oldDates);
            oldDates.stream().filter(s -> session.getId() == s.getId()).findFirst().ifPresent(oldDates::remove);
            if (!isFittedInTime(session, oldDates)) {
                LOG.warn("Another session is in progress during this one -> " + session);
                throw new ServiceException("Another session is in progress during this one");
            }
            sessionDao = factory.getSessionDao(transaction.getConnection());
            sessionDao.update(session);
            LOG.info("Updating session has been finished");
        } catch (DAOException e) {
            LOG.error("Can`t update session " + e);
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
