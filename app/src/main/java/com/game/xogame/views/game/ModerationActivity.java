package com.game.xogame.views.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.adapter.ModerationAdapter;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Moderation;
import com.game.xogame.models.GamesModel;
import com.game.xogame.presenters.ModerationPresenter;
import com.game.xogame.views.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ModerationActivity extends AppCompatActivity {

    public ApiService api;
    private TextView company;
    private TextView game;
    private TextView replaced;
    private ImageView logo;
    private String gameid;
    private Button button;
    private ModerationPresenter presenter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_moderation);

        init();
        presenter.getModeration(gameid);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        company = findViewById(R.id.textView01);
        game = findViewById(R.id.textView02);
        replaced = findViewById(R.id.textView03);
        logo = findViewById(R.id.imageView);
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.imageButton);

        api = RetroClient.getApiService();
        GamesModel usersModel = new GamesModel(api, getApplicationContext());
        presenter = new ModerationPresenter(usersModel);
        presenter.attachView(this);

        gameid = getIntent().getStringExtra("gameid");
        company.setText(getIntent().getStringExtra("company"));
        game.setText(getIntent().getStringExtra("title"));
        replaced.setText(getString(R.string.activityModeration_moved) + " " + getString(R.string.activityModeration_from) + " " + getIntent().getStringExtra("latePlace") + " " + getString(R.string.activityModeration_to) + " " + getIntent().getStringExtra("newPlace") + " " + getString(R.string.activityModeration_place));
        Picasso.with(this).load(getIntent().getStringExtra("logo")).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(logo);

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        button.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        button.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        Intent intent = new Intent(ModerationActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("page","1");
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setList(List<Moderation> list) {
        ModerationAdapter adapter = new ModerationAdapter(this, list);
        listView.setAdapter(adapter);
    }


}
