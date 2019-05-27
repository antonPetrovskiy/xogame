package com.game.xogame.presenters;

import android.content.ContentValues;
import android.util.Log;

import com.game.xogame.entity.Feed;
import com.game.xogame.models.GamesModel;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.views.game.FragmentFeeds;
import com.game.xogame.views.game.FragmentGames;
import com.game.xogame.views.profile.FragmentProfile;
import com.game.xogame.views.main.MainActivity;

import java.util.LinkedList;
import java.util.List;

public class MainPresenter {
    private MainActivity viewMain;
    private final UserInfoModel model;
    private final GamesModel modelGames;

    public MainPresenter(UserInfoModel model, GamesModel modelGames) {
        this.model = model;
        this.modelGames = modelGames;
    }

    public void attachMainView(MainActivity mainActivity) {
        viewMain = mainActivity;
    }

    public void detachView() {
        viewMain = null;
    }



    public void showUserInfo(final FragmentProfile fragment) {
        model.getInfo(new UserInfoModel.GetInfoCallback() {
            @Override
            public void onGet() {
                Log.i("LOG_log", model.user.getPhoto() + "");
                fragment.setName(model.user.getName() + "");
                fragment.setNickName(model.user.getNickname() + "");

                if (!model.user.getPhoto().equals("")) {
                    fragment.setPhoto(model.user.getPhoto());
                } else {
                    fragment.setPhoto("");
                }
            }
        });

    }

    public void editPhoto(final FragmentProfile fragment) {
        ContentValues cv = new ContentValues(1);
        cv.put("IMAGE", fragment.getPhoto());
        model.editPhoto(cv, new UserInfoModel.EditPhotoCallback() {
            @Override
            public void onEdit() {
                fragment.update();
            }
        });
    }

    public void showGames(final FragmentGames fragment){
        modelGames.getGames(new GamesModel.GetGamesCallback() {
            @Override
            public void onGet(String status, String error) {
                if(status.equals("success")) {
                    fragment.setList(modelGames.gameList);
                }else{
                    fragment.setError(error);
                }
            }
        });
    }

    public void showFeeds(final FragmentFeeds fragment, String flag, String limit){
    modelGames.getFeeds(flag,limit,new GamesModel.GetFeedsCallback() {
            @Override
            public void onGet(String status, String error) {
                if(status.equals("success")) {
                    fragment.setList(modelGames.feedList);
                }else{
                    fragment.setError(error);
                }

            }
        });

    }

    public void showMyGames(final FragmentProfile fragment){
        model.getProfileGames(new UserInfoModel.GetProfileGamesCallback() {
            @Override
            public void onGet() {
                fragment.setNowList(model.profileNowGameList);
                fragment.setFutureList(model.profileFutureGameList);
            }
        });
    }


}
