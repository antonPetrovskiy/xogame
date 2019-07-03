package com.game.xogame.presenters;

import android.content.ContentValues;

import com.game.xogame.R;
import com.game.xogame.models.PlayModel;
import com.game.xogame.views.game.PlayActivity;

public class PlayPresenter {
    private final PlayModel model;
    private PlayActivity view;

    public PlayPresenter(PlayModel model) {
        this.model = model;
    }

    public void attachView(PlayActivity mainActivity) {
        view = mainActivity;
    }


    public void sendTask() {
        ContentValues cv = new ContentValues(1);
        cv.put("IMAGE", view.getImage());
        cv.put("TASKID", view.getTaskid());
        cv.put("COMMENT", view.getComment());
        cv.put("TASKTIME", view.getTasktime());
        model.doTask(cv, new PlayModel.DoTaskCallback() {
            @Override
            public void onDo() {
                if(model.getStatus().equals("error")){
                    switch (model.getError()){
                        case "timeout":
                            view.showToast(view.getString(R.string.error_cannotReachServer));
                            break;
                        case "Game ended":
                            view.showToast(view.getString(R.string.error_gameEnded));
                            break;
                        case "Second post":
                            view.showToast(view.getString(R.string.error_secondPost));
                            break;
                        case "File don't upload":
                            view.showToast(view.getString(R.string.error_dontUpload));
                            break;
                        default:
                            view.showToast(model.getError()+"");
                            break;
                    }

                }else{
                    view.timer.cancel();
                    view.position_str = model.position;
                    view.toMainActivityWin();
                }
            }
        });
    }

}
