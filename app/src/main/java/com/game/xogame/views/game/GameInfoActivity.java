package com.game.xogame.views.game;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.GamesModel;
import com.game.xogame.presenters.GameInfoPresenter;
import com.game.xogame.views.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

public class GameInfoActivity extends AppCompatActivity {

    public String isStatistic;
    private ApiService api;
    private GameInfoPresenter presenter;
    private TextView title;
    private TextView description;
    private TextView name;
    private TextView date;
    private TextView time;
    private TextView tasks;
    private TextView money;
    private TextView people;
    public static MainActivity activity;
    private TextView address;
    private String avalible;
    private ImageView statistic;
    private ImageView logo;
    private ImageView background;
    private ImageView back;
    private Button subscribe;
    private String count;
    private String gameid;
    private String share;
    private RelativeLayout tutorialView;
    private TextView tutorialText;
    private Button tutorialButton;
    private ImageView tutorialArrowRight;
    private ImageView tutorialIconRight;
    private LinearLayout layMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        api = RetroClient.getApiService();
        GamesModel gamesModel = new GamesModel(api, getApplicationContext());
        presenter = new GameInfoPresenter(gamesModel);
        presenter.attachMainView(this);
        init();


    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    public void init() {
        back = findViewById(R.id.imageView1);
        subscribe = findViewById(R.id.imageButton);
        title = findViewById(R.id.textView1);
        description = findViewById(R.id.textView30);
        name = findViewById(R.id.textView0);
        date = findViewById(R.id.textView28);
        time = findViewById(R.id.textView32);
        tasks = findViewById(R.id.textView29);
        money = findViewById(R.id.textView33);
        people = findViewById(R.id.textView31);
        address = findViewById(R.id.textView303);
        statistic = findViewById(R.id.imageView0);
        logo = findViewById(R.id.imageView3);
        background = findViewById(R.id.imageView4);
        layMap = findViewById(R.id.layMap);

        tutorialView = findViewById(R.id.tutorial);
        tutorialText = findViewById(R.id.tutorial_text);
        tutorialButton = findViewById(R.id.tutorial_button);
        tutorialArrowRight = findViewById(R.id.tutorial_right_arrow);
        tutorialIconRight = findViewById(R.id.tutorial_right_icon);

        final Bundle extras = getIntent().getExtras();
        assert extras != null;
        gameid = extras.getString("GAMEID");
        title.setText(extras.getString("TITLE") + "");
        description.setText(extras.getString("DESCRIPTION") + "");
        name.setText(extras.getString("NAME") + "");
        date.setText(extras.getString("DATE") + "");
        time.setText(extras.getString("TIME") + "");
        tasks.setText(extras.getString("TASKS") + " " + getString(R.string.activityGameInfo_tasks));
        money.setText(extras.getString("MONEY") + " ₴");
        people.setText(extras.getString("PEOPLE") + " " + getString(R.string.activityGameInfo_people));
        count = extras.getString("PEOPLE");
        share = extras.getString("SHARE");
        avalible = extras.getString("AVALIBLE");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.unknow_wide);
        requestOptions.error(R.drawable.unknow_wide);
        requestOptions.override(1920, 1080);
        requestOptions.centerCrop();

        //if(extras.getString("USER")!=null && extras.getString("USER").equals("another"))
            //subscribe.setVisibility(View.GONE);
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(extras.getString("BACKGROUND")).thumbnail(0.3f).into(background);
        Picasso.with(this).load(extras.getString("LOGO")).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(logo);
        isStatistic = extras.getString("STATISTIC");

        if(avalible!=null) {
            if(avalible.equals("0")){
                subscribe.setVisibility(View.GONE);
            }else{
                subscribe.setVisibility(View.VISIBLE);
            }
        }


        if(extras.getString("ADDRESS")==null || extras.getString("ADDRESS").equals("")){
            layMap.setVisibility(View.GONE);
        }else{
            layMap.setVisibility(View.VISIBLE);
            address.setText(extras.getString("ADDRESS"));
        }

