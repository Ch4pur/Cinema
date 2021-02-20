package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.SessionServiceImpl;
import com.example.cinema.db.service.iface.SessionService;
import com.example.cinema.web.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

class CreateSessionsPageCommand implements Command {
    private static final Logger LOG = Logger.getLogger(CreateSessionsPageCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws TransactionException, CommandException {
        LOG.info("Start create receipt page");
        LOG.info("Current URI -> " + request.getRequestURI());

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        boolean sortByDate = request.getParameter("sortByDate") != null;
        boolean sortBySeats = request.getParameter("sortBySeats") != null;
        boolean onlyAvailable = request.getParameter("available") != null;
        LOG.info("Sort by date ->" + sortByDate + " seats -> " + sortBySeats + " available -> "+ onlyAvailable);
        SessionService sessionService = new SessionServiceImpl();

        request.setAttribute("sortByDate", request.getParameter("sortByDate"));
        request.setAttribute("sortBySeats", request.getParameter("sortBySeats"));
        request.setAttribute("available", request.getParameter("available"));

        try {
            Set<Session> sessions = sessionService.getAll();
            LOG.info("Get all sessions -> " + sessions);
            if (sortByDate && sortBySeats) {
                sessions = Sorter.sortSessionsByDateAndSeats(sessions);
            } else if (sortByDate) {
                sessions = Sorter.sortSessionsByDate(sessions);
            } else if (sortBySeats) {
                sessions = Sorter.sortSessionsBySeats(sessions);
            }

            if (onlyAvailable) {
                sessions = Sorter.filterByAvailableSessions(sessions, user);
            }
            LOG.info("Sort sessions -> " + sessions);
            request.setAttribute("sessions", sessions);

        } catch (ServiceException e) {
            LOG.error("Can`t create sessions page " + e);
            throw new CommandException(e.getMessage(),e);
        }
        return Pages.SESSIONS_JSP;
    }

    private static class Sorter {
        private static final Comparator<Session> SORT_BY_DATE = Comparator.comparing(Session::getFullDate);
        private static final Comparator<Session> SORT_BY_SEATS = Comparator.comparing(Session::getNumberOfFreeSeats, Comparator.reverseOrder());

        private static final Comparator<Session> SORT_BY_DATE_THEN_BY_SEATS = Comparator.comparing(Session::getFullDate)
                .thenComparing(Session::getNumberOfFreeSeats, Comparator.reverseOrder());


        private static Set<Session> sortSessionsBy(Set<Session> sessions, Comparator<Session> comparator) {
            return sessions.stream().sorted(comparator).collect(Collectors.toCollection(LinkedHashSet::new));
        }

        public static Set<Session> sortSessionsByDate(Set<Session> sessions) {
            return sortSessionsBy(sessions, SORT_BY_DATE);
        }

        public static Set<Session> sortSessionsBySeats(Set<Session> sessions) {
            return sortSessionsBy(sessions, SORT_BY_SEATS);
        }

        public static Set<Session> sortSessionsByDateAndSeats(Set<Session> sessions) {
            return sortSessionsBy(sessions, SORT_BY_DATE_THEN_BY_SEATS);
        }

        public static Set<Session> filterByAvailableSessions(Set<Session> sessions, User user) {
            return sessions.stream().filter(session ->
                    (user == null || user.mayWatch(session.getFilm())) && session.getNumberOfFreeSeats() > 0)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

    }

}


