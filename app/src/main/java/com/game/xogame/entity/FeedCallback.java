package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FeedCallback {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("games")
    @Expose
    private ArrayList<Feed> feeds = new ArrayList<>();


    public String getStatus(){return status;}

    public ArrayList<Feed> getFeeds() {
        return feeds;
    }

    public String getError() {
        return error;
    }
}
