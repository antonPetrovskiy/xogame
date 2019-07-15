package com.game.xogame.presenters;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.game.xogame.models.GamesModel;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.views.game.FragmentFeeds;
import com.game.xogame.views.game.FragmentGames;
import com.game.xogame.views.main.MainActivity;
import com.game.xogame.views.profile.FragmentProfile;

import static android.content.Context.MODE_PRIVATE;

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
                SharedPreferences sharedPref = fragment.getContext().getSharedPreferences("myPref", MODE_PRIVATE);
                sharedPref.edit().putString("ccard", model.user.getCard()).commit();
                Log.i("LOG_card", model.user.getCard() + "");
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
                fragment.load.setVisibility(View.GONE);
                if(status.equals("success")) {
                    Log.i("LOG_scroll", "success");
                    fragment.setList(modelGames.feedList);
                }else{
                    Log.i("LOG_scroll", "error");
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
