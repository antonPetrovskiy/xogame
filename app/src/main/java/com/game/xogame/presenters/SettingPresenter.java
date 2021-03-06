package com.game.xogame.presenters;

import android.content.ContentValues;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.game.xogame.R;
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
                if((model.user.getGender()+"").equals("Male")){
                    viewSetting.setGender(viewSetting.getString(R.string.popupGenderChooser_man));
                }else if((model.user.getGender()+"").equals("Female")){
                    viewSetting.setGender(viewSetting.getString(R.string.popupGenderChooser_woman));
                }

                Log.i("LOG_logmail" , model.user.getVerifyMail());
                if(model.user.getVerifyMail().equals("true")){
                    viewSetting.confirm.setText(viewSetting.getString(R.string.activitySetting_confirmed));
                    viewSetting.confirm.setTextColor(Color.parseColor("#80586575"));
                }else{
                    viewSetting.confirm.setText(viewSetting.getString(R.string.activitySetting_notConfirmed));
                    viewSetting.confirm.setTextColor(Color.parseColor("#F08C3C"));
                }

                viewSetting.setAge(model.user.getBirthday() + "");
                viewSetting.setEmail(model.user.getMail() + "");
                //viewSetting.setInfo(model.user.getAbout() + "");
                viewSetting.setCountry(model.user.getCountry() + "");
                viewSetting.setCity(model.user.getCity() + "");
                //viewSetting.getLoadView().setVisibility(View.GONE);
                viewSetting.main.setVisibility(View.VISIBLE);
            }
        });

    }

    public void editInfo() {
        ContentValues cv = new ContentValues(1);
        cv.put("NAME", viewSetting.getName());
        //cv.put("INFO", viewSetting.getInfo());
        if(viewSetting.getGender().equals(viewSetting.getString(R.string.popupGenderChooser_man))){
            cv.put("GENDER", "Male");
        }else if(viewSetting.getGender().equals(viewSetting.getString(R.string.popupGenderChooser_woman))){
            cv.put("GENDER", "Female");
        }else{
            cv.put("GENDER", viewSetting.getGender());
        }

        cv.put("AGE", viewSetting.getAge());
        cv.put("EMAIL", viewSetting.getEmail());
        cv.put("COUNTRY", viewSetting.getCountry());
        cv.put("CITY", viewSetting.getCity());

        model.editInfo(cv, new UserInfoModel.EditInfoCallback() {
            @Override
            public void onEdit() {
                //viewSetting.showToast("Данные сохранены");
                viewSetting.getLoadView().setVisibility(View.GONE);
                viewSetting.onBack();
            }
        });
    }

    public void sentVerify() {
        ContentValues cv = new ContentValues(1);
        cv.put("EMAIL", viewSetting.getEmail());

        model.verify(cv, new UserInfoModel.VerifyCallback() {
            @Override
            public void onEdit() {
                viewSetting.error(viewSetting.getString(R.string.activitySetting_sent));
                viewSetting.getLoadView().setVisibility(View.GONE);
            }
        });
    }

}
