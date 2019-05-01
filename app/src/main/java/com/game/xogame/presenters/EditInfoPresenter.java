package com.game.xogame.presenters;

import android.content.ContentValues;

import com.game.xogame.models.LoginModel;
import com.game.xogame.views.authentication.EditInfoActivity;

public class EditInfoPresenter {
    private EditInfoActivity viewEditInfo;
    private final LoginModel model;

    public EditInfoPresenter(LoginModel model) {
        this.model = model;
    }

    public void attachEditInfoView(EditInfoActivity editInfoActivity) {
        viewEditInfo = editInfoActivity;
    }

    public void detachView() {
        viewEditInfo = null;
    }



    public void editInfo() {
        ContentValues cv = new ContentValues(1);
        cv.put("NAME", viewEditInfo.getName());
        cv.put("EMAIL", viewEditInfo.getEmail());
        cv.put("IMAGE", viewEditInfo.getPhoto());
        model.editInfo(cv, new LoginModel.EditInfoCallback() {
            @Override
            public void onEdit() {
                if(model.getStatus().equals("error")){
                    viewEditInfo.showToast(model.getError()+"");
                }else{
                    viewEditInfo.showToast("Данные сохранены");
                    viewEditInfo.toMainActivity();
                }

            }
        });

    }



}
