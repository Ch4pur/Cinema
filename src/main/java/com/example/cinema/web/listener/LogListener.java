package com.example.cinema.web.listener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class LogListener implements ServletContextListener {
    private static final Logger LOG = Logger.getLogger(LogListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Starting configure context");
        try {

            String homeDir= sce.getServletContext().getRealPath("/");
            File propertiesFile=new File(homeDir,"WEB-INF/log4j.properties");
            PropertyConfigurator.configure(propertiesFile.toString());
            LOG.info("Log4j has been initialized");
        } catch (Exception e) {
            LOG.info("Log4j configure isn`t finished");
        }
        LOG.info("Log4j configure is finished");
    }
}
