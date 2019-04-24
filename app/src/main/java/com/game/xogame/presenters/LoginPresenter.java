package com.game.xogame.presenters;

import android.content.ContentValues;
import android.util.Log;

import com.game.xogame.models.LoginModel;
import com.game.xogame.views.authentication.ConfirmPhoneActivity;
import com.game.xogame.views.authentication.LoginActivity;

public class LoginPresenter {
    private LoginActivity viewLogin;
    private ConfirmPhoneActivity viewConfirmPhone;
    private final LoginModel model;

    public LoginPresenter(LoginModel model) {
        this.model = model;
    }

    public void attachLoginView(LoginActivity loginActivity) {
        viewLogin = loginActivity;
    }

    public void attachCofirmPhoneView(ConfirmPhoneActivity cofirmPhoneActivity) {
        viewConfirmPhone = cofirmPhoneActivity;
    }

    public void detachView() {
        viewLogin = null;
    }

    public void login() {
        ContentValues cv = new ContentValues(1);
        cv.put("NUMBER", viewLogin.getPhoneView().getFullNumber());
        model.registratePhone(cv,new LoginModel.RegistratePhoneCallback() {
            @Override
            public void onRegistrate() {
                viewLogin.showToast("Смc отправлен");
                viewLogin.toConfirmPhoneActivity();
            }
        });
    }

    public void confirmPhone(){
        ContentValues cv = new ContentValues(1);
        cv.put("CODE", viewConfirmPhone.getCodeView().getText().toString());
        cv.put("NUMBER", model.getNumber());
        model.checkCode(cv,new LoginModel.CheckCodeCallback() {
            @Override
            public void onCheck() {
                //if(model.getPhoneCallback().getError() == null || !model.getPhoneCallback().getError().equals("Bad code")) {
                    if (model.getNewUser().equals("true")) {
                        viewConfirmPhone.toInfoActivity();
                    } else if (model.getNewUser().equals("false")) {
                        viewConfirmPhone.toMainActivity();
                    } else if (model.getNewUser().equals("no")) {

                    }
                //}
            }
        });
    }

    public void resentPhone(){
        ContentValues cv = new ContentValues(1);
        cv.put("NUMBER", viewLogin.getPhoneView().getFullNumber());
        model.registratePhone(cv,new LoginModel.RegistratePhoneCallback() {
            @Override
            public void onRegistrate() {
                viewConfirmPhone.showToast("Смc отправлен еще раз");
            }
        });
    }
}
