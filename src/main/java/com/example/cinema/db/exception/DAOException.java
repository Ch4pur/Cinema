package com.example.cinema.db.exception;

public class DAOException extends ApplicationException{

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(String message) {
        super(message);
    }
}
