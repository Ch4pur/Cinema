package com.example.cinema.db.entity;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Session extends Entity {
    public static final int NUMBER_OF_COLUMNS = 8;
    public static final int NUMBER_OF_ROWS = 8;
    private Film film;
    private Timestamp fullDate;
    private boolean threeDSupporting;
    private int numberOfFreeSeats;

    public Session setNumberOfFreeSeats(int numberOfFreeSeats) {

        if (numberOfFreeSeats < 0 || numberOfFreeSeats > NUMBER_OF_ROWS * NUMBER_OF_COLUMNS) {
            throw new IllegalArgumentException();
        }
        this.numberOfFreeSeats = numberOfFreeSeats;
        return this;
    }

    public int getNumberOfFreeSeats() {
        return numberOfFreeSeats;
    }

    public Film getFilm() {
        return film;
    }

    public Timestamp getFullDate() {
        return fullDate;
    }
    public Time getTime() {
        return fullDate == null? null : new Time(fullDate.getTime());
    }
    public Date getDate() {
        return fullDate == null ? null : new Date(fullDate.getTime());
    }
    public boolean isThreeDSupporting() {
        return threeDSupporting;
    }

    @Override
    public Session setId(int id) {
        return (Session) super.setId(id);
    }

    public Session setFilm(Film film) {
        this.film = film;

        return this;
    }

    public Session setFullDate(Timestamp fullDate) {
        this.fullDate = fullDate;

        return this;
    }

    public Session setThreeDSupporting(boolean threeDSupporting) {
        this.threeDSupporting = threeDSupporting;

        return this;
    }

    @Override
    public int hashCode() {
        int res = 73;
        res = 19 * res + (film== null? 0 :film.hashCode());
        res = 19 * res + fullDate.hashCode();
        res = 19 * res + (threeDSupporting? 3 : 0);
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Session)) {
            return false;
        }
        Session other = (Session) obj;
        return (getId() != -1 && other.getId() != -1)?
                getId() == other.getId() :
                film.equals(other.film)
                && fullDate.equals(other.fullDate)
                && threeDSupporting == other.threeDSupporting;
    }

    @Override
    public String toString() {
        return  "Session:" + System.lineSeparator() +
                film +
                "Date: " + fullDate + System.lineSeparator() +
                "with 3D: " + (threeDSupporting? "+" : "-") + System.lineSeparator();
    }
}
