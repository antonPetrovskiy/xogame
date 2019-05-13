package com.game.xogame.presenters;

import android.content.ContentValues;

import com.game.xogame.R;
import com.game.xogame.models.GamesModel;
import com.game.xogame.views.game.GameInfoActivity;

public class GameInfoPresenter {
    private final GamesModel model;
    private GameInfoActivity viewMain;

    public GameInfoPresenter(GamesModel model) {
        this.model = model;
    }

    public void attachMainView(GameInfoActivity mainActivity) {
        viewMain = mainActivity;
    }


    public void subscribeGame() {
        ContentValues cv = new ContentValues(1);
        cv.put("GAMEID", viewMain.getGameid());
        model.subscribeGame(cv, new GamesModel.SubscribeGameCallback() {
            @Override
            public void onSubscribe() {
                //viewMain.showToast("Вы подписались на игру");
                viewMain.setButtonName(viewMain.getString(R.string.btn_leave));
            }
        });
    }

    public void unsubscribeGame() {
        ContentValues cv = new ContentValues(1);
        cv.put("GAMEID", viewMain.getGameid());
        model.unsubscribeGame(cv, new GamesModel.UnsubscribeGameCallback() {
            @Override
            public void onUnsubscribe() {
                //viewMain.showToast("Вы отписались от игры");
                viewMain.setButtonName(viewMain.getString(R.string.btn_join));
                if (viewMain.isStatistic.equals("true"))
                    viewMain.onBackPressed();
            }
        });
    }

}
