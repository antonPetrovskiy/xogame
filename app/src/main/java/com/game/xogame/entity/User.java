package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("ccard")
    @Expose
    private String card;
    @SerializedName("mail")
    @Expose
    private String mail;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("gameid")
    @Expose
    private String gameid;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("city")
    @Expose
    private String city;


    public String getNickname() {
        return nickname;
    }

    public String getNumber() {
        return number;
    }

    public String getCard() {
        return card;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
