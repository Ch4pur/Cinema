package com.example.cinema.db.entity;


import java.io.Serializable;

public abstract class Entity implements Serializable {
    private int id;

    protected Entity() {
        id = -1;
    }

    protected Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Entity setId(int id) {
        if (this.id == -1) this.id = id;
        return this;
    }
}
