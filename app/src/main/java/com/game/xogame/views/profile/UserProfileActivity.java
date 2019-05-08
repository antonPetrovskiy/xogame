package com.game.xogame.views.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.game.xogame.adapter.GamesAdapter;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Game;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.presenters.UserProfilePresenter;
import com.game.xogame.views.game.GameInfoActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.game.xogame.views.profile.FragmentProfile.setListViewHeightBasedOnChildren;

public class UserProfileActivity extends AppCompatActivity {

    private String userid;
    private TextView name;
    private TextView nickname;
    private TextView current;
    private TextView future;
    private ImageView photo;
    private ImageView myGames;
    private ListView list1;
    private ListView list2;
    private GamesAdapter adapter1;
    private GamesAdapter adapter2;
    private LinearLayout load;
    private ImageView back;
    private String photourl;
    private List<Game> gameNowList;
    private List<Game> gameFutureList;
    private RelativeLayout empty;

    private ApiService api;
    static private UserProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

    }

    public void init(){
        photo = findViewById(R.id.imageProfile);
        name = findViewById(R.id.textView1);
        nickname = findViewById(R.id.textView2);
        current = findViewById(R.id.textView9);
        future = findViewById(R.id.textView10);
        myGames = findViewById(R.id.myGames);
        load = findViewById(R.id.targetView);
        back = findViewById(R.id.imageViewBack);
        empty = findViewById(R.id.empty);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        myGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, MyGamesActivity.class);
                intent.putExtra("USERID",userid);
                startActivity(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(UserProfileActivity.this);
                View promptView = layoutInflater.inflate(R.layout.popup_image, null);
                final AlertDialog alertD = new AlertDialog.Builder(UserProfileActivity.this).create();
                alertD.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertD.getWindow().setDimAmount(0.9f);
                ImageView btnAdd1 = promptView.findViewById(R.id.imageView5);


                Picasso.with(getApplicationContext()).load(photourl).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(btnAdd1);
                //btnAdd1.setImageDrawable(image.getDrawable());
                alertD.setView(promptView);
                alertD.show();
            }
        });

        list1 = findViewById(R.id.list1);
        list2 = findViewById(R.id.list2);

        api = RetroClient.getApiService();
        UserInfoModel usersModel = new UserInfoModel(api, getApplicationContext());
        presenter = new UserProfilePresenter(usersModel);
        presenter.attachMainView(this);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            photourl = extras.getString("PHOTO")+"";
            userid = extras.getString("USERID"+"");
            name.setText(extras.getString("NAME")+"");
            nickname.setText(extras.getString("NICKNAME")+"");
            Picasso.with(this).load(extras.getString("PHOTO")+"").placeholder(R.drawable.unknow).error(R.drawable.unknow).into(photo);
            userid = extras.getString("USERID");
        }

        //presenter.showUserInfo(this);
        presenter.showMyGames(userid);
    }

    public void setNowList(List<Game> list){
        gameNowList = list;
        final List<Game> l = list;
        if(adapter1==null) {
            adapter1 = new GamesAdapter(this, gameNowList);
        }else{
            adapter1.notifyDataSetChanged();
        }
        list1.setAdapter(adapter1);
        setListViewHeightBasedOnChildren(list1);
        if(gameNowList.size()!=0)
            current.setVisibility(View.VISIBLE);


        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserProfileActivity.this, GameInfoActivity.class);
                intent.putExtra("GAMEID",l.get(position).getGameid());
                intent.putExtra("SUBSCRIBE",l.get(position).getSubscribe());
                intent.putExtra("TITLE", l.get(position).getTitle());
                intent.putExtra("NAME", l.get(position).getCompany());
                intent.putExtra("LOGO", l.get(position).getLogo());
                intent.putExtra("BACKGROUND", l.get(position).getBackground());
                intent.putExtra("DATE", l.get(position).getStartdate()+"-"+l.get(position).getEnddate());
                intent.putExtra("DESCRIPTION", l.get(position).getDescription());
                intent.putExtra("TASKS", l.get(position).getTasks());
                intent.putExtra("TIME", l.get(position).getStarttime()+"-"+l.get(position).getEndtime());
                intent.putExtra("MONEY", l.get(position).getReward());
                intent.putExtra("PEOPLE", l.get(position).getFollowers());
                intent.putExtra("STATISTIC", "true");
                startActivity(intent);
            }
        });

    }

    public void setFutureList(List<Game> list){
        final List<Game> l = list;
        gameFutureList = list;
        if(adapter2==null) {
            adapter2 = new GamesAdapter(this, gameFutureList);
        }else{
            adapter2.notifyDataSetChanged();
        }
        list2.setAdapter(adapter2);
        setListViewHeightBasedOnChildren(list2);
        if(gameFutureList.size()!=0)
            future.setVisibility(View.VISIBLE);

        load.setVisibility(View.GONE);
        if(gameNowList.size()==0 && gameFutureList.size()==0) {
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserProfileActivity.this, GameInfoActivity.class);
                intent.putExtra("GAMEID",l.get(position).getGameid());
                intent.putExtra("SUBSCRIBE",l.get(position).getSubscribe());
                intent.putExtra("TITLE", l.get(position).getTitle());
                intent.putExtra("NAME", l.get(position).getCompany());
                intent.putExtra("LOGO", l.get(position).getLogo());
                intent.putExtra("BACKGROUND", l.get(position).getBackground());
                intent.putExtra("DATE", l.get(position).getStartdate()+"-"+l.get(position).getEnddate());
                intent.putExtra("DESCRIPTION", l.get(position).getDescription());
                intent.putExtra("TASKS", l.get(position).getTasks());
                intent.putExtra("TIME", l.get(position).getStarttime()+"-"+l.get(position).getEndtime());
                intent.putExtra("MONEY", l.get(position).getReward());
                intent.putExtra("PEOPLE", l.get(position).getFollowers());
                intent.putExtra("STATISTIC", "true");
                startActivity(intent);
            }
        });

    }
}
