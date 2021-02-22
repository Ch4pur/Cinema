package com.example.cinema.db.exception;

public class TransactionException extends ApplicationException{

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(String message) {
        super(message);
    }
}
