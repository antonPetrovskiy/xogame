package com.game.xogame.presenters;

import com.game.xogame.models.RatingModel;
import com.game.xogame.views.game.RatingActivity;
import com.game.xogame.views.game.RatingGameActivity;

public class RatingPresenter {
    private RatingActivity view;
    private RatingGameActivity viewGame;
    private final RatingModel model;

    public RatingPresenter(RatingModel model) {
        this.model = model;
    }

    public void attachView(RatingActivity mainActivity) {
        view = mainActivity;
    }

    public void attacRatingGamehView(RatingGameActivity gameActivity) {
        viewGame = gameActivity;
    }

    public void detachView() {
        view = null;
    }


    public void showGameRating(String gameid) {
        model.getGameRating("1", new RatingModel.GetGameRatingCallback() {
            @Override
            public void onGet() {
                viewGame.setGameViews(model.ratingGame);
            }
        });
    }

    public void showRating() {
        model.getRating(new RatingModel.GetRatingCallback() {
            @Override
            public void onGet() {
                view.setList(model.ratingList);
            }
        });
    }
}
