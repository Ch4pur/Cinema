package com.example.cinema.db.entity;

import java.sql.Date;
import java.sql.Time;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Film extends Entity {
    private String title;
    private String producersName;
    private AgeRating ageRating;
    private Set<Genre> genres;
    private Time duration;
    private Date releaseDate;
    private String description;

    public Film() {
        genres = new LinkedHashSet<>();
    }
    public Film setDescription(String description) {
        this.description = description;

        return this;
    }

    public String getDescription() {
        return description;
    }

    public Film setImagePath(String imagePath) {
        this.imagePath = imagePath;

        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    private String imagePath;

    public String getTitle() {
        return title;
    }

    public String getProducersName() {
        return producersName;
    }

    public AgeRating getAgeRating() {
        return ageRating;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public Time getDuration() {
        return duration;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Film setTitle(String title) {
        this.title = title;

        return this;
    }

    public Film setProducersName(String producersName) {
        this.producersName = producersName;

        return this;
    }

    public Film setAgeRating(AgeRating ageRating) {
        this.ageRating = ageRating;

        return this;
    }

    public Film setGenre(Set<Genre> genres) {
        this.genres = genres;

        return this;
    }

    public Film setDuration(Time duration) {
        this.duration = duration;

        return this;
    }

    public Film setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;

        return this;
    }

    @Override
    public Film setId(int id) {
        return (Film) super.setId(id);
    }

    @Override
    public int hashCode() {
        int res = 7;
        res = 97 * res + title.hashCode();
        res = 97 * res + producersName.hashCode();
        res = 97 * res + (ageRating == null? 0: ageRating.hashCode());
        res = 97 * res + releaseDate.hashCode();
        res = 97 * res + description.hashCode();
        res = 97 * res + duration.hashCode();
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Film)) {
            return false;
        }
        Film other = (Film) obj;
        return (getId() != -1 && other.getId() != -1)?
                getId() == other.getId() :
                title.equals(other.title)
                && producersName.equals(other.producersName)
                && ageRating.equals(other.ageRating)
                && genres.equals(other.genres)
                && duration.equals(other.duration)
                && releaseDate.equals(other.releaseDate)
                && description.equals(other.description);
    }

    @Override
    public String toString() {
        return "Film: " + title + System.lineSeparator() +
                "Producer: " + producersName + System.lineSeparator() +
                ageRating +
                "Genres: " + genres.stream().map(Genre::toString)
                        .collect(Collectors.joining(", ")) +
                "Duration: " + duration + System.lineSeparator() +
                "Release date: " + releaseDate + System.lineSeparator();
    }
}
