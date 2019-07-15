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


    public void createGame(String title, String description, String background, String[] name, String limpeople, String city, String address, String flevel) {
        ContentValues cv = new ContentValues(1);
        cv.put("TITLE", title);
        cv.put("DESCRIPTION", description);
        cv.put("BACKGROUND", background);
        cv.put("LIMPEOPLE", limpeople);
        cv.put("CITY", city);
        cv.put("ADDRESS", address);
        cv.put("FLEVEL", flevel);
        model.createGame(cv, name, new CreateGameModel.CreateGameCallback() {
            @Override
            public void onCreate() {
                //view.success();
            }
        });
    }
}
