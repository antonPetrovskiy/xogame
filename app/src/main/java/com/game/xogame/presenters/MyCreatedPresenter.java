package com.game.xogame.presenters;

import com.game.xogame.models.CreateGameModel;
import com.game.xogame.views.create.MyCreatedActivity;

public class MyCreatedPresenter {
    private final CreateGameModel model;
    private MyCreatedActivity view;

    public MyCreatedPresenter(CreateGameModel model) {
        this.model = model;
    }

    public void attachView(MyCreatedActivity mainActivity) {
        view = mainActivity;
    }


    public void getGames() {
        model.getGames(new CreateGameModel.GetGamesCallback() {
            @Override
            public void onGet(String status, String error) {
                if(status.equals("success")) {
                    view.setList(model.gameList);
                }else{
                    view.setError(error);
                }
            }
        });
    }
}
