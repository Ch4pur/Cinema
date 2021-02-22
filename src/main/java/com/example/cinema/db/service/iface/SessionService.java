package com.example.cinema.db.service.iface;

import com.example.cinema.db.entity.Film;
import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SessionService {

    Session getSessionById(int id) throws ServiceException, TransactionException;

    Set<Session> getAll() throws ServiceException, TransactionException;
    Set<Session> getSessionsByFilm(Film film) throws ServiceException, TransactionException;
    Set<Session> getSessionsByThreeDSupporting(boolean threeDSupporting) throws ServiceException, TransactionException;
    Set<Session> getSessionsByDate(Date date) throws ServiceException, TransactionException;


    Map<Integer,Integer> getNumberOfSessionsOfAllFilms() throws ServiceException, TransactionException;

    Map<String, List<Session>> getFilmSessionsGroupedByDate(Film film) throws ServiceException, TransactionException;

    List<List<Ticket>> getSeatsById(int id) throws ServiceException, TransactionException;

//    static Time getEndTimeOfSession(Session session) {
//        long startingOfFilm = session.getFullDate().getTime();
//        long filmDuration = session.getFilm().getDuration().getTime();
//        long addTime = Time.valueOf("00:20:00").getTime();
//        long sessionGap = Time.valueOf("00:20:00").getTime();
//        long timeZoneOffset = -(Time.valueOf("00:00:00").getTime());
//
//        return new Time(startingOfFilm + filmDuration + addTime + sessionGap + timeZoneOffset * 3);
//    }

    void addSession(Session session) throws ServiceException, TransactionException;
    void deleteSession(Session session) throws ServiceException, TransactionException;
    void updateSession(Session session) throws ServiceException, TransactionException;

}
