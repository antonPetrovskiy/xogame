package com.game.xogame.views.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.GamesModel;
import com.game.xogame.presenters.GameInfoPresenter;
import com.game.xogame.views.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

public class GameInfoActivity extends AppCompatActivity {

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

    private ImageView statistic;
    private ImageView logo;
    private ImageView background;
    private ImageView back;
    private Button subscribe;
    private String count;

    private String gameid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        api = RetroClient.getApiService();
        GamesModel gamesModel = new GamesModel(api, getApplicationContext());
        presenter = new GameInfoPresenter(gamesModel);
        presenter.attachMainView(this);
        init();


    }

    public void init(){
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
        statistic = findViewById(R.id.imageView0);
        logo = findViewById(R.id.imageView3);
        background = findViewById(R.id.imageView4);

        final Bundle extras = getIntent().getExtras();

        gameid = extras.getString("GAMEID");
        title.setText(extras.getString("TITLE")+"");
        description.setText(extras.getString("DESCRIPTION")+"");
        name.setText(extras.getString("NAME")+"");

        date.setText(extras.getString("DATE")+"");
        time.setText(extras.getString("TIME")+"");
        tasks.setText(extras.getString("TASKS")+" заданий");
        money.setText(extras.getString("MONEY")+" uah");
        people.setText(extras.getString("PEOPLE")+" человек");
        count = extras.getString("PEOPLE");
        Picasso.with(this).load(extras.getString("LOGO")).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(logo);
        Picasso.with(this).load(extras.getString("BACKGROUND")).placeholder(R.drawable.unknow_wide).error(R.drawable.unknow_wide).into(background);

        if(extras.getString("SUBSCRIBE").equals("0")){
            subscribe.setText("Участвовать");
            subscribe.setBackgroundResource(R.drawable.regbtn);
            subscribe.setTextColor(Color.parseColor("#ffffff"));
        }else{
            subscribe.setText("Отказатся");
            subscribe.setBackgroundResource(R.drawable.registration_oval_button);
            subscribe.setTextColor(Color.parseColor("#F05A23"));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameInfoActivity.this, RatingGameActivity.class);
                startActivity(intent);
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subscribe.getText().equals("Участвовать")){
                    presenter.subscribeGame();
                    people.setText((Integer.parseInt(count)+1)+" человек");
                    count = (Integer.parseInt(count)+1)+"";
                    FirebaseMessaging.getInstance().subscribeToTopic(gameid)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                }else{
                    presenter.unsubscribeGame();
                    people.setText((Integer.parseInt(count)-1)+" человек");
                    count = (Integer.parseInt(count)-1)+"";
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(gameid)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                }
            }
        });
    }

    public void setButtonName(String s){
        if(s.equals("Участвовать")){
            subscribe.setBackgroundResource(R.drawable.regbtn);
            subscribe.setTextColor(Color.parseColor("#ffffff"));
        }else{
            subscribe.setBackgroundResource(R.drawable.registration_oval_button);
            subscribe.setTextColor(Color.parseColor("#F05A23"));
        }
        subscribe.setText(s);
    }

    public void showToast(String text){
        Toast.makeText(getApplicationContext(), text,
                Toast.LENGTH_SHORT).show();
    }

    public String getGameid(){
        return gameid;
    }

}
