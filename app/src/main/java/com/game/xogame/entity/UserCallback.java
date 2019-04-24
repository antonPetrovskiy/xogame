package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserCallback {
    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("status")
    @Expose
    private String status;

    public User getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }
}
