package com.example.cinema.web.listener;

import com.example.cinema.db.entity.Session;
import com.example.cinema.db.service.SessionServiceImpl;
import com.example.cinema.db.service.iface.SessionService;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

public class MyContextListener implements ServletContextListener {
    private Thread dateChecker;
    private final long SLEEP_TIME = 600;
    private static final Logger LOG = Logger.getLogger(MyContextListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Creating date thread");
        dateChecker = new Thread(() -> {
            while (true) {
                SessionService sessionService = new SessionServiceImpl();
                try {

                    Set<Session> sessions = sessionService.getAll();
                    Timestamp currentDateTime = Timestamp.valueOf(LocalDateTime.now());
                    LOG.info("Starting check sessions at -> " + Timestamp.valueOf(LocalDateTime.now()));
                    for (Session session : sessions) {
                        if (session.getFullDate().before(currentDateTime)) {
                            LOG.info("Deleting old session -> " + session);
                            sessionService.deleteSession(session);
                        }
                    }
                    LOG.info("Updating sessions at " + currentDateTime + " is finished");
                    Thread.sleep(500 * SLEEP_TIME);
                } catch (Exception e) {
                    dateChecker.interrupt();
                    LOG.error("Error with updating sessions");
                }
            }
        });
        dateChecker.setDaemon(true);
        dateChecker.start();
    }
}
