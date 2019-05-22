package com.game.xogame.presenters;

import com.game.xogame.models.UserInfoModel;
import com.game.xogame.views.profile.MoneyActivity;

public class MoneyPresenter {
    private final UserInfoModel model;
    private MoneyActivity view;

    public MoneyPresenter(UserInfoModel model) {
        this.model = model;
    }

    public void attachView(MoneyActivity mainActivity) {
        view = mainActivity;
    }


    public void sendMoney(String type, String gameid) {
        model.sendMoney(type,gameid,new UserInfoModel.SendMoneyCallback() {
            @Override
            public void onSend() {
                view.success();
            }
        });
    }
}
