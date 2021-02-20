package com.example.cinema.db.exception;

public class CommandException extends ApplicationException {
    public CommandException() {
        super();
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(String message) {
        super(message);
    }
}
