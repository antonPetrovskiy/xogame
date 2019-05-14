package com.game.xogame.views.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.xogame.adapter.GamesAdapter;
import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Game;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.presenters.MyGamesPresenter;
import com.game.xogame.views.main.MainActivity;

import java.util.List;

public class MyGamesActivity extends AppCompatActivity {
    public ApiService api;
    private MyGamesPresenter presenter;


    public ImageView back;
    private TextView count;
    private TextView time;
    private TextView title;
    private LinearLayout load;
    private ListView listView;

    private RelativeLayout empty;
    public Button find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_games);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        init();

        String userid = getIntent().getStringExtra("USERID");
        if(userid!=null){
            title.setText(getString(R.string.activityNotMyGames_history));
            presenter.getUserGames(userid);
        }else{
            presenter.getMyGames();
        }


    }

    public void init(){
        back = findViewById(R.id.imageView1);
        count = findViewById(R.id.textView16);
        time = findViewById(R.id.textView17);
        load = findViewById(R.id.targetView);
        listView = findViewById(R.id.gamelist);
        title = findViewById(R.id.textView1);
        empty = findViewById(R.id.empty);
        find = findViewById(R.id.imageButton);
        api = RetroClient.getApiService();
        UserInfoModel usersModel = new UserInfoModel(api, getApplicationContext());
        presenter = new MyGamesPresenter(usersModel);
        presenter.attachMyGamesView(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGamesActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void setList(List<Game> list){
        GamesAdapter adapter = new GamesAdapter(this, list);
        listView.setAdapter(adapter);
        load.setVisibility(View.GONE);

        if(list.size()==0) {
            empty.setVisibility(View.VISIBLE);
            count.setText("0");
            time.setText("0");
        }else{
            empty.setVisibility(View.GONE);
            count.setText(list.size()+"");
            int n=0;
            for(int i = 0; i < list.size(); i ++){
                n+=Integer.parseInt(list.get(i).getComplited());
            }
            time.setText(n+"");
        }
    }
}
