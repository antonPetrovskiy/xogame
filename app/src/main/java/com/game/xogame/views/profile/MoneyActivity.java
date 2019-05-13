package com.game.xogame.views.profile;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        if (type.equals("phone")) {
            header.setText(getString(R.string.txt_sentMoneyPhone));
            money.setText(count + " ₴");
            way.setText(getString(R.string.txt_phone));
            info.setText(phone);
            assert phone != null;
            if(phone.equals(""))
                ok.setEnabled(false);
        } else {
            header.setText(getString(R.string.txt_sentMoneyCard));
            money.setText(count + " ₴");
            way.setText(getString(R.string.txt_card));
            info.setText(ccard);
            assert ccard != null;
            if(ccard.equals(""))
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
                        onBackPressed();
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


}
