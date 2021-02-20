package com.example.cinema.db.service;

import com.example.cinema.db.exception.TransactionException;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Transaction implements Closeable {

    private static final Logger LOG = Logger.getLogger(Transaction.class);

    private static final BasicDataSource connectionPool;
    private Connection connection;

    private Transaction() {}

    static {
        LOG.info("Getting bundle");
        ResourceBundle dbConfig = ResourceBundle.getBundle("config/dataBaseConfig");

        LOG.info("Getting bundle info");
        String username = dbConfig.getString("connection.user");
        String dataBase = dbConfig.getString("connection.db");
        String password = dbConfig.getString("connection.password");
        String timezone = dbConfig.getString("connection.timezone");
        String url = dbConfig.getString("connection.url") + dataBase +
                    "?user="            + username +
                    "&password="        + password +
                    "&serverTimezone="  + timezone;

        int maxIdle = Integer.parseInt(dbConfig.getString("connectionPool.maxIdle"));
        int minIdle = Integer.parseInt(dbConfig.getString("connectionPool.minIdle"));
        int maxActive = Integer.parseInt(dbConfig.getString("connectionPool.maxActive"));
        int maxOpenPStatements = Integer.parseInt(dbConfig.getString("connectionPool.maxOpenPreparedStatements"));

        LOG.info("Initialize connection pool");
        connectionPool = new BasicDataSource();
        connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver");
        connectionPool.setUsername(username);
        connectionPool.setPassword(password);
        connectionPool.setUrl(url);
        connectionPool.setMinIdle(minIdle);
        connectionPool.setMaxIdle(maxIdle);
        connectionPool.setMaxActive(maxActive);
        connectionPool.setMaxOpenPreparedStatements(maxOpenPStatements);
        LOG.info("Initializing of pool has been finished");
    }

    public static Transaction createTransaction() throws TransactionException {
        LOG.info("Creating transaction object");
        Transaction res = new Transaction();
        try {
            LOG.info("Setting connection to transaction");
            res.connection = connectionPool.getConnection();
            LOG.info("Set connection -> " + res.connection);
        } catch (SQLException throwables) {
            LOG.error("Can`t create transaction ", throwables);
            throw new TransactionException(throwables.getMessage(),throwables);
        }
        LOG.info("Creating transaction has been finished");
        return res;
    }

    public void startTransaction() throws TransactionException {
        try {
            LOG.info("Disable autocommit");
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            LOG.error("Can`t disable autocommit " + throwables);
            throw new TransactionException(throwables.getMessage(),throwables);
        }
    }

    public void endTransaction() throws TransactionException {
        try {
            LOG.info("Enable autocommit");
            connection.setAutoCommit(true);
        } catch (SQLException throwables) {
            LOG.error("Can`t enable autocommit ", throwables);
            throw new TransactionException(throwables.getMessage(),throwables);
        }
    }

    public void commit() throws TransactionException {
        try {
            LOG.info("Commit transaction");
            connection.commit();
        } catch (SQLException throwables) {
            LOG.error("Can`t commit " + throwables);
            throw new TransactionException(throwables.getMessage(),throwables);
        }
    }

    public void rollback() throws TransactionException {
        try {
            LOG.info("Rollback transaction");
            connection.rollback();
        } catch (SQLException throwables) {
            LOG.error("Can`t rollback " + throwables);
            throw new TransactionException(throwables.getMessage(),throwables);
        }
    }

    public Connection getConnection() {
        LOG.info("Getting connection -> " + connection);
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                LOG.info("Close connection");
                connection.close();
            } catch (SQLException throwables) {
                LOG.error("Can`t close connection");
            }
        }
    }
}
