package com.example.cinema.web.comand;


import com.example.cinema.db.exception.CommandException;
import com.example.cinema.db.exception.TransactionException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
    String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException, TransactionException;
}
