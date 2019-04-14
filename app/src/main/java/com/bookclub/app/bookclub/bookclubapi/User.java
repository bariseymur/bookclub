/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bookclub.app.bookclub.bookclubapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import bookclubapi.*;

/**
 *
 * @author mosma
 */
public class User {

    private int id;
    private String username,
            password,
            mail,
            name,
            surname,
            country,
            phoneNumber,
            profilePicture;

    private boolean onlineState;
    private Date dateOfBirth;
    private double lon, lat;

    public User(
            int id,
            String username,
            String password,
            String mail,
            String name,
            String surname,
            String country,
            String phoneNumber,
            String profilePicture,
            boolean onlineState,
            String dateOfBirth,
            double lon,
            double lat
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.onlineState = onlineState;
        this.setDateOfBirth(dateOfBirth);
        this.lon = lon;
        this.lat = lat;
    }
    
    public User() {
        
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getOnlineState() {
        return this.onlineState;
    }

    public void setOnlineState(boolean onlineState) {
        this.onlineState = onlineState;
    }

    public String getProfilePictureUrl() {
        return this.profilePicture;
    }

    public void setProfilePictureUrl(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Double getLon() {
        return this.lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return this.lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        // parameter should be in "yyyy-MM-dd" format

        try {
            this.dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth);
        } catch (ParseException ex) {
            Logger.getLogger(bookclubapi.Book.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
