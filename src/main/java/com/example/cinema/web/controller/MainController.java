package com.example.cinema.web.controller;

import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.web.comand.Command;
import com.example.cinema.web.comand.CommandContainer;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;

@WebServlet(name = "MainController", urlPatterns = "/controller")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
public class MainController extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(MainController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("Controller starts");
        try {
            Command command = CommandContainer.getCommand(request.getParameter("command"));
            LOG.info("Executing command -> " + command.getClass().getName());
            String path = command.execute(request, response);
            LOG.info("Command executed");
            LOG.info("Forward path -> " + path);
            request.getRequestDispatcher(path).forward(request, response);
        } catch (TransactionException e) {
            LOG.error("Can`t execute command " + e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (CommandException e) {
            LOG.error("Can`t execute command " + e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("Controller starts");

        try {
            Command command = CommandContainer.getCommand(request.getParameter("command"));
            LOG.info("Executing command -> " + command.getClass().getName());
            String path = command.execute(request, response);
            LOG.info("Command executed");
            LOG.info("Redirect path -> " + path);
            response.sendRedirect(path);
        } catch (TransactionException e) {
            LOG.error("Can`t execute command " + e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (CommandException e) {
            e.printStackTrace();
            LOG.error("Can`t execute command " + e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
