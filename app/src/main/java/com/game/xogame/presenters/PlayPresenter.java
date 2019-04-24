package com.game.xogame.presenters;

import android.content.ContentValues;

import com.game.xogame.entity.Game;
import com.game.xogame.models.PlayModel;
import com.game.xogame.views.game.PlayActivity;


import java.util.List;

public class PlayPresenter {
    private PlayActivity view;
    private final PlayModel model;

    public PlayPresenter(PlayModel model) {
        this.model = model;
    }

    public void attachView(PlayActivity mainActivity) {
        view = mainActivity;
    }

    public void detachView() {
        view = null;
    }

    public void sendTask() {
        ContentValues cv = new ContentValues(1);
        cv.put("IMAGE", view.getImage());
        cv.put("TASKID", view.getTaskid());
        cv.put("COMMENT", view.getComment());
        cv.put("TASKTIME", view.getTasktime());
        model.doTask(cv,new PlayModel.DoTaskCallback() {
            @Override
            public void onDo() {
                view.toMainActivityWin();
            }
        });
    }

}
