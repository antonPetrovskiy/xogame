package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GamesNewCallback {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("games")
    @Expose
    private ArrayList<GameNew> games = new ArrayList<>();


    public String getStatus(){return status;}

    public ArrayList<GameNew> getGames() {
        return games;
    }

    public String getError() {
        return error;
    }
}
