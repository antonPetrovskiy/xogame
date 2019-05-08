package com.game.xogame.views.authentication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.LoginModel;
import com.game.xogame.presenters.LoginPresenter;
import com.game.xogame.views.main.MainActivity;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {


    @SuppressLint("StaticFieldLeak")
    static CountryCodePicker ccp;
    EditText editTextCarrierNumber;
    public ApiService api;
    private LoginPresenter presenter;
    private Button next;

    public static CountryCodePicker getPhoneView() {
        return ccp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // get or create SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        String token = sharedPref.getString("token", "null");
        assert token != null;
        if (!token.equals("null")) {
            toMainActivity();
        }

        init();

    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        api = RetroClient.getApiService();
        LoginModel usersModel = new LoginModel(api, getApplicationContext());
        presenter = new LoginPresenter(usersModel);
        presenter.attachLoginView(this);

        ccp = findViewById(R.id.ccp);
        editTextCarrierNumber = findViewById(R.id.phone_input);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        ccp.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {

            }

            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {
                //your code
            }

            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) {
                //your code
            }
        });

        //phoneNumber = findViewById(R.id.phone_input);
        next = findViewById(R.id.imageButton);
        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        next.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        next.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        if (ccp.getFullNumber().length() == 12) {
                            if (((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                                presenter.login();
                                //loading...
                            } else {
                                showToast("Нет подключения к интернету");
                            }
                        } else {
                            showToast("Номер не верный");
                        }
                        break;
                }
                return true;
            }
        });


    }

    public void toConfirmPhoneActivity() {
        Intent intent = new Intent(LoginActivity.this, ConfirmPhoneActivity.class);
        intent.putExtra("NUMBER", ccp.getFullNumber());
        startActivity(intent);
    }

    public void toMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void showToast(String s) {
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        View promptView = layoutInflater.inflate(R.layout.error, null);
//        final AlertDialog alertD = new AlertDialog.Builder(this).create();
//
//        TextView btnAdd1 = promptView.findViewById(R.id.textView1);
//        btnAdd1.setText(s);
//
//        alertD.setView(promptView);
//        alertD.show();
        Toast.makeText(getApplicationContext(), s,
                Toast.LENGTH_SHORT).show();
    }
}
