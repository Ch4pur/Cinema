package com.example.cinema.db.entity;



public class AgeRating extends Entity {
    private String title;
    private short minAge;
    private boolean withParents;

    public String getTitle() {
        return title;
    }

    public short getMinAge() {
        return minAge;
    }

    public boolean isWithParents() {
        return withParents;
    }

    @Override
    public AgeRating setId(int id) {
        return (AgeRating) super.setId(id);
    }

    public AgeRating setTitle(String title) {
        this.title = title;

        return this;
    }

    public AgeRating setMinAge(short minAge) {
        if (minAge <= 10 || minAge > 30) {
            throw new IllegalArgumentException();
        }
        this.minAge = minAge;
        return this;
    }

    public AgeRating setWithParents(boolean withParents) {
        this.withParents = withParents;

        return this;
    }


    @Override
    public int hashCode() {
        int res = 37;
        res = 3 * res + minAge;
        res = 3 * res + title.hashCode();
        res = 3 * res + (isWithParents()? 1 : 7);
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AgeRating)) {
            return false;
        }

        AgeRating other = (AgeRating) obj;
        return this.title.equals(other.title)
                && this.withParents == other.withParents
                && this.minAge == other.minAge;
    }

    @Override
    public String toString() {
        return  "Age rating: " + title + System.lineSeparator() +
                "Max allowable age: " + minAge + System.lineSeparator() +
                "possibility of going with parents" + (withParents ? "+" : "-") + System.lineSeparator();
    }
}
