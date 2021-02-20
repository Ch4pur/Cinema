package com.example.cinema.db.entity;


import java.sql.Date;


public class Comment extends Entity {
    private Film film;
    private User author;
    private String content;
    private Date dateOfWriting;

    public Comment setFilm(Film film) {
        this.film = film;
        return this;
    }

    public Comment setAuthor(User author) {
        this.author = author;
        return this;
    }

    public Comment setContent(String content) {
        this.content = content;
        return this;
    }

    public Comment setDateOfWriting(Date dateOfWriting) {
        this.dateOfWriting = dateOfWriting;
        return this;
    }

    public Film getFilm() {
        return film;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Date getDateOfWriting() {
        return dateOfWriting;
    }

    @Override
    public Comment setId(int id) {
        return (Comment) super.setId(id);
    }

    @Override
    public int hashCode() {
        int res = 7;
        res = 37 * res + ((film == null) ? 0:film.hashCode());
        res = 37 * res + ((author == null) ? 0 : author.hashCode());
        res = 37 * res + content.hashCode();
        res = 37 * res + dateOfWriting.hashCode();
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) obj;
        return  (getId()!=-1 && other.getId() != -1)?
                getId() == other.getId() :
                author.equals(other.author)
                && film.equals(other.film)
                && content.equals(other.content)
                && dateOfWriting.equals(other.dateOfWriting);
    }

    @Override
    public String toString() {
        return author.getMail() + " " + dateOfWriting + System.lineSeparator() +
                content + System.lineSeparator();
    }
}
