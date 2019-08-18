package com.game.xogame.presenters;

import android.content.ContentValues;
import android.util.Log;
import android.view.View;

import com.game.xogame.models.CreateGameModel;
import com.game.xogame.models.GamesModel;
import com.game.xogame.views.create.CreateGameActivity;
import com.game.xogame.views.create.CreatedFeedsActivity;
import com.game.xogame.views.game.FragmentFeeds;

public class CreatedFeedsPresenter {
    private final CreateGameModel model;
    private CreatedFeedsActivity view;

    public CreatedFeedsPresenter(CreateGameModel model) {
        this.model = model;
    }

    public void attachView(CreatedFeedsActivity mainActivity) {
        view = mainActivity;
    }



    public void showFeeds(String id){
        model.getFeeds(id,new GamesModel.GetFeedsCallback() {
            @Override
            public void onGet(String status, String error) {
                view.load.setVisibility(View.GONE);
                if(status.equals("success")) {
                    Log.i("LOG_scroll", "success");
                    view.setList(model.feedList);
                }else{
                    Log.i("LOG_scroll", "error");
                    view.setError(error);
                }

            }
        });

    }

}
