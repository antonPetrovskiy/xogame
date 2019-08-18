package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userLike")
    @Expose
    private boolean userLike;
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
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("taskLikes")
    @Expose
    private String feedLikes;

    public String getFeedId() {
        return feedId;
    }

    public String getLogoSponsorUrl() {
        return logoSponsorUrl;
    }

    public String getUserPhotoUrl() {
        if(userPhotoUrl == null || userPhotoUrl.equals("")){
            return "null";
        }else{
            return userPhotoUrl;
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public String getUserId() {
        return userId;
    }

    public boolean getUserLike() {
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

    public String getType() {
        return type;
    }

    @SerializedName("logo")
    @Expose
    private String logo;
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
    @SerializedName("reward")
    @Expose
    private String reward;
    @SerializedName("followers")
    @Expose
    private String followers;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timeleft")
    @Expose
    private String timeleft;
    @SerializedName("top")
    @Expose
    private ArrayList<Rating.Top> top;
    public class Top{
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("nickname")
        @Expose
        private String nickname;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("position")
        @Expose
        private String position;
        @SerializedName("complited")
        @Expose
        private String complited;
        @SerializedName("tasktime")
        @Expose
        private String tasktime;

        public String getPhoto() {
            return photo;
        }

        public String getName() {
            return name;
        }

        public String getNickname() {
            return nickname;
        }

        public String getUserid() {
            return userid;
        }

        public String getPosition() {
            return position;
        }

        public String getComplited() {
            return complited;
        }

        public String getTasktime() {
            return tasktime;
        }
    }

    public String getLogo() {
        return logo;
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

    public String getStarttime() {
        return starttime;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getReward() {
        return reward;
    }

    public String getFollowers() {
        return followers;
    }

    public String getTasks() {
        return tasks;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeleft() {
        return timeleft;
    }

    public ArrayList<Rating.Top> getTop() {
        return top;
    }
}
