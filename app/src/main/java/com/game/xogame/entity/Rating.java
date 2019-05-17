package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Rating {
    @SerializedName("logo")
    @Expose
    private String logo;
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
    @SerializedName("reward")
    @Expose
    private String reward;
    @SerializedName("followers")
    @Expose
    private String followers;
    @SerializedName("tasks")
    @Expose
    private String tasks;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timeleft")
    @Expose
    private String timeleft;
        @SerializedName("top")
        @Expose
        private ArrayList<Top> top;
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
                if(photo == null || photo.equals("")){
                    return "null";
                }else{
                    return photo;
                }

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

    public ArrayList<Top> getTop() {
        return top;
    }
}
