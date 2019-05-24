package com.game.xogame.views.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.adapter.WinsAdapter;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Game;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.presenters.MyWinsPresenter;
import com.game.xogame.views.main.MainActivity;

import java.util.List;

public class MyWinsActivity extends AppCompatActivity {

    public ApiService api;
    private MyWinsPresenter presenter;


    public ImageView back;
    private TextView money;
    private LinearLayout load;
    private ListView listView;
    private List<Game> gameList;

    private RelativeLayout empty;
    private ImageView money_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wins);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
        presenter.getMyWins();
    }

    public void init(){
        back = findViewById(R.id.imageView1);
        money = findViewById(R.id.textView20);
        money_icon = findViewById(R.id.imageView12);
        load = findViewById(R.id.targetView);
        empty = findViewById(R.id.empty);
        listView = findViewById(R.id.gamelist);
        api = RetroClient.getApiService();
        UserInfoModel usersModel = new UserInfoModel(api, getApplicationContext());
        presenter = new MyWinsPresenter(usersModel);
        presenter.attachMyWinsView(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWinsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("page","2");
                startActivity(intent);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(gameList.get(position).getRewardstatus().equals("1")) {
                    LayoutInflater layoutInflater = LayoutInflater.from(MyWinsActivity.this);
                    @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.popup_genderchooser, null);
                    final AlertDialog alertD = new AlertDialog.Builder(MyWinsActivity.this).create();

                    final int i = position;
                    TextView title = promptView.findViewById(R.id.textView1);
                    TextView btnAdd1 = promptView.findViewById(R.id.textView3);
                    TextView btnAdd2 = promptView.findViewById(R.id.textView2);
                    title.setText(getString(R.string.activityMyWins_sent) + " " + presenter.getWinsList().get(position).getReward() + " ₴");
                    btnAdd1.setText(getString(R.string.activityMyWins_mobile));
                    btnAdd2.setText(getString(R.string.activityMyWins_card));

                    btnAdd1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(MyWinsActivity.this, MoneyActivity.class);
                            intent.putExtra("type", "phone");
                            intent.putExtra("gameid", presenter.getWinsList().get(i).getGameid());
                            intent.putExtra("money", presenter.getWinsList().get(i).getReward());
                            startActivity(intent);
                            alertD.cancel();
                        }
                    });

                    btnAdd2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(MyWinsActivity.this, MoneyActivity.class);
                            intent.putExtra("type", "ccard");
                            intent.putExtra("gameid", presenter.getWinsList().get(i).getGameid());
                            intent.putExtra("money", presenter.getWinsList().get(i).getReward());
                            startActivity(intent);
                            alertD.cancel();
                        }
                    });

                    alertD.setView(promptView);
                    alertD.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        back.performClick();
    }

    @SuppressLint("SetTextI18n")
    public void setList(List<Game> list){
            WinsAdapter adapter = new WinsAdapter(this, list);
            listView.setAdapter(adapter);
            load.setVisibility(View.GONE);
            gameList = list;

        if(list.size()==0) {
            empty.setVisibility(View.VISIBLE);
            money.setText("0 ₴");
            money.setTextColor(Color.parseColor("#C4C4C4"));
            money_icon.setImageResource(R.drawable.mywin_noactive);
        }else{
            empty.setVisibility(View.GONE);
            int sum = 0;
            for (int i = 0; i < list.size(); i++) {
                try {
                    sum += Integer.parseInt(list.get(i).getReward());
                } catch (NumberFormatException e) {
                    sum = 0;
                }
            }
            money.setText(sum + " ₴");
            money.setTextColor(Color.parseColor("#F05A23"));
            money_icon.setImageResource(R.drawable.mywin_active);
        }
    }
}
