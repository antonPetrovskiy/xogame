package com.game.xogame.views.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.views.main.MainActivity;
import com.game.xogame.views.profile.MoneyActivity;
import com.squareup.picasso.Picasso;

public class WinActivity extends AppCompatActivity {

    private TextView company;
    private TextView title;
    private TextView nickname;
    private TextView task;
    private ImageView logo;
    private ImageView photo;
    private ProgressBar progress;
    private TextView toMobile;
    private TextView toCard;

    private Button ok;
    private TextView money;

    private String gameid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_win);

        Bundle extras = getIntent().getExtras();

        init();

        company.setText(extras.getString("COMPANY"));
        title.setText(extras.getString("TITLE"));
        nickname.setText(extras.getString("NICKNAME"));
        money.setText(extras.getString("MONEY"));
        task.setText(extras.getString("NUMBERTASK") + "/" + extras.getString("TASKS"));
        gameid = extras.getString("GAMEID");
        Picasso.with(this).load(extras.getString("LOGO")).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(logo);
        Picasso.with(this).load(extras.getString("PHOTO")).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(photo);
        int n = 1000/Integer.parseInt(extras.getString("TASKS"));
        n*= Integer.parseInt(extras.getString("NUMBERTASK"));
        progress.setProgress(n);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init(){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.win);
        mp.start();
        company = findViewById(R.id.textView01);
        title = findViewById(R.id.textView02);
        nickname = findViewById(R.id.textView11);
        task = findViewById(R.id.textView14);
        money = findViewById(R.id.textView12);
        toMobile = findViewById(R.id.textView16);
        toCard = findViewById(R.id.textView15);

        logo = findViewById(R.id.imageView);
        photo = findViewById(R.id.imageView11);
        progress = findViewById(R.id.bar1);
        ok = findViewById(R.id.imageButton);

        toMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WinActivity.this, MoneyActivity.class);
                intent.putExtra("type", "phone");
                intent.putExtra("gameid", gameid);
                intent.putExtra("money", money.getText()+"");
                startActivity(intent);
            }
        });

        toCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WinActivity.this, MoneyActivity.class);
                intent.putExtra("type", "card");
                intent.putExtra("gameid", gameid);
                intent.putExtra("money", money.getText()+"");
                startActivity(intent);
            }
        });

        ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        ok.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        ok.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        Intent intent = new Intent(WinActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
