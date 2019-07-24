package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCallback {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("gameid")
    @Expose
    private String gameid;
    @SerializedName("url")
    @Expose
    private String url;

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getGameid() {
        return gameid;
    }

    public String getUrl() {
        return url;
    }
}
