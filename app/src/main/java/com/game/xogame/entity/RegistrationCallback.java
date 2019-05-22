package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationCallback {
    @SerializedName("newUser")
    @Expose
    private String newUser;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("userid")
    @Expose
    private String userid;

    public String getNewUser() {
        return newUser;
    }

    public String getId() {
        return token;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getUserid() {
        return userid;
    }
}
