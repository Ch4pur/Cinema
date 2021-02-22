package com.example.cinema.db.dao.implementation;

import com.example.cinema.db.dao.iface.DAOInterface;
import com.example.cinema.db.entity.Entity;
import com.example.cinema.db.exception.DAOException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

public abstract class AbstractDAO<T extends Entity> implements DAOInterface<T> {


    protected Connection connection;

    protected AbstractDAO(Connection connection) {
        this.connection = connection;
    }

    protected abstract T createEntity(ResultSet resultSet) throws SQLException;

    //метод для создания prepareStatement со вставкой аргументов в ?
    private PreparedStatement prepareStatement(String sql,String[] args) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int place = 1;
        for (String whereArg : args) {
            preparedStatement.setString(place++, whereArg);
        }
        return preparedStatement;
    }
    private PreparedStatement prepareStatement(String sql, String[] args,int startWith,int partSize) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int place = 1;
        for (String whereArg : args) {
            preparedStatement.setString(place++, whereArg);
        }
        preparedStatement.setInt(place++,partSize);
        preparedStatement.setInt(place,startWith);

        return preparedStatement;
    }
    //универсальный метод получения Сущности
    protected T getByStatement(String sql, String... args) throws DAOException {
        T entity = null;
        try (PreparedStatement preparedStatement = prepareStatement(sql, args)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                entity = createEntity(resultSet);
            }
        } catch (SQLException e) {
            //logging
            throw new DAOException(e.getMessage(), e);
        }

        return entity;
    }
    //универсальный метод получения коллекции
    protected Collection<T> getAllByStatement(String sql, String... args) throws DAOException {
        Collection<T> set = new LinkedList<>();
        try (PreparedStatement preparedStatement = prepareStatement(sql, args)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                set.add(createEntity(resultSet));
            }
        } catch (SQLException e) {
            //logging
            throw new DAOException(e.getMessage(),e);
        }
        return set;
    }
    //универсальный метод получения внешнего ключа
    protected int getForeignKeyId(String sql, int entityId) throws DAOException {
        int res = 0;
        try (PreparedStatement preparedStatement = prepareStatement(sql,new String[]{String.valueOf(entityId)})) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            //logging
            throw new DAOException(e.getMessage(), e);
        }
        return res;
    }
    //универсальный метод обновления сущностей
    protected void updateByStatement(String sql, String... args) throws DAOException {
        try (PreparedStatement preparedStatement = prepareStatement(sql,args)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            //logging
            throw new DAOException(e.getMessage(),e);
        }
    }
    //универсальный метод получения сущностей с пагинацией
    protected Collection<T> getPartByStatement(int startWith, int partSize, String sql, String... args) throws DAOException {
        Collection<T> set = new LinkedList<>();
        try (PreparedStatement preparedStatement = prepareStatement(sql, args,startWith,partSize)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                set.add(createEntity(resultSet));
            }
            return set;
        } catch (SQLException e) {
            //logging
            throw new DAOException(e.getMessage(),e);
        }
    }

    @Override
    public final int getCount() throws DAOException {
        return getAll().size();
    }

}
