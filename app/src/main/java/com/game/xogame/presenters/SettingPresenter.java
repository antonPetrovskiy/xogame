package com.game.xogame.presenters;

import android.content.ContentValues;
import android.view.View;

import com.game.xogame.models.UserInfoModel;
import com.game.xogame.views.profile.SettingActivity;

public class SettingPresenter {
    private SettingActivity viewSetting;
    private final UserInfoModel model;

    public SettingPresenter(UserInfoModel model) {
        this.model = model;
    }

    public void attachSettingView(SettingActivity mainActivity) {
        viewSetting = mainActivity;
    }

    public void detachView() {
        viewSetting = null;
    }

    public void showUserInfo() {
        model.getInfo(new UserInfoModel.GetInfoCallback() {
            @Override
            public void onGet() {
                //Log.i("LOG_log" , model.user.getPhoto());
                viewSetting.setName(model.user.getName() + "");
                viewSetting.setGender(model.user.getGender() + "");
                viewSetting.setAge(model.user.getBirthday() + "");
                viewSetting.setEmail(model.user.getMail() + "");
                //viewSetting.setInfo(model.user.getAbout() + "");
                viewSetting.setCard(model.user.getCard() + "");
                viewSetting.setCountry(model.user.getCountry() + "");
                viewSetting.setCity(model.user.getCity() + "");
                viewSetting.getLoadView().setVisibility(View.GONE);
            }
        });

    }

    public void editInfo() {
        ContentValues cv = new ContentValues(1);
        cv.put("NAME", viewSetting.getName());
        //cv.put("INFO", viewSetting.getInfo());
        cv.put("GENDER", viewSetting.getGender());
        cv.put("AGE", viewSetting.getAge());
        cv.put("EMAIL", viewSetting.getEmail());
        cv.put("COUNTRY", viewSetting.getCountry());
        cv.put("CITY", viewSetting.getCity());
        cv.put("CARD", viewSetting.getCard());

        model.editInfo(cv, new UserInfoModel.EditInfoCallback() {
            @Override
            public void onEdit() {
                //viewSetting.showToast("Данные сохранены");
                viewSetting.getLoadView().setVisibility(View.GONE);
                viewSetting.onBack();
            }
        });
    }
}
