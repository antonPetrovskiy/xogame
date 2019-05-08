package com.game.xogame.presenters;

import android.content.ContentValues;

import com.game.xogame.models.LoginModel;
import com.game.xogame.views.authentication.ConfirmPhoneActivity;
import com.game.xogame.views.authentication.LoginActivity;

public class LoginPresenter {
    private final LoginModel model;
    private LoginActivity viewLogin;
    private ConfirmPhoneActivity viewConfirmPhone;

    public LoginPresenter(LoginModel model) {
        this.model = model;
    }

    public void attachLoginView(LoginActivity loginActivity) {
        viewLogin = loginActivity;
    }

    public void attachCofirmPhoneView(ConfirmPhoneActivity cofirmPhoneActivity) {
        viewConfirmPhone = cofirmPhoneActivity;
    }


    public void login() {
        ContentValues cv = new ContentValues(1);
        cv.put("NUMBER", LoginActivity.getPhoneView().getFullNumber());
        model.registratePhone(cv, new LoginModel.RegistratePhoneCallback() {
            @Override
            public void onRegistrate() {
                //viewLogin.showToast("Смc отправлен");
                viewLogin.toConfirmPhoneActivity();
            }
        });
    }

    public void confirmPhone() {
        ContentValues cv = new ContentValues(1);
        cv.put("CODE", viewConfirmPhone.getCodeView().getText().toString());
        cv.put("NUMBER", model.getNumber());
        model.checkCode(cv, new LoginModel.CheckCodeCallback() {
            @Override
            public void onCheck() {
                //if(model.getPhoneCallback().getError() == null || !model.getPhoneCallback().getError().equals("Bad code")) {
                if (model.getNewUser().equals("true")) {
                    viewConfirmPhone.toInfoActivity();
                } else if (model.getNewUser().equals("false")) {
                    viewConfirmPhone.toMainActivity();
                }
                //}
            }
        });
    }

    public void resentPhone() {
        ContentValues cv = new ContentValues(1);
        cv.put("NUMBER", LoginActivity.getPhoneView().getFullNumber());
        model.registratePhone(cv, new LoginModel.RegistratePhoneCallback() {
            @Override
            public void onRegistrate() {
                //viewConfirmPhone.showToast("Смc отправлен еще раз");
            }
        });
    }
}
