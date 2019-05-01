package com.game.xogame.views.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.game.xogame.R;
import com.game.xogame.adapter.FeedsAdapter;
import com.game.xogame.adapter.RatingAdapter;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Feed;
import com.game.xogame.entity.Rating;
import com.game.xogame.models.RatingModel;
import com.game.xogame.presenters.RatingPresenter;
import com.game.xogame.views.main.MainActivity;
import com.game.xogame.views.profile.MyGamesActivity;

import java.util.LinkedList;
import java.util.List;

public class RatingActivity extends AppCompatActivity {

    private ApiService api;
    private RatingPresenter presenter;

    private RatingAdapter adapter;
    private LinearLayout load;
    private ListView listView;
    private SwipeRefreshLayout pullToRefresh;
    private RelativeLayout empty;
    private Button find;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        api = RetroClient.getApiService();
        RatingModel gamesModel = new RatingModel(api, getApplicationContext());
        presenter = new RatingPresenter(gamesModel);
        presenter.attachView(this);
        init();
        presenter.showRating();
    }

    public void init(){
        load = findViewById(R.id.targetView);
        listView = findViewById(R.id.gamelist);
        empty = findViewById(R.id.empty);
        find = findViewById(R.id.imageButton);
        back = findViewById(R.id.imageView1);
        pullToRefresh = findViewById(R.id.swiperefresh);
        final List<Rating> list = new LinkedList<>();
        adapter = new RatingAdapter(this,list);



        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RatingActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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

    public void setList(List<Rating> list){
        adapter = new RatingAdapter(this, list);
        listView.setAdapter(adapter);
        load.setVisibility(View.GONE);
        if(list.size()==0) {
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }
    }
}
