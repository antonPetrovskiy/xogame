package com.game.xogame.views.authentication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.LoginModel;
import com.game.xogame.models.SmsListener;
import com.game.xogame.presenters.LoginPresenter;
import com.game.xogame.presenters.MainPresenter;
import com.game.xogame.views.main.MainActivity;




public class ConfirmPhoneActivity extends AppCompatActivity {

    private ApiService api;
    private LoginPresenter presenter;
    private String phone;
    public static EditText codeText;

    private Button next;
    private Button resent;
    private TextView text;
    private  TextView time;
    LottieAnimationView load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_phone);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        init();
        startTimer();
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            phone = null;
        } else {
            phone = extras.getString("NUMBER");
            text.setText("На ваш номер +"+phone+" выслано сообщение с кодом подтверждения");
        }
        //new SmsListener();
    }

    public void init(){
        text = findViewById(R.id.textView5);
        load = findViewById(R.id.targetView);
        codeText = findViewById(R.id.editText3);
        time = findViewById(R.id.textView21);
        api = RetroClient.getApiService();
        LoginModel usersModel = new LoginModel(api, getApplicationContext());
        presenter = new LoginPresenter(usersModel);
        presenter.attachCofirmPhoneView(this);

        next = findViewById(R.id.imageButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.confirmPhone();
            }
        });

        resent = findViewById(R.id.imageButton1);
        resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resent.setVisibility(View.GONE);
                load.setVisibility(View.VISIBLE);
                presenter.resentPhone();
                startTimer();
            }
        });




    }

    public void toInfoActivity(){
        Intent intent = new Intent(ConfirmPhoneActivity.this, EditInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void toMainActivity(){
        Intent intent = new Intent(ConfirmPhoneActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void startTimer(){
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                time.setText((millisUntilFinished / 1000)+"");
            }

            public void onFinish() {
                time.setText("");
                load.setVisibility(View.GONE);
                resent.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public EditText getCodeView(){
        return codeText;
    }

    public void showToast(String s){
        Toast.makeText(getApplicationContext(), s,
                Toast.LENGTH_SHORT).show();
    }
}
