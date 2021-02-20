package com.example.cinema.db.entity;

public class Genre extends Entity{
    private String name;

    public Genre setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        this.name = name;

        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public Genre setId(int id) {
        return (Genre) super.setId(id);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(! (obj instanceof Genre)) {
            return false;
        }

        return name.equals(((Genre) obj).name);
    }

    @Override
    public String toString() {
        return name;
    }
}
