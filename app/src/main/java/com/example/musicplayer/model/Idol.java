package com.example.musicplayer.model;

import java.io.Serializable;

public class Idol implements Serializable {

    private String idolId, country, dob, favoriteReason, idolImg, name, publisher;

    public Idol() {
    }

    public Idol(String idolId, String country, String dob, String favoriteReason, String idolImg, String name, String publisher) {
        this.idolId = idolId;
        this.country = country;
        this.dob = dob;
        this.favoriteReason = favoriteReason;
        this.idolImg = idolImg;
        this.name = name;
        this.publisher = publisher;
    }

    public String getIdolId() {
        return idolId;
    }

    public void setIdolId(String idolId) {
        this.idolId = idolId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFavoriteReason() {
        return favoriteReason;
    }

    public void setFavoriteReason(String favoriteReason) {
        this.favoriteReason = favoriteReason;
    }

    public String getIdolImg() {
        return idolImg;
    }

    public void setIdolImg(String idolImg) {
        this.idolImg = idolImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}