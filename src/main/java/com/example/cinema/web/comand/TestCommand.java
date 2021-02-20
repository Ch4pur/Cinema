package com.example.cinema.web.comand;

import com.example.cinema.db.entity.Session;
import com.example.cinema.db.entity.Ticket;
import com.example.cinema.db.entity.User;
import com.example.cinema.db.exception.ServiceException;
import com.example.cinema.db.exception.TransactionException;
import com.example.cinema.db.service.FilmServiceImpl;
import com.example.cinema.db.service.TicketServiceImpl;
import com.example.cinema.db.service.iface.FilmService;
import com.example.cinema.db.service.iface.SessionService;
import com.example.cinema.db.service.SessionServiceImpl;
import com.example.cinema.db.service.iface.TicketService;
import com.example.cinema.web.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class TestCommand implements Command{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");


        return request.getContextPath() + Pages.MAIN;
    }
}
