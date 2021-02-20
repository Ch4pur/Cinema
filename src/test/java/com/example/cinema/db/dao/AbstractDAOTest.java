package com.example.cinema.db.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractDAOTest {
    private static final String TEST_DB_URL = "jdbc:mysql://localhost:3306/testdb?user=root&password=123456&serverTimezone=Europe/Kiev";
    protected Connection connection;

    protected static final DAOFactory factory = MyDAOFactory.getInstance();
    protected void createTables(String... tables) throws SQLException {
        connection = DriverManager.getConnection(TEST_DB_URL);

        try (Statement statement = connection.createStatement();) {
            for (String table : tables) {
                statement.executeUpdate(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void clearTables(String... sqlCommands) {
        try (Statement statement = connection.createStatement();) {
            for (String sql : sqlCommands) {
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
