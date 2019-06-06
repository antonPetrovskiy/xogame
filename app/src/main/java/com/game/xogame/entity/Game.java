package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Game {
    @SerializedName("gameid")
    @Expose
    private String gameid;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("startdate")
    @Expose
    private String startdate;
    @SerializedName("starttime")
    @Expose
    private String starttime;
    @SerializedName("enddate")
    @Expose
    private String enddate;
    @SerializedName("endtime")
    @Expose
    private String endtime;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("tasks")
    @Expose
    private String tasks;
    @SerializedName("followers")
    @Expose
    private String followers;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("reward")
    @Expose
    private String reward;
    @SerializedName("complited")
    @Expose
    private String complited;
    @SerializedName("sub")
    @Expose
    private String sub;
    @SerializedName("siteurl")
    @Expose
    private String siteurl;
    @SerializedName("rewardstatus")
    @Expose
    private String rewardstatus;
    @SerializedName("gameAvaible")
    @Expose
    private String gameAvaible;
    @SerializedName("gameSub")
    @Expose
    private String gameSub;


    public String getGameid() {
        return gameid;
    }

    public String getCompany() {
        return company;
    }

    public String getTitle() {
        return title;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getLogo() {
        return logo;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getTasks() {
        return tasks;
    }

    public String getFollowers() {
        return followers;
    }

    public String getBackground() {
        return background;
    }

    public String getDescription() {
        return description;
    }

    public String getReward() {
        return reward;
    }

    public String getSubscribe() {
        return sub;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getComplited() {
        return complited;
    }

    public String getSiteurl() {
        return siteurl;
    }

    public String getRewardstatus() {
        return rewardstatus;
    }

    public String getGameAvaible() {
        return gameAvaible;
    }

    public String getGameSub() {
        return gameSub;
    }
}
