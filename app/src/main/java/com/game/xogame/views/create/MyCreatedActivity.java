package com.game.xogame.views.create;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
                            Log.i("LOG_create" , " id0 - "+gameList.get(position).getGameid());
                            if(gameList.get(position).getAddressGame()!=null) {
                                intent1.putExtra("street", gameList.get(position).getAddressGame().getAddress_text() + "");
                                intent1.putExtra("lat", gameList.get(position).getAddressGame().getCoordinateGame().getLat() + "");
                                intent1.putExtra("lng", gameList.get(position).getAddressGame().getCoordinateGame().getLon() + "");
                                intent1.putExtra("radius", gameList.get(position).getAddressGame().getRadius() + "");
                            }
                            //Log.i("LOG_category" , gameList.get(position).getCategory()+"");
                            intent1.putExtra("category", gameList.get(position).getCategoryText() + "");
                            intent1.putExtra("categoryID", gameList.get(position).getCategory() + "");
                            intent1.putExtra("type", "draft");

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
                            if(gameList.get(position).getAddressGame()!=null) {
                                intent2.putExtra("street", gameList.get(position).getAddressGame().getAddress_text());
                                intent2.putExtra("lat", gameList.get(position).getAddressGame().getCoordinateGame().getLat() + "");
                                intent2.putExtra("lng", gameList.get(position).getAddressGame().getCoordinateGame().getLon() + "");
                                intent2.putExtra("radius", gameList.get(position).getAddressGame().getRadius() + "");
                            }
                            intent2.putExtra("category", gameList.get(position).getCategoryText() + "");
                            intent2.putExtra("type", "moderation");

                            ArrayList<String> list1 = new ArrayList<>();
                            for (int i = 0; i < gameList.get(position).getTasksGame().size(); i++) {
                                list1.add(gameList.get(position).getTasksGame().get(i).getTask_description());
                            }
                            intent2.putExtra("tasks", list1);
                            startActivity(intent2);
                            break;
                        case "Canceled":
                            Intent intent4 = new Intent(MyCreatedActivity.this, CreateGameActivity.class);
                            intent4.putExtra("name", gameList.get(position).getName_game());
                            intent4.putExtra("description", gameList.get(position).getDescription());
                            intent4.putExtra("photo", gameList.get(position).getBackground());
                            intent4.putExtra("gameid", gameList.get(position).getGameid());
                            intent4.putExtra("street", gameList.get(position).getAddressGame().getAddress_text());
                            intent4.putExtra("lat", gameList.get(position).getAddressGame().getCoordinateGame().getLat() + "");
                            intent4.putExtra("lng", gameList.get(position).getAddressGame().getCoordinateGame().getLon() + "");
                            intent4.putExtra("radius", gameList.get(position).getAddressGame().getRadius() + "");
                            intent4.putExtra("category", gameList.get(position).getCategoryText() + "");
                            intent4.putExtra("type", "canceled");
                            intent4.putExtra("badName", gameList.get(position).isWrong_name_game() + "");
                            intent4.putExtra("badPhoto", gameList.get(position).isWrong_background() + "");
                            intent4.putExtra("badDescription", gameList.get(position).isWrong_description() + "");
                            intent4.putExtra("badTasks", gameList.get(position).isWrong_tasks() + "");
                            intent4.putExtra("type", "canceled" + "");

                            ArrayList<String> list4 = new ArrayList<>();
                            for (int i = 0; i < gameList.get(position).getTasksGame().size(); i++) {
                                list4.add(gameList.get(position).getTasksGame().get(i).getTask_description());
                            }
                            intent4.putExtra("tasks", list4);
                            startActivity(intent4);
                            break;
                        case "Active":
                            Intent intent5 = new Intent(MyCreatedActivity.this, CreatedFeedsActivity.class);
                            intent5.putExtra("gameid",gameList.get(position).getGameid()+"");
                            intent5.putExtra("name",gameList.get(position).getName_game()+"");

                            startActivity(intent5);
                            break;
                        case "Ended":
                            Intent intent3 = new Intent(MyCreatedActivity.this, CreatedFeedsActivity.class);
                            intent3.putExtra("gameid",gameList.get(position).getGameid()+"");
                            intent3.putExtra("name",gameList.get(position).getName_game()+"");
                            intent3.putExtra("description", gameList.get(position).getDescription());
                            intent3.putExtra("photo", gameList.get(position).getBackground());
                            intent3.putExtra("street", gameList.get(position).getAddressGame().getAddress_text());
                            intent3.putExtra("category", gameList.get(position).getCategoryText() + "");
                            if(gameList.get(position).getStartdate().equals(gameList.get(position).getEnddate())){
                                intent3.putExtra("date", gameList.get(position).getStartdate() + "");
                            }else{
                                intent3.putExtra("date", gameList.get(position).getStartdate() + " - " + gameList.get(position).getEnddate());
                            }
                            intent3.putExtra("time", gameList.get(position).getStart_task_time() + " - " + gameList.get(position).getEnd_task_time());
                            intent3.putExtra("people", gameList.get(position).getFollowers() + "");
                            intent3.putExtra("tasks", gameList.get(position).getTasksGame().size() + "");
                            intent3.putExtra("money", gameList.get(position).getReward() + "");
                            intent3.putExtra("type","ended");
                            startActivity(intent3);
                            break;
                        case "Date and time":
                            Intent intent6 = new Intent(MyCreatedActivity.this, DateActivity.class);
                            intent6.putExtra("gameid",gameList.get(position).getGameid()+"");
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
        //empty.setVisibility(View.GONE);
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
            empty.setVisibility(View.VISIBLE);
            //refresh.setVisibility(View.GONE);
            //error.setText(getString(R.string.fragmentGames_nogames));
        }else{
            empty.setVisibility(View.GONE);
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
