package com.game.xogame.presenters;

import com.game.xogame.models.UserInfoModel;
import com.game.xogame.views.profile.MyGamesActivity;

public class MyGamesPresenter {
    private MyGamesActivity viewMyGames;
    private final UserInfoModel model;

    public MyGamesPresenter(UserInfoModel model) {
        this.model = model;
    }

    public void attachMyGamesView(MyGamesActivity mainActivity) {
        viewMyGames = mainActivity;
    }

    public void detachView() {
        viewMyGames = null;
    }

    public void getMyGames() {
        model.getMyGames(new UserInfoModel.GetMyGamesCallback() {
            @Override
            public void onGet() {
                viewMyGames.setList(model.gameList);
            }
        });
    }

    public void getUserGames(String userid) {
        model.getUserGames(userid, new UserInfoModel.GetUserGamesCallback() {
            @Override
            public void onGet() {
                viewMyGames.setList(model.gameList);
            }
        });
    }

}
