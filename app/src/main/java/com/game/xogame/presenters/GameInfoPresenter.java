package com.game.xogame.presenters;

import android.content.ContentValues;
import android.util.Log;

import com.game.xogame.models.GamesModel;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.views.game.FragmentGames;
import com.game.xogame.views.game.GameInfoActivity;
import com.game.xogame.views.main.MainActivity;
import com.game.xogame.views.profile.FragmentProfile;

public class GameInfoPresenter {
    private GameInfoActivity viewMain;
    private final GamesModel model;

    public GameInfoPresenter(GamesModel model) {
        this.model = model;
    }

    public void attachMainView(GameInfoActivity mainActivity) {
        viewMain = mainActivity;
    }

    public void detachView() {
        viewMain = null;
    }



    public void subscribeGame() {
        ContentValues cv = new ContentValues(1);
        cv.put("GAMEID", viewMain.getGameid());
        model.subscribeGame(cv,new GamesModel.SubscribeGameCallback() {
            @Override
            public void onSubscribe() {
                viewMain.showToast("Вы подписались на игру");
                viewMain.setButtonName("Отказатся");
            }
        });
    }

    public void unsubscribeGame(){
        ContentValues cv = new ContentValues(1);
        cv.put("GAMEID", viewMain.getGameid());
        model.unsubscribeGame(cv,new GamesModel.UnsubscribeGameCallback() {
            @Override
            public void onUnsubscribe() {
                viewMain.showToast("Вы отписались от игры");
                viewMain.setButtonName("Участвовать");
            }
        });
    }

}
