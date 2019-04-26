package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feed {

    @SerializedName("postId")
    @Expose
    private String feedId;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sponsorLogo")
    @Expose
    private String logoSponsorUrl;
    @SerializedName("userPhoto")
    @Expose
    private String userPhotoUrl;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userNickname")
    @Expose
    private String userNickname;
    @SerializedName("userLike")
    @Expose
    private String userLike;
    @SerializedName("taskTime")
    @Expose
    private String taskTime;
    @SerializedName("taskPhoto")
    @Expose
    private String taskPhotoUrl;
    @SerializedName("taskText")
    @Expose
    private String taskDescription;
    @SerializedName("taskNumber")
    @Expose
    private String taskNumber;
    @SerializedName("tasks")
    @Expose
    private String tasks;
    @SerializedName("taskComment")
    @Expose
    private String taskComment;
    @SerializedName("taskLikes")
    @Expose
    private String feedLikes;

    public String getFeedId() {
        return feedId;
    }

    public String getTitle() {
        return title;
    }

    public String getLogoSponsorUrl() {
        return logoSponsorUrl;
    }

    public String getCompany() {
        return company;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public String getUserLike() {
        return userLike;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public String getTaskPhotoUrl() {
        return taskPhotoUrl;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public String getTaskComment() {
        return taskComment;
    }

    public String getFeedLikes() {
        return feedLikes;
    }

    public String getTasks() {
        return tasks;
    }
}