        if (extras.getString("STATISTIC").equals("true")) {
            statistic.setVisibility(View.VISIBLE);
            statistic.setImageDrawable(getDrawable(R.drawable.feed_statistics));
            statistic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GameInfoActivity.this, RatingGameActivity.class);
                    intent.putExtra("gameid", gameid);
                    startActivity(intent);
                }
            });
            subscribe.setText(getString(R.string.activityGameInfo_leave));
            subscribe.setBackgroundResource(R.drawable.registration_oval_button);
            subscribe.setTextColor(Color.parseColor("#F05A23"));
        } else {
            statistic.setVisibility(View.VISIBLE);
            statistic.setImageDrawable(getDrawable(R.drawable.share));
            statistic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share);
                    startActivity(Intent.createChooser(sharingIntent, ""));
                }
            });
            if ((extras.getString("SUBSCRIBE")+"").equals("0")) {
                subscribe.setText(getString(R.string.activityGameInfo_join));
                subscribe.setBackgroundResource(R.drawable.regbtn);
                subscribe.setTextColor(Color.parseColor("#ffffff"));
            } else {
                subscribe.setText(getString(R.string.activityGameInfo_leave));
                subscribe.setBackgroundResource(R.drawable.registration_oval_button);
                subscribe.setTextColor(Color.parseColor("#F05A23"));
            }
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q="+address.getText().toString()));
                startActivity(intent);
            }
        });

        subscribe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: // нажатие
                            subscribe.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                            break;
                        case MotionEvent.ACTION_UP: // отпускание

                                subscribe.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                                if (subscribe.getText().equals(getString(R.string.activityGameInfo_join))) {
                                    if(NotificationManagerCompat.from(getBaseContext()).areNotificationsEnabled()) {
                                        presenter.subscribeGame();
                                        people.setText((Integer.parseInt(count) + 1) + " " + getString(R.string.activityGameInfo_people));
                                        count = (Integer.parseInt(count) + 1) + "";
                                        subscribe.setBackgroundResource(R.drawable.registration_oval_button);
                                        subscribe.setTextColor(Color.parseColor("#F05A23"));
                                        subscribe.setText(getString(R.string.activityGameInfo_leave));
                                        FirebaseMessaging.getInstance().subscribeToTopic("/topics/agame" + gameid)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });
                                        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
                                        String token = sharedPref.getString("userid", "null");
                                        FirebaseMessaging.getInstance().subscribeToTopic("/topics/auser" + token)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });
                                    }else {
                                        LayoutInflater layoutInflater = LayoutInflater.from(GameInfoActivity.this);
                                        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
                                        final AlertDialog alertD = new AlertDialog.Builder(GameInfoActivity.this).create();
                                        TextView title = promptView.findViewById(R.id.textView1);
                                        title.setText(getString(R.string.activityGameInfo_errorpush));
                                        alertD.setView(promptView);
                                        alertD.show();
                                    }
                                } else {
                                    LayoutInflater layoutInflater = LayoutInflater.from(GameInfoActivity.this);
                                    @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.popup_genderchooser, null);
                                    final AlertDialog alertD = new AlertDialog.Builder(GameInfoActivity.this).create();
                                    TextView title = promptView.findViewById(R.id.textView1);
                                    TextView btnAdd1 = promptView.findViewById(R.id.textView3);
                                    TextView btnAdd2 = promptView.findViewById(R.id.textView2);
                                    title.setText(getString(R.string.popupUnsubscribe_unsubscribe));
                                    btnAdd1.setText(getString(R.string.popupSignout_yes));
                                    btnAdd2.setText(getString(R.string.popupSignout_no));

                                    btnAdd1.setOnClickListener(new View.OnClickListener() {
                                        @SuppressLint("ApplySharedPref")
                                        public void onClick(View v) {
                                            presenter.unsubscribeGame();
                                            people.setText((Integer.parseInt(count) - 1) + " " + getString(R.string.activityGameInfo_people));
                                            count = (Integer.parseInt(count) - 1) + "";
                                            subscribe.setBackgroundResource(R.drawable.regbtn);
                                            subscribe.setTextColor(Color.parseColor("#ffffff"));
                                            subscribe.setText(getString(R.string.activityGameInfo_join));
                                            alertD.cancel();
                                            FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/agame" + gameid)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    });
                                            SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
                                            String token = sharedPref.getString("userid", "null");
                                            FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/auser" + token)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    });
                                        }
                                    });

                                    btnAdd2.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            alertD.cancel();
                                        }
                                    });

                                    alertD.setView(promptView);
                                    alertD.show();
                                }
                            break;
                    }

                return true;
            }
        });

        checkTutorial();

    }

    public void setButtonName(String s) {


    }

    @Override
    public void onBackPressed() {
        if(activity==null) {
            Intent openMainActivity = new Intent(GameInfoActivity.this, MainActivity.class);
            openMainActivity.putExtra("page", "0");
            openMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(openMainActivity);
            finish();
        }else{
            super.onBackPressed();
        }
    }

    public String getGameid() {
        return gameid;
    }

    public void checkTutorial(){
        final SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        if(sharedPref.getString("tutorial_game", "false").equals("true")){
        tutorialView.setVisibility(View.VISIBLE);
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tutorialText.getText().toString().equals(getString(R.string.tutorial_share))){
                    tutorialArrowRight.clearAnimation();
                    tutorialView.setVisibility(View.GONE);
                    sharedPref.edit().putString("tutorial_game","false").commit();
                }else {
                    tutorialText.setText(getString(R.string.tutorial_share));
                    tutorialArrowRight.setVisibility(View.VISIBLE);
                    tutorialIconRight.setVisibility(View.VISIBLE);
                    TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
                            0.0f, 8.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
                    animation.setDuration(300);  // animation duration
                    animation.setRepeatCount(50);  // animation repeat count
                    animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
                    tutorialArrowRight.startAnimation(animation);
                }
            }
        });
        }
    }

}
