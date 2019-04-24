package com.game.xogame.presenters;

import com.game.xogame.entity.Game;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.views.profile.MyWinsActivity;

import java.util.List;

public class MyWinsPresenter {
    private MyWinsActivity viewMyWins;
    private final UserInfoModel model;

    public MyWinsPresenter(UserInfoModel model) {
        this.model = model;
    }

    public void attachMyWinsView(MyWinsActivity mainActivity) {
        viewMyWins = mainActivity;
    }

    public void detachView() {
        viewMyWins = null;
    }

    public void getMyWins() {
        model.getMyWins(new UserInfoModel.GetMyWinsCallback() {
            @Override
            public void onGet() {
                viewMyWins.setList(model.winGameList);
            }
        });
    }

    public List<Game> getWinsList(){
        return model.winGameList;
    }

}
