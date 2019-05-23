package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Moderation {

    @SerializedName("tasknum")
    @Expose
    private String tasknum;
    @SerializedName("tasktext")
    @Expose
    private String tasktext;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("taskphoto")
    @Expose
    private String taskphoto;

    public String getTasknum() {
        return tasknum;
    }

    public String getTasktext() {
        return tasktext;
    }

    public String getReason() {
        return reason;
    }

    public String getTaskphoto() {
        return taskphoto;
    }
}
