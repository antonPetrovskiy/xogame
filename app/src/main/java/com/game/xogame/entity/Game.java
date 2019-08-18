package com.game.xogame.entity;

import android.content.res.Resources;
import android.util.Log;

import com.game.xogame.R;
import com.game.xogame.views.main.MainActivity;
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
    private boolean sub;
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
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("owner")
    @Expose
    private boolean owner;


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

    public boolean getSubscribe() {
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

    public String getAddress() {
        return address;
    }

    public String getCategoryId() {
        return category;
    }

    public boolean isOwner() {
        return owner;
    }

    public String getCategory() {
        switch (category) {
            case "0":
                return MainActivity.mContext.getResources().getString(R.string.category_auto);
            case "1":
                return MainActivity.mContext.getResources().getString(R.string.category_sport);
            case "2":
                return MainActivity.mContext.getResources().getString(R.string.category_food);
            case "3":
                return MainActivity.mContext.getResources().getString(R.string.category_travel);
            case "4":
                return MainActivity.mContext.getResources().getString(R.string.category_fun);
            case "5":
                return MainActivity.mContext.getResources().getString(R.string.category_tv);
            case "6":
                return MainActivity.mContext.getResources().getString(R.string.category_beauty);
            case "7":
                return MainActivity.mContext.getResources().getString(R.string.category_fashion);
            case "8":
                return MainActivity.mContext.getResources().getString(R.string.category_decor);
            case "9":
                return MainActivity.mContext.getResources().getString(R.string.category_iscustvo);
            case "10":
                return MainActivity.mContext.getResources().getString(R.string.category_art);
            case "11":
                return MainActivity.mContext.getResources().getString(R.string.category_style);
            case "12":
                return MainActivity.mContext.getResources().getString (R.string.category_myday);
            case "13":
                return MainActivity.mContext.getResources().getString(R.string.category_other);
            default:
                return MainActivity.mContext.getResources().getString(R.string.category_other);
        }
    }
}
