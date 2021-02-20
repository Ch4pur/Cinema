package com.example.cinema.db.entity;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.time.LocalDate;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User extends Entity {

    private static final Logger LOG = Logger.getLogger(User.class);
    private String mail;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date birthday;
    private boolean isAdmin;
    private int coins;

    private static final int ITERATIONS = 20 * 1000;
    private static final int SALT_LENGTH = 10;
    private static final int DESIRED_KEY_LENGTH = 100;

    public int getCoins() {
        return coins;
    }

    public User setCoins(int coins) {
        if (coins < 0) {
            throw new IllegalArgumentException();
        }
        this.coins = coins;

        return this;
    }

    public boolean mayWatch(Film film) {
        int age = LocalDate.now().getYear() - birthday.toLocalDate().getYear();
        return film.getAgeRating().getMinAge() <= age;
    }

    public User setMail(String mail) {
        if (this.mail == null) {
            this.mail = mail;
        }
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }


    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;

        return this;
    }

    public User setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null) {
            phoneNumber = phoneNumber.replaceAll("[\\s()_-]", "");
        }
        this.phoneNumber = phoneNumber;
        return this;
    }

    public User setBirthday(Date birthday) {
        this.birthday = birthday;

        return this;
    }

    public User setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    @Override
    public User setId(int id) {
        return (User) super.setId(id);
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        res.append("User with e-mail: ").append(mail).append(System.lineSeparator());
        if (firstName != null && lastName != null) {
            res.append(lastName).append(' ').append(firstName).append(System.lineSeparator());
        }
        if (phoneNumber != null) {
            res.append("Phone number: ").append(phoneNumber).append(System.lineSeparator());
        }
        res.append("Role: ").append(isAdmin ? "Admin" : "User").append(System.lineSeparator());

        res.append("BirthDay: ").append(birthday).append(System.lineSeparator());
        res.append("Id: ").append(getId()).append(System.lineSeparator());
        res.append("Password: ").append(password).append(System.lineSeparator());
        return res.toString();
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 7 * res + mail.hashCode();
        res = 7 * res + password.hashCode();
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;

        return (getId() != -1 && other.getId() != -1)?
                getId() == other.getId() :
                this.mail.equals(other.mail) && this.password.equals(other.password);
    }

    private static String hash(String password, byte[] salt) {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = null;
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            LOG.error("No such algorithm -> " + e);
        }
        SecretKey key = null;
        try {
            key = f.generateSecret(new PBEKeySpec(
                    password.toCharArray(), salt, ITERATIONS, DESIRED_KEY_LENGTH)
            );
        } catch (InvalidKeySpecException e) {
            LOG.error("Invalid key Spec exception -> " + e);
        }
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static String getSaltedHash(String password) {
        byte[] salt = new byte[0];
        try {
            salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(SALT_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("No such algorithm -> " + e);
        }

        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
    }

    public static boolean check(String password, String stored) {
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2) {
            throw new IllegalStateException(
                    "The stored password have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);
    }
}
