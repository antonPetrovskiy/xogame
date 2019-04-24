package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GamesCallback {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("games")
    @Expose
    private ArrayList<Game> games = new ArrayList<>();


    public String getStatus(){return status;}

    public ArrayList<Game> getGames() {
        return games;
    }
}
