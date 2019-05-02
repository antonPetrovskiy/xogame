package com.game.xogame.presenters;

import com.game.xogame.models.UserInfoModel;
import com.game.xogame.views.profile.UserProfileActivity;

public class UserProfilePresenter {
    private UserProfileActivity viewMain;
    private final UserInfoModel model;

    public UserProfilePresenter(UserInfoModel model) {
        this.model = model;
    }

    public void attachMainView(UserProfileActivity mainActivity) {
        viewMain = mainActivity;
    }

    public void detachView() {
        viewMain = null;
    }



//    public void showGames(){
//        model.getUserGames(new UserInfoModel.GetUserGamesCallback() {
//            @Override
//            public void onGet() {
//                viewMain.setList(modelGames.gameList);
//            }
//        });
//    }



    public void showMyGames(String userid){
        model.getUserProfileGames(userid, new UserInfoModel.GetUserProfileGamesCallback() {
            @Override
            public void onGet() {
                viewMain.setNowList(model.profileNowGameList);
                viewMain.setFutureList(model.profileFutureGameList);
            }
        });
    }
}
