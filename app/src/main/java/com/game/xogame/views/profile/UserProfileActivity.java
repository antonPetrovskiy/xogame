package com.game.xogame.views.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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

public class UserProfileActivity extends AppCompatActivity {

    static public UserProfilePresenter presenter;
    private String userid;
    public TextView name;
    public TextView nickname;
    private TextView current;
    private TextView future;
    public ImageView photo;
    public ImageView myGames;
    private ListView list1;
    private ListView list2;
    private GamesAdapter adapter1;
    private GamesAdapter adapter2;
    private LinearLayout load;
    public ImageView back;
    private String photourl;
    private List<Game> gameNowList;
    public List<Game> gameFutureList;
    private RelativeLayout empty;
    public ApiService api;
    private TextView textHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

    }

    @SuppressLint("SetTextI18n")
    public void init() {
        photo = findViewById(R.id.imageProfile);
        name = findViewById(R.id.textView1);
        nickname = findViewById(R.id.textView2);
        current = findViewById(R.id.textView9);
        future = findViewById(R.id.textView10);
        myGames = findViewById(R.id.myGames);
        load = findViewById(R.id.targetView);
        back = findViewById(R.id.imageViewBack);
        textHolder = findViewById(R.id.textHolder);
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
                intent.putExtra("USERID", userid);
                startActivity(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photourl!=null && !photourl.equals("") && !photourl.equals("null")) {
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
            }
        });

        list1 = findViewById(R.id.list1);
        list2 = findViewById(R.id.list2);

        api = RetroClient.getApiService();
        UserInfoModel usersModel = new UserInfoModel(api, getApplicationContext());
        presenter = new UserProfilePresenter(usersModel);
        presenter.attachMainView(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            userid = extras.getString("USERID" + "");
            name.setText(extras.getString("NAME") + "");
            nickname.setText(extras.getString("NICKNAME") + "");
            //Picasso.with(this).load(extras.getString("PHOTO") + "").placeholder(R.drawable.unknow).error(R.drawable.unknow).into(photo);
            userid = extras.getString("USERID");
        }
        photourl = extras.getString("PHOTO") + "";
        if (!extras.getString("PHOTO").equals("")) {
            setPhoto(extras.getString("PHOTO"));
        } else {
            setPhoto("");
        }
        //presenter.showUserInfo(this);
        presenter.showMyGames(userid);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);

            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        ViewGroup.LayoutParams.MATCH_PARENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount()));

        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public void setNowList(List<Game> list) {
        gameNowList = list;
        final List<Game> l = list;
        if (adapter1 == null) {
            adapter1 = new GamesAdapter(this, gameNowList);
        } else {
            adapter1.notifyDataSetChanged();
        }
        list1.setAdapter(adapter1);
        setListViewHeightBasedOnChildren(list1);
        if (gameNowList.size() != 0)
            current.setVisibility(View.VISIBLE);


        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(l.get(position).getGameAvaible().equals("1")) {
                    Intent intent = new Intent(UserProfileActivity.this, GameInfoActivity.class);
                    intent.putExtra("GAMEID", l.get(position).getGameid());
                    intent.putExtra("SUBSCRIBE", l.get(position).getSubscribe());
                    intent.putExtra("TITLE", l.get(position).getTitle());
                    intent.putExtra("NAME", l.get(position).getCompany());
                    intent.putExtra("LOGO", l.get(position).getLogo());
                    intent.putExtra("BACKGROUND", l.get(position).getBackground());
                    intent.putExtra("DATE", l.get(position).getStartdate() + "-" + l.get(position).getEnddate());
                    intent.putExtra("DESCRIPTION", l.get(position).getDescription());
                    intent.putExtra("TASKS", l.get(position).getTasks());
                    intent.putExtra("TIME", l.get(position).getStarttime() + "-" + l.get(position).getEndtime());
                    intent.putExtra("MONEY", l.get(position).getReward());
                    intent.putExtra("PEOPLE", l.get(position).getFollowers());
                    intent.putExtra("ADDRESS", l.get(position).getAddress());
                    intent.putExtra("CATEGORY", l.get(position).getCategory());
                    intent.putExtra("STATISTIC", "true");
                    intent.putExtra("USER", "another");
                    startActivity(intent);
                }else{
                    LayoutInflater layoutInflater = LayoutInflater.from(UserProfileActivity.this);
                    @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
                    final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(UserProfileActivity.this).create();
                    TextView btnAdd1 = promptView.findViewById(R.id.textView1);
                    btnAdd1.setText(getString(R.string.activityUserProfile_notsupported));
                    alertD.setView(promptView);
                    alertD.show();
                }
            }
        });

    }

    public void setFutureList(List<Game> list) {
        final List<Game> l = list;
        gameFutureList = list;
        if (adapter2 == null) {
            adapter2 = new GamesAdapter(this, gameFutureList);
        } else {
            adapter2.notifyDataSetChanged();
        }
        list2.setAdapter(adapter2);
        setListViewHeightBasedOnChildren(list2);
        if (gameFutureList.size() != 0)
            future.setVisibility(View.VISIBLE);

        load.setVisibility(View.GONE);
        if (gameNowList.size() == 0 && gameFutureList.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(l.get(position).getGameAvaible().equals("1")) {
                    Intent intent = new Intent(UserProfileActivity.this, GameInfoActivity.class);
                    intent.putExtra("GAMEID", l.get(position).getGameid());
                    intent.putExtra("SUBSCRIBE", l.get(position).getSubscribe());
                    intent.putExtra("TITLE", l.get(position).getTitle());
                    intent.putExtra("NAME", l.get(position).getCompany());
                    intent.putExtra("LOGO", l.get(position).getLogo());
                    intent.putExtra("BACKGROUND", l.get(position).getBackground());
                    intent.putExtra("DATE", l.get(position).getStartdate() + "-" + l.get(position).getEnddate());
                    intent.putExtra("DESCRIPTION", l.get(position).getDescription());
                    intent.putExtra("TASKS", l.get(position).getTasks());
                    intent.putExtra("TIME", l.get(position).getStarttime() + "-" + l.get(position).getEndtime());
                    intent.putExtra("MONEY", l.get(position).getReward());
                    intent.putExtra("PEOPLE", l.get(position).getFollowers());
                    intent.putExtra("ADDRESS", l.get(position).getAddress());
                    intent.putExtra("CATEGORY", l.get(position).getCategory());
                    intent.putExtra("STATISTIC", "true");
                    startActivity(intent);
                }else{
                    LayoutInflater layoutInflater = LayoutInflater.from(UserProfileActivity.this);
                    @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
                    final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(UserProfileActivity.this).create();
                    TextView btnAdd1 = promptView.findViewById(R.id.textView1);
                    btnAdd1.setText(getString(R.string.activityUserProfile_notsupported));
                    alertD.setView(promptView);
                    alertD.show();
                }
            }
        });

    }

    public void setPhoto(String s) {
        if(s.equals(""))
            s = "q";
        textHolder.setText(nickname.getText().toString().substring(0, 1).toUpperCase());
        this.photo.setImageResource(getPlaceholder(nickname.getText().toString()));
        Picasso.with(this).load(s + "").placeholder(getPlaceholder(nickname.getText().toString())).error(getPlaceholder(nickname.getText().toString())).into(this.photo, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                textHolder.setText("");
            }

            @Override
            public void onError() {

            }
        });
    }

    private int getPlaceholder(String s) {
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZЯЧСМИТЬБЮФЫВАПРОЛДЖЭЁЙЦУКЕНГШЩЗХЪІЄ0123456789";
        int n = abc.indexOf(s.substring(0, 1).toUpperCase());
        if (n == 0 || n == 20 || n == 40 || n == 60) {
            return R.color.color1;
        }
        if (n == 1 || n == 21 || n == 41 || n == 61) {
            return R.color.color2;
        }
        if (n == 2 || n == 22 || n == 42 || n == 62) {
            return R.color.color3;
        }
        if (n == 3 || n == 23 || n == 43 || n == 63) {
            return R.color.color4;
        }
        if (n == 4 || n == 24 || n == 44 || n == 64) {
            return R.color.color5;
        }
        if (n == 5 || n == 25 || n == 45 || n == 65) {
            return R.color.color6;
        }
        if (n == 6 || n == 26 || n == 46 || n == 66) {
            return R.color.color7;
        }
        if (n == 7 || n == 27 || n == 47 || n == 67) {
            return R.color.color8;
        }
        if (n == 8 || n == 28 || n == 48 || n == 68) {
            return R.color.color9;
        }
        if (n == 9 || n == 29 || n == 49 || n == 69) {
            return R.color.color10;
        }
        if (n == 10 || n == 30 || n == 50 || n == 70) {
            return R.color.color11;
        }
        if (n == 11 || n == 31 || n == 51 || n == 71) {
            return R.color.color12;
        }
        if (n == 12 || n == 32 || n == 52 || n == 72) {
            return R.color.color13;
        }
        if (n == 13 || n == 33 || n == 53 || n == 73) {
            return R.color.color14;
        }
        if (n == 14 || n == 34 || n == 54 || n == 74) {
            return R.color.color15;
        }
        if (n == 15 || n == 35 || n == 55 || n == 75) {
            return R.color.color16;
        }
        if (n == 16 || n == 36 || n == 56 || n == 76) {
            return R.color.color17;
        }
        if (n == 17 || n == 37 || n == 57 || n == 77) {
            return R.color.color18;
        }
        if (n == 18 || n == 38 || n == 58 || n == 78) {
            return R.color.color19;
        }
        if (n == 19 || n == 39 || n == 59 || n == 79) {
            return R.color.color20;
        }
        return R.color.color1;
    }
}
