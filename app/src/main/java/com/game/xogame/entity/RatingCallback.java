package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RatingCallback {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("games")
    @Expose
    private ArrayList<Rating> rating = new ArrayList<>();


    public String getStatus(){return status;}

    public ArrayList<Rating> getRating() {
        return rating;
    }
}
