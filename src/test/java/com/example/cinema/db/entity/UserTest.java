package com.example.cinema.db.entity;

import com.example.cinema.db.entity.User;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;

public class UserTest {
    @Test
    public void user_check_same_hashedPassword() {
        String password = "12345sd";

        String firstSalted = User.getSaltedHash(password);
        String secondSalted = User.getSaltedHash(password);

        Assert.assertNotEquals(firstSalted,secondSalted);
    }
    @Test
    public void user_getSaltedHash() {
        String expected = "12345sd";
        String actual = User.getSaltedHash(expected);

        Assert.assertTrue(User.check(expected,actual));
    }

    @Test
    public void user_setPhone() {
        String phone = "(800)-(553)_35 35";
        String expected = "8005533535";

        User user = new User().setPhoneNumber(phone);
        String actual = user.getPhoneNumber();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void user_mayWatch() {
        User user = new User().setBirthday(Date.valueOf("2000-01-01"));
        AgeRating ageRating = new AgeRating().setMinAge((short) 11);
        Film film = new Film().setAgeRating(ageRating);

        Assert.assertTrue(user.mayWatch(film));
    }

    @Test
    public void user_doesntMayWatch() {
        User user = new User().setBirthday(Date.valueOf("2010-01-01"));
        AgeRating ageRating = new AgeRating().setMinAge((short) 18);
        Film film = new Film().setAgeRating(ageRating);

        Assert.assertFalse(user.mayWatch(film));
    }
    @Test
    public void user_changeMail() {
        String expected = "Mail";
        User user = new User().setMail(expected);

        user.setMail("Mail2");

        Assert.assertEquals(expected,user.getMail());
    }
}
