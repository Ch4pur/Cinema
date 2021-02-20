package com.example.cinema.db.entity;

import java.sql.Date;

public class Ticket extends Entity {
    private User customer;
    private Session session;
    private int placeColumn;
    private int placeRow;
    private int price;
    private Date orderDate;

    public User getCustomer() {
        return customer;
    }

    public Session getSession() {
        return session;
    }

    public int getPlaceColumn() {
        return placeColumn;
    }

    public int getPlaceRow() {
        return placeRow;
    }

    public int getPrice() {
        return price;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void computePrice() {
        price = 50 + 5 * placeRow + 5 * session.getTime().toLocalTime().getHour() + (session.isThreeDSupporting() ? 20 : 0);
    }

    @Override
    public Ticket setId(int id) {
        return (Ticket) super.setId(id);
    }

    public Ticket setCustomer(User customer) {
        this.customer = customer;
        return this;
    }

    public Ticket setSession(Session session) {
        this.session = session;
        return this;
    }

    public Ticket setPlaceColumn(int placeColumn) {
        if (placeColumn < 1 || placeColumn > Session.NUMBER_OF_COLUMNS) {
            throw new IllegalArgumentException();
        }
        this.placeColumn = placeColumn;

        return this;
    }

    public Ticket setPlaceRow(int placeRow) {
        if (placeRow < 1 || placeRow > Session.NUMBER_OF_ROWS) {
            throw new IllegalArgumentException();
        }
        this.placeRow = placeRow;
        return this;
    }

    public Ticket setPrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException();
        }
        this.price = price;

        return this;
    }

    public Ticket setOrderDate(Date orderDate) {
        this.orderDate = orderDate;

        return this;
    }

    @Override
    public int hashCode() {
        int result = 113;
        result = 19 * result + (session == null ? 0 : session.hashCode());
        result = 19 * result + (customer == null ? 0 : customer.hashCode());
        result = 19 * result + orderDate.hashCode();
        result = 19 * result + placeRow;
        result = 19 * result + placeColumn;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Ticket)) {
            return false;
        }

        Ticket other = (Ticket) obj;
        return (getId() != -1 && other.getId() != -1) ?
                getId() == other.getId() :
                customer.equals(other.customer)
                        && session.equals(other.session)
                        && placeColumn == other.placeColumn
                        && placeRow == other.placeRow
                        && orderDate.equals(other.orderDate);
    }

    @Override
    public String toString() {
        return System.lineSeparator() +
                "------------------------------------" + System.lineSeparator() +
                "Ticket: " + System.lineSeparator() +
                "Customer: " + System.lineSeparator() +
                customer +
                session +
                "placeColumn = " + placeColumn +
                ", placeRow = " + placeRow + System.lineSeparator() +
                "Price: " + price + System.lineSeparator() +
                "OrderDate: " + orderDate + System.lineSeparator() +
                "------------------------------------" + System.lineSeparator();
    }
}
