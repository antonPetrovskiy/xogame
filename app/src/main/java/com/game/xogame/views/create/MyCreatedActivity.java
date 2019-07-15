package com.game.xogame.views.create;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.adapter.GamesAdapter;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Game;
import com.game.xogame.models.CreateGameModel;
import com.game.xogame.presenters.MyCreatedPresenter;

import java.util.LinkedList;
import java.util.List;

public class MyCreatedActivity extends AppCompatActivity {

    public static MyCreatedPresenter presenter;
    private static RelativeLayout empty;
    public ApiService api;
    private ImageView back;
    private ImageView create;
    private Button find;
    private SwipeRefreshLayout pullToRefresh;
    private List<Game> gameList;
    private List<Game> tmpgameList;
    private GamesAdapter adapter;
    private LinearLayout load;
    private Button refresh;
    private TextView error;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_created);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
        update();
    }

    public void init(){
        back = findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        create = findViewById(R.id.imageView0);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCreatedActivity.this, CreateGameActivity.class);
                startActivity(intent);
            }
        });
        find = findViewById(R.id.imageButton);
        find.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        find.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        find.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        Intent intent = new Intent(MyCreatedActivity.this, CreateGameActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        api = RetroClient.getApiService();
        CreateGameModel model = new CreateGameModel(api, getApplicationContext());
        presenter = new MyCreatedPresenter(model);
        presenter.attachView(this);

        error = findViewById(R.id.error);
        refresh = findViewById(R.id.refresh);
        load = findViewById(R.id.targetView);
        listView = findViewById(R.id.gamelist);
        pullToRefresh = findViewById(R.id.swiperefresh);
        empty = findViewById(R.id.empty);
        gameList = new LinkedList<>();
        adapter = null;
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gameList = new LinkedList<>();
                adapter = null;
                presenter.getGames();
                empty.setVisibility(View.GONE);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void update() {
        gameList = new LinkedList<>();
        adapter = null;
        presenter.getGames();
        empty.setVisibility(View.GONE);
    }

    public void setList(List<Game> list) {
        final List<Game> l = list;
        tmpgameList = list;
        gameList = list;
        if (adapter == null) {
            adapter = new GamesAdapter(this, gameList);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        if(list == null || list.size()==0){
            //empty.setVisibility(View.VISIBLE);
            //refresh.setVisibility(View.GONE);
            error.setText(getString(R.string.fragmentGames_nogames));
        }
        load.setVisibility(View.GONE);

    }

    public void setError(String msg){
        load.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        error.setText(msg);
        refresh.setVisibility(View.GONE);
    }

}
