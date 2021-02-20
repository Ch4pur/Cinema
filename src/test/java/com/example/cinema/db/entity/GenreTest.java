package com.example.cinema.db.entity;

import org.junit.Test;

public class GenreTest {

    @Test(expected = IllegalArgumentException.class)
    public void genre_setNullName() {
        Genre genre = new Genre().setName(null);
    }
}
