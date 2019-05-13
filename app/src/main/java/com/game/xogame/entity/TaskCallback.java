package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TaskCallback {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("error")
    @Expose
    private String error;


    public String getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }

    public String getPosition() {
        return position;
    }

}
