package com.game.xogame.presenters;

import android.content.ContentValues;

import com.game.xogame.models.CreateGameModel;
import com.game.xogame.views.create.CreateGameActivity;

public class CreateGamePresenter {
    private final CreateGameModel model;
    private CreateGameActivity view;

    public CreateGamePresenter(CreateGameModel model) {
        this.model = model;
    }

    public void attachView(CreateGameActivity mainActivity) {
        view = mainActivity;
    }


    public void deleteGame(String gameid) {
        ContentValues cv = new ContentValues(1);
        cv.put("GAMEID", gameid);
        model.deleteGame(cv, new CreateGameModel.DeleteGameCallback() {
            @Override
            public void onDelete(String status, String error) {
                if(status.equals("success")){
                    view.end();
                }else{
                    view.showToast(error+"");
                }


            }
        });
    }

    public void createGame(String title, String description, String background, String[] name, String lat, String lon, String address, String flevel, String category, String gameid, final boolean pay) {
        ContentValues cv = new ContentValues(1);
        cv.put("TITLE", title);
        cv.put("DESCRIPTION", description);
        cv.put("BACKGROUND", background);
        cv.put("LAT", lat);
        cv.put("LON", lon);
        cv.put("ADDRESS", address);
        cv.put("FLEVEL", flevel);
        cv.put("CATEGORY", category);
        cv.put("GAMEID", gameid);
        model.createGame(cv, name, new CreateGameModel.CreateGameCallback() {
            @Override
            public void onCreate(String status, String error, String url) {
                if(status.equals("success")){
                    if(pay){
                        view.toPay(url);
                    }else{
                        view.end();
                    }

                }else{
                    view.showToast(error+"");
                }


            }
        });
    }
}
