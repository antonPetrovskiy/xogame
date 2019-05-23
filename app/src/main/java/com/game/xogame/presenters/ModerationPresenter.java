package com.game.xogame.presenters;

import com.game.xogame.models.GamesModel;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.views.game.ModerationActivity;
import com.game.xogame.views.profile.MoneyActivity;

public class ModerationPresenter {
    private final GamesModel model;
    private ModerationActivity view;

    public ModerationPresenter(GamesModel model) {
        this.model = model;
    }

    public void attachView(ModerationActivity mainActivity) {
        view = mainActivity;
    }


    public void getModeration(String gameid) {
        model.getModeration(gameid, new GamesModel.GetModerationCallback() {
            @Override
            public void onGet(String s, String s1) {
                view.setList(model.moderateList);
            }
        });
    }
}
