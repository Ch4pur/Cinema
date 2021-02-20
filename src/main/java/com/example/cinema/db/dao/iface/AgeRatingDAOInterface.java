package com.example.cinema.db.dao.iface;

import com.example.cinema.db.entity.AgeRating;
import com.example.cinema.db.exception.DAOException;

public interface AgeRatingDAOInterface<T extends AgeRating> extends DAOInterface<T> {
    AgeRating getByTitle(String title) throws DAOException;
}
