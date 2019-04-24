package com.game.xogame.presenters;

import com.game.xogame.models.RatingModel;
import com.game.xogame.views.game.RatingActivity;

public class RatingPresenter {
    private RatingActivity view;
    private final RatingModel model;

    public RatingPresenter(RatingModel model) {
        this.model = model;
    }

    public void attachView(RatingActivity mainActivity) {
        view = mainActivity;
    }

    public void detachView() {
        view = null;
    }

    public void showRating() {
//        ContentValues cv = new ContentValues(1);
//        cv.put("IMAGE", view.getImage());
//        cv.put("TASKID", view.getTaskid());
//        cv.put("COMMENT", view.getComment());
//        cv.put("TASKTIME", view.getTasktime());
//        model.doTask(cv,new PlayModel.DoTaskCallback() {
//            @Override
//            public void onDo() {
//                view.toMainActivityWin();
//            }
//        });
    }
}
