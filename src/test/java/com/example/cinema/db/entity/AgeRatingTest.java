package com.example.cinema.db.entity;

import org.junit.Test;

public class AgeRatingTest {
    @Test(expected = IllegalArgumentException.class)
    public void ageRating_setNegativeMinAge() {
        AgeRating ageRating = new AgeRating().setMinAge((short) -1);
    }

}
