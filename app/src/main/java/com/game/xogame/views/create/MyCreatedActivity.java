package com.game.xogame.views.create;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.adapter.GamesNewAdapter;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.GameNew;
import com.game.xogame.models.CreateGameModel;
import com.game.xogame.presenters.MyCreatedPresenter;

import java.util.ArrayList;
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
    private List<GameNew> gameList;
    private List<GameNew> tmpgameList;
    private GamesNewAdapter adapter;
    private LinearLayout load;
    //private Button refresh;
    private TextView error;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_created);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

    }

    @Override
    protected void onResume() {
        update();
        super.onResume();
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
        //refresh = findViewById(R.id.refresh);
        load = findViewById(R.id.targetView);
        listView = findViewById(R.id.gamelist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(gameList.size()>position) {
                    switch (gameList.get(position).getStatus()) {
                        case "Draft":
                            Intent intent1 = new Intent(MyCreatedActivity.this, CreateGameActivity.class);
                            intent1.putExtra("name", gameList.get(position).getName_game());
                            intent1.putExtra("description", gameList.get(position).getDescription());
                            intent1.putExtra("photo", gameList.get(position).getBackground());
                            intent1.putExtra("gameid", gameList.get(position).getGameid());
                            intent1.putExtra("street", gameList.get(position).getAddressGame().getAddress_text());
                            intent1.putExtra("lat", gameList.get(position).getAddressGame().getCoordinateGame().getLat() + "");
                            intent1.putExtra("lng", gameList.get(position).getAddressGame().getCoordinateGame().getLon() + "");
                            intent1.putExtra("radius", gameList.get(position).getAddressGame().getRadius() + "");
                            intent1.putExtra("category", gameList.get(position).getCategory() + "");

                            ArrayList<String> list = new ArrayList<>();
                            for (int i = 0; i < gameList.get(position).getTasksGame().size(); i++) {
                                list.add(gameList.get(position).getTasksGame().get(i).getTask_description());
                            }
                            intent1.putExtra("tasks", list);
                            startActivity(intent1);
                            break;
                        case "Moderation":
                            Intent intent2 = new Intent(MyCreatedActivity.this, ModeratedActivity.class);
                            intent2.putExtra("name", gameList.get(position).getName_game());
                            intent2.putExtra("description", gameList.get(position).getDescription());
                            intent2.putExtra("photo", gameList.get(position).getBackground());
                            intent2.putExtra("gameid", gameList.get(position).getGameid());
                            intent2.putExtra("street", gameList.get(position).getAddressGame().getAddress_text());
                            intent2.putExtra("lat", gameList.get(position).getAddressGame().getCoordinateGame().getLat() + "");
                            intent2.putExtra("lng", gameList.get(position).getAddressGame().getCoordinateGame().getLon() + "");
                            intent2.putExtra("radius", gameList.get(position).getAddressGame().getRadius() + "");
                            intent2.putExtra("category", gameList.get(position).getCategory() + "");
                            ArrayList<String> list1 = new ArrayList<>();
                            for (int i = 0; i < gameList.get(position).getTasksGame().size(); i++) {
                                list1.add(gameList.get(position).getTasksGame().get(i).getTask_description());
                            }
                            intent2.putExtra("tasks", list1);
                            startActivity(intent2);
                            break;
                        case "Active":
                            break;
                        case "Canceled":
                            break;
                        case "Ended":
                            break;
                        case "Date and time":
                            Intent intent6 = new Intent(MyCreatedActivity.this, DateActivity.class);
                            startActivity(intent6);
                            break;
                    }
                }
            }
        });
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

    public void setList(List<GameNew> list) {
        final List<GameNew> l = list;
        tmpgameList = list;
        gameList = list;
        if (adapter == null) {
            adapter = new GamesNewAdapter(this, gameList);
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
        find.setVisibility(View.GONE);
    }

}
