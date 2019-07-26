package com.game.xogame.presenters;

import android.content.ContentValues;

import com.game.xogame.models.CreateGameModel;
import com.game.xogame.views.create.DateActivity;

public class DatePresenter {
    private final CreateGameModel model;
    private DateActivity view;

    public DatePresenter(CreateGameModel model) {
        this.model = model;
    }

    public void attachView(DateActivity mainActivity) {
        view = mainActivity;
    }

    public void setDateGame(String gameid, String startdate, String enddate, String starttime, String endtime) {
        ContentValues cv = new ContentValues(1);
        cv.put("STARTDATE", startdate);
        cv.put("ENDDATE", enddate);
        cv.put("STARTTIME", starttime);
        cv.put("ENDTIME", endtime);
        cv.put("GAMEID", gameid);
        model.setDateGame(cv, new CreateGameModel.SetDateGameCallback() {
            @Override
            public void onSet(String status, String error) {
                if(status.equals("success")){
                    view.finish();
                }else{
                    view.showToast(error+"");
                }


            }
        });
    }
}
