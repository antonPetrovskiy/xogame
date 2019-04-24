package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProfileGamesCallback {
    @SerializedName("status")
    @Expose
    private String status;
    public String getStatus(){return status;}



    @SerializedName("games")
    @Expose
    private ProfileGames games;
    public ProfileGames getGames() {
        return games;
    }

    public class ProfileGames {
        @SerializedName("now")
        @Expose
        public ArrayList<Game> nowGames;

        public ArrayList<Game> getNowGames(){
            return nowGames;
        }

        @SerializedName("future")
        @Expose
        public ArrayList<Game> futureGames;

        public ArrayList<Game> getFutureGames(){
            return futureGames;
        }
    }
}
