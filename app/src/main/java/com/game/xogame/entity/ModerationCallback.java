package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModerationCallback {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("tasks")
    @Expose
    private ArrayList<Moderation> tasks = new ArrayList<>();

    public String getStatus(){return status;}

    public ArrayList<Moderation> getTasks() {
        return tasks;
    }

    public String getError() {
        return error;
    }
}
