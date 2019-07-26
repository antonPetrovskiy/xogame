package com.game.xogame.views.game;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.game.xogame.R;
import com.game.xogame.views.main.MainActivity;
import com.game.xogame.views.profile.MoneyActivity;
import com.squareup.picasso.Picasso;

public class WinActivity extends AppCompatActivity {
    public static MainActivity activity;
    private TextView header;
    private TextView company;
    private TextView title;
    private TextView nickname;
    private TextView task;
    private ImageView logo;
    private ImageView photo;
    private ProgressBar progress;
    private ImageView toPaypal;
    private LottieAnimationView anim;
    private LinearLayout layAnimation;

    private Button ok;
    private TextView money;

    private String gameid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_win);
        if(activity!=null)
            activity.finish();
        Bundle extras = getIntent().getExtras();

        init();

        switch (extras.getString("POSITION")) {
            case "1":
                header.setText(getString(R.string.activityWin_first));
                break;
            case "2":
                header.setText(getString(R.string.activityWin_second));
                break;
            case "3":
                header.setText(getString(R.string.activityWin_third));
                break;
        }


        company.setText(extras.getString("COMPANY"));
        title.setText(extras.getString("TITLE"));
        nickname.setText(extras.getString("NICKNAME"));
        money.setText(extras.getString("MONEY"));
        task.setText(extras.getString("NUMBERTASK") + "/" + extras.getString("TASKS"));
        gameid = extras.getString("GAMEID");
        Picasso.with(this).load(extras.getString("LOGO")).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(logo);
        Picasso.with(this).load(extras.getString("PHOTO")).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(photo);
        int n = 1000/Integer.parseInt(extras.getString("TASKS"));
        n*= Integer.parseInt(extras.getString("NUMBERTASK")+"");
        progress.setProgress(n);
        anim = findViewById(R.id.animation);
        switch (extras.getString("POSITION")) {
            case "1":
                anim.setAnimation("win1.json");
                break;
            case "2":
                anim.setAnimation("win2.json");
                break;
            case "3":
                anim.setAnimation("win3.json");
                break;
        }

        anim.playAnimation();

        anim.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                anim.setVisibility(View.GONE);
                layAnimation.setVisibility(View.VISIBLE);
                ok.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init(){


        company = findViewById(R.id.textView01);
        header = findViewById(R.id.textView1);
        title = findViewById(R.id.textView02);
        nickname = findViewById(R.id.textView11);
        task = findViewById(R.id.textView14);
        money = findViewById(R.id.textView12);
        toPaypal = findViewById(R.id.paypal);
        layAnimation = findViewById(R.id.linearLayout6);

        logo = findViewById(R.id.imageView);
        photo = findViewById(R.id.imageView11);
        progress = findViewById(R.id.bar1);
        ok = findViewById(R.id.imageButton);

        toPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WinActivity.this, MoneyActivity.class);
                intent.putExtra("type", "paypal");
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
                        Intent openMainActivity= new Intent(WinActivity.this, MainActivity.class);
                        openMainActivity.putExtra("page","1");
                        openMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(openMainActivity);
                        finish();
                        break;
                }
                return true;
            }
        });
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.win);
        mp.start();
    }

    @Override
    public void onBackPressed() {

    }
}
