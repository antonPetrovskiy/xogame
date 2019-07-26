package com.game.xogame.views.profile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.presenters.MoneyPresenter;
import com.game.xogame.views.main.MainActivity;

public class MoneyActivity extends AppCompatActivity {
    public ApiService api;
    public MoneyPresenter presenter;
    private String type;
    private String count;
    private String gameid;

    public TextView header;
    public TextView money;
    public TextView way;
    public TextView info;
    public ImageView back;
    public Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            type = null;
            count = null;
        } else {
            type = extras.getString("type");
            count = extras.getString("money");
            gameid = extras.getString("gameid");
        }

        init();

    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    public void init() {

        api = RetroClient.getApiService();
        UserInfoModel model = new UserInfoModel(api, getApplicationContext());
        presenter = new MoneyPresenter(model);
        presenter.attachView(this);

        header = findViewById(R.id.textView1);
        money = findViewById(R.id.textView2);
        way = findViewById(R.id.textView3);
        info = findViewById(R.id.textView4);
        back = findViewById(R.id.imageView1);
        ok = findViewById(R.id.imageButton);

        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        String phone = sharedPref.getString("phone", "");
        String ccard = sharedPref.getString("ccard", "");
        String paypal = sharedPref.getString("email", "");
        if (type.equals("phone")) {
            header.setText(getString(R.string.activityMoney_sentMoneyPhone));
            money.setText(count + " ₴");
            way.setText(getString(R.string.activityMyWins_mobile));
            info.setText(phone);
            assert phone != null;
            if(phone.equals(""))
                ok.setEnabled(false);
        } else if (type.equals("ccard")){
            header.setText(getString(R.string.activityMoney_sentMoneyCard));
            money.setText(count + " ₴");
            way.setText(getString(R.string.activityMyWins_card));
            info.setText(ccard);
            assert ccard != null;
            if(ccard.equals(""))
                ok.setEnabled(false);
        } else if (type.equals("paypal")){
            header.setText(getString(R.string.R_string_activityMoney_sentMoneyPaypal));
            money.setText(count + " $");
            way.setText(getString(R.string.activityMyWins_email));
            info.setText(paypal);
            assert ccard != null;
            //if(ccard.equals(""))
                ok.setEnabled(false);
        }

        ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        ok.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        ok.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        presenter.sendMoney(type,gameid);
                        ok.setEnabled(false);
                        break;
                }
                return true;
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    public void success(){
        final MediaPlayer mp = MediaPlayer.create(MoneyActivity.this, R.raw.cash);
        mp.start();
        LayoutInflater layoutInflater = LayoutInflater.from(MoneyActivity.this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        TextView tw = promptView.findViewById(R.id.textView1);
        tw.setText(getString(R.string.activityMoney_success));
        alertD.setView(promptView);
        alertD.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent openMainActivity= new Intent(MoneyActivity.this, MainActivity.class);
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openMainActivity, 0);
                finish();
            }
        });
        alertD.show();
    }


}
