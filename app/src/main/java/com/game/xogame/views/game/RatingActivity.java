package com.game.xogame.views.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.game.xogame.R;
import com.game.xogame.adapter.RatingAdapter;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Rating;
import com.game.xogame.models.RatingModel;
import com.game.xogame.presenters.RatingPresenter;
import com.game.xogame.views.main.MainActivity;

import java.util.LinkedList;
import java.util.List;

public class RatingActivity extends AppCompatActivity {

    public ApiService api;
    private RatingPresenter presenter;

    private RatingAdapter adapter;
    private LinearLayout load;
    private ListView listView;
    private SwipeRefreshLayout pullToRefresh;
    private RelativeLayout empty;
    private Button find;
    public ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        api = RetroClient.getApiService();
        RatingModel gamesModel = new RatingModel(api, getApplicationContext());
        presenter = new RatingPresenter(gamesModel);
        presenter.attachView(this);
        init();
        presenter.showRating();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        load = findViewById(R.id.targetView);
        listView = findViewById(R.id.gamelist);
        empty = findViewById(R.id.empty);
        find = findViewById(R.id.imageButton);
        back = findViewById(R.id.imageView1);
        pullToRefresh = findViewById(R.id.swiperefresh);
        final List<Rating> list = new LinkedList<>();
        adapter = new RatingAdapter(this, list);


        find.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        find.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        find.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        Intent intent = new Intent(RatingActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                presenter.showRating();
                adapter.notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void setList(List<Rating> list) {
        adapter = new RatingAdapter(this, list);
        listView.setAdapter(adapter);
        load.setVisibility(View.GONE);
        if (list.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }
    }
}
