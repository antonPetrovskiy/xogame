package com.game.xogame.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GameNew {
    @SerializedName("game_id")
    @Expose
    private String gameid;
    @SerializedName("game_status")
    @Expose
    private String status;
    @SerializedName("company_name")
    @Expose
    private String company;
    @SerializedName("name_game")
    @Expose
    private String name_game;
    @SerializedName("wrong_name_game")
    @Expose
    private boolean wrong_name_game ;
    @SerializedName("startdate")
    @Expose
    private String startdate;
    @SerializedName("start_task_time")
    @Expose
    private String start_task_time;
    @SerializedName("enddate")
    @Expose
    private String enddate;
    @SerializedName("end_task_time")
    @Expose
    private String end_task_time;
    @SerializedName("company_logo_url")
    @Expose
    private String logo;
    @SerializedName("background_game_url")
    @Expose
    private String background;
    @SerializedName("wrong_background")
    @Expose
    private boolean wrong_background;
    @SerializedName("followers")
    @Expose
    private int followers;
    @SerializedName("description_game")
    @Expose
    private String description;
    @SerializedName("wrong_description")
    @Expose
    private String wrong_description;
    @SerializedName("reward")
    @Expose
    private int reward;
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
    @SerializedName("wrong_tasks")
    @Expose
    private boolean wrong_tasks;
    @SerializedName("game_category")
    @Expose
    private String category;
    @SerializedName("address_game")
    @Expose
    private AddressGame addressGame;
    @SerializedName("tasks")
    @Expose
    private ArrayList<TasksGame> tasksGame = new ArrayList<>();

    public String getGameid() {
        return gameid;
    }

    public String getStatus() {
        return status;
    }

    public String getCompany() {
        return company;
    }

    public String getName_game() {
        return name_game;
    }

    public boolean isWrong_name_game() {
        return wrong_name_game;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getStart_task_time() {
        return start_task_time;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getEnd_task_time() {
        return end_task_time;
    }

    public String getLogo() {
        return logo;
    }

    public String getBackground() {
        return background;
    }

    public boolean isWrong_background() {
        return wrong_background;
    }

    public int getFollowers() {
        return followers;
    }

    public String getDescription() {
        return description;
    }

    public String isWrong_description() {
        return wrong_description;
    }

    public int getReward() {
        return reward;
    }

    public String getComplited() {
        return complited;
    }

    public String getSub() {
        return sub;
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

    public AddressGame getAddressGame() {
        return addressGame;
    }

    public boolean isWrong_tasks() {
        return wrong_tasks;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<TasksGame> getTasksGame() {
        return tasksGame;
    }

    public class AddressGame {
        @SerializedName("address_text")
        @Expose
        private String address_text;
        @SerializedName("radius")
        @Expose
        private int radius;
        @SerializedName("coordinate")
        @Expose
        private CoordinateGame coordinateGame;

        public String getAddress_text() {
            return address_text;
        }

        public int getRadius() {
            return radius;
        }

        public CoordinateGame getCoordinateGame() {
            return coordinateGame;
        }

        public class CoordinateGame {
            @SerializedName("lon")
            @Expose
            private float lon;
            @SerializedName("lat")
            @Expose
            private float lat;

            public float getLon() {
                return lon;
            }

            public float getLat() {
                return lat;
            }
        }
    }

    public class TasksGame {
        @SerializedName("task_number")
        @Expose
        private int task_number;
        @SerializedName("task_description")
        @Expose
        private String task_description;
        @SerializedName("wrong_task")
        @Expose
        private boolean wrong_task;

        public int getTask_number() {
            return task_number;
        }

        public String getTask_description() {
            return task_description;
        }

        public boolean isWrong_task() {
            return wrong_task;
        }
    }
}
