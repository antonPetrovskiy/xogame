package com.game.xogame.views.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.game.xogame.presenters.MainPresenter;
import com.game.xogame.views.create.CreateGameActivity;
import com.game.xogame.views.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentGames extends Fragment {

    public static MainPresenter presenter;
    @SuppressLint("StaticFieldLeak")
    static GamesAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    static private Context context;
    @SuppressLint("StaticFieldLeak")
    private static FragmentGames fragment;
    public ApiService api;
    private LinearLayout load;
    private TextView error;
    private static RelativeLayout empty;
    private ListView listView;
    private View rootView;
    private Button refresh;
    private SwipeRefreshLayout pullToRefresh;
    public static List<Game> gameList;
    public static List<Game> tmpgameList;
    public EditText search;
    private ImageView searchIcon;
    private ImageView exitIcon;
    private boolean subscribeCheck = false;
    private RelativeLayout logoView;
    private RelativeLayout searchView;
    private LinearLayout categories;

    private RelativeLayout tutorialView;
    private TextView tutorialText;
    private Button tutorialButton;

    private TextView create;
    private TextView all;
    private TextView category_auto;
    private TextView category_sport;
    private TextView category_food;
    private TextView category_travel;
    private TextView category_humor;
    private TextView category_tv;
    private TextView category_beauty;
    private TextView category_fashion;
    private TextView category_decor;
    private TextView category_iscustvo;
    private TextView category_art;
    private TextView category_style;
    private TextView category_myday;
    private TextView category_other;


    public FragmentGames() {
    }

    public static FragmentGames newInstance(Context c, MainPresenter p) {
        if (fragment == null) {
            fragment = new FragmentGames();
        }
        context = c;
        presenter = p;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_games, container, false);
        rootView = inflater.inflate(R.layout.fragment_games, container, false);

        init();



        return rootView;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        load = rootView.findViewById(R.id.targetView);
        listView = rootView.findViewById(R.id.gamelist);
        pullToRefresh = rootView.findViewById(R.id.swiperefresh);
        empty = rootView.findViewById(R.id.empty);
        refresh = rootView.findViewById(R.id.refresh);
        search = rootView.findViewById(R.id.search);
        searchIcon = rootView.findViewById(R.id.imageView1);
        exitIcon = rootView.findViewById(R.id.imageView111);
        error = rootView.findViewById(R.id.error);
        logoView = rootView.findViewById(R.id.linearLayout);
        searchView = rootView.findViewById(R.id.linearLayout7);

        tutorialView = rootView.findViewById(R.id.tutorial);
        tutorialText = rootView.findViewById(R.id.tutorial_text);
        tutorialButton = rootView.findViewById(R.id.tutorial_button);
        categories = rootView.findViewById(R.id.categories);
        api = RetroClient.getApiService();

        gameList = new LinkedList<>();
        adapter = null;

        if (context!=null && !context.getSharedPreferences("myPref", MODE_PRIVATE).getString("lat", "null").equals("null")) {
            Log.i("LOG_gethistory" , "1.1 " );
            presenter.showGames(FragmentGames.this);
        } else {
            Log.i("LOG_gethistory" , "1.2 " );
            empty.setVisibility(View.VISIBLE);
            load.setVisibility(View.GONE);
        }

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, //fromXType
                        0.0f,                       //fromXValue
                        Animation.RELATIVE_TO_SELF, //toXType
                        -1.0f,                      //toXValue
                        Animation.RELATIVE_TO_SELF, //fromYType
                        0.0f,                       //fromYValue
                        Animation.RELATIVE_TO_SELF, //toYType
                        0.0f);
                //animation.setFillAfter(true);
                animation.setDuration(300);
                logoView.startAnimation(animation);


                Animation animation1 = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, //fromXType
                        1.0f,                       //fromXValue
                        Animation.RELATIVE_TO_SELF, //toXType
                        0.0f,                      //toXValue
                        Animation.RELATIVE_TO_SELF, //fromYType
                        0.0f,                       //fromYValue
                        Animation.RELATIVE_TO_SELF, //toYType
                        0.0f);
                //animation1.setFillAfter(true);
                animation1.setDuration(300);
                searchView.startAnimation(animation1);

                searchIcon.setEnabled(false);
                exitIcon.setEnabled(true);
                searchView.setVisibility(View.VISIBLE);
                logoView.setVisibility(View.GONE);
            }
        });

        exitIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, //fromXType
                        0.0f,                       //fromXValue
                        Animation.RELATIVE_TO_SELF, //toXType
                        1.0f,                      //toXValue
                        Animation.RELATIVE_TO_SELF, //fromYType
                        0.0f,                       //fromYValue
                        Animation.RELATIVE_TO_SELF, //toYType
                        0.0f);
                //animation.setFillAfter(true);
                animation.setDuration(300);
                searchView.startAnimation(animation);


                Animation animation1 = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, //fromXType
                        -1.0f,                       //fromXValue
                        Animation.RELATIVE_TO_SELF, //toXType
                        0.0f,                      //toXValue
                        Animation.RELATIVE_TO_SELF, //fromYType
                        0.0f,                       //fromYValue
                        Animation.RELATIVE_TO_SELF, //toYType
                        0.0f);
                //animation1.setFillAfter(true);
                animation1.setDuration(300);
                logoView.startAnimation(animation1);

                searchIcon.setEnabled(true);
                exitIcon.setEnabled(false);
                searchView.setVisibility(View.GONE);
                logoView.setVisibility(View.VISIBLE);
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!context.getSharedPreferences("myPref", MODE_PRIVATE).getString("lat", "null").equals("null")) {
                    Log.i("LOG_gethistory" , "2.1 " );
                    gameList = new LinkedList<>();
                    adapter = null;
                    presenter.showGames(FragmentGames.this);
                    empty.setVisibility(View.GONE);
                } else {
                    Log.i("LOG_gethistory" , "2.2 " );
                    load.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }

                pullToRefresh.setRefreshing(false);
            }
        });


        refresh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        refresh.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        refresh.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        ((MainActivity) getActivity()).coord();
                        update();
                        break;
                }
                return true;
            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tmpgameList = new LinkedList<>();
//                Toast toast = Toast.makeText(rootView.getContext(),
//                        gameList.size()+"no", Toast.LENGTH_SHORT);
//                toast.show();
                for (int j = 0; j < gameList.size(); j++) {

                    if (gameList.get(j).getTitle().toLowerCase().contains(s.toString().toLowerCase())) {

                        tmpgameList.add(gameList.get(j));
                    }
                }
                try{
                    adapter = new GamesAdapter(context, tmpgameList);
                    listView.setAdapter(adapter);
                }catch (NullPointerException e){

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                search.setFocusableInTouchMode(true);

                return false;
            }
        });

        create = rootView.findViewById(R.id.textView1);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateGameActivity.class);
                startActivity(intent);
            }
        });

        all = rootView.findViewById(R.id.textView2);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","all");
                startActivity(intent);
            }
        });
        category_auto = rootView.findViewById(R.id.textView3);
        category_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","auto");
                startActivity(intent);
            }
        });
        category_sport = rootView.findViewById(R.id.textView4);
        category_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","sport");
                startActivity(intent);
            }
        });
        category_food = rootView.findViewById(R.id.textView5);
        category_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","food");
                startActivity(intent);
            }
        });
        category_travel = rootView.findViewById(R.id.textView6);
        category_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","travel");
                startActivity(intent);
            }
        });
        category_humor = rootView.findViewById(R.id.textView7);
        category_humor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","fun");
                startActivity(intent);
            }
        });
        category_tv = rootView.findViewById(R.id.textView8);
        category_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","tv");
                startActivity(intent);
            }
        });
        category_beauty = rootView.findViewById(R.id.textView9);
        category_beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","beauty");
                startActivity(intent);
            }
        });
        category_fashion = rootView.findViewById(R.id.textView99);
        category_fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","fashion");
                startActivity(intent);
            }
        });
        category_decor = rootView.findViewById(R.id.textView10);
        category_decor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","decor");
                startActivity(intent);
            }
        });
        category_iscustvo = rootView.findViewById(R.id.textView11);
        category_iscustvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","iscustvo");
                startActivity(intent);
            }
        });
        category_art = rootView.findViewById(R.id.textView12);
        category_art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","art");
                startActivity(intent);
            }
        });
        category_style = rootView.findViewById(R.id.textView13);
        category_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","style");
                startActivity(intent);
            }
        });
        category_myday = rootView.findViewById(R.id.textView14);
        category_myday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","myday");
                startActivity(intent);
            }
        });
        category_other = rootView.findViewById(R.id.textView15);
        category_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category","other");
                startActivity(intent);
            }
        });

        checkTutorial();
    }

    @Override
    public void onResume() {
        update();
        super.onResume();
    }

    public void update() {
         if (!context.getSharedPreferences("myPref", MODE_PRIVATE).getString("lat", "null").equals("null")) {
            gameList = new LinkedList<>();
            adapter = null;
            presenter.showGames(FragmentGames.this);
            empty.setVisibility(View.GONE);

        } else {
             if(empty!=null)
                empty.setVisibility(View.VISIBLE);
             if(load!=null)
                load.setVisibility(View.GONE);
        }

    }

    public void updateSubscribe(){
        for(final Game item : gameList){
            if(!item.getSubscribe()){
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/agame" + item.getGameid())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.i("LOG_subscribe" , item.getGameid()+" +");
                            }
                        });
                SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);

            }else{
                FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/agame" + item.getGameid())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.i("LOG_subscribe" , item.getGameid()+" -");
                            }
                        });
            }
        }
    }

    public void setList(List<Game> list) {
        final List<Game> l = list;
        tmpgameList = list;
        gameList = list;
        if (adapter == null) {
            adapter = new GamesAdapter(context, gameList);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }


        all.setVisibility(View.GONE);
        category_auto.setVisibility(View.GONE);
        category_sport.setVisibility(View.GONE);
        category_food.setVisibility(View.GONE);
        category_travel.setVisibility(View.GONE);
        category_humor.setVisibility(View.GONE);
        category_tv.setVisibility(View.GONE);
        category_beauty.setVisibility(View.GONE);
        category_fashion.setVisibility(View.GONE);
        category_decor.setVisibility(View.GONE);
        category_iscustvo.setVisibility(View.GONE);
        category_art.setVisibility(View.GONE);
        category_style.setVisibility(View.GONE);
        category_myday.setVisibility(View.GONE);
        category_other.setVisibility(View.GONE);
        if(list == null || list.size()==0){
            categories.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.GONE);
            error.setText(getString(R.string.fragmentGames_nogames));
        }else{
            all.setVisibility(View.VISIBLE);
            for(int i = 0; i < list.size(); i ++){
                switch (list.get(i).getCategoryId()) {
                    case "0":
                        category_auto.setVisibility(View.VISIBLE);
                        break;
                    case "1":
                        category_sport.setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        category_food.setVisibility(View.VISIBLE);
                        break;
                    case "3":
                        category_travel.setVisibility(View.VISIBLE);
                        break;
                    case "4":
                        category_humor.setVisibility(View.VISIBLE);
                        break;
                    case "5":
                        category_tv.setVisibility(View.VISIBLE);
                        break;
                    case "6":
                        category_beauty.setVisibility(View.VISIBLE);
                        break;
                    case "7":
                        category_fashion.setVisibility(View.VISIBLE);
                        break;
                    case "8":
                        category_decor.setVisibility(View.VISIBLE);
                        break;
                    case "9":
                        category_iscustvo.setVisibility(View.VISIBLE);
                        break;
                    case "10":
                        category_art.setVisibility(View.VISIBLE);
                        break;
                    case "11":
                        category_style.setVisibility(View.VISIBLE);
                        break;
                    case "12":
                        category_myday.setVisibility(View.VISIBLE);
                        break;
                    case "13":
                        category_other.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        categories.setVisibility(View.VISIBLE);
        load.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, GameInfoActivity.class);
                intent.putExtra("GAMEID", tmpgameList.get(position).getGameid());
                intent.putExtra("SUBSCRIBE", tmpgameList.get(position).getSubscribe());
                intent.putExtra("TITLE", tmpgameList.get(position).getTitle());
                intent.putExtra("NAME", tmpgameList.get(position).getCompany());
                intent.putExtra("LOGO", tmpgameList.get(position).getLogo());
                intent.putExtra("BACKGROUND", tmpgameList.get(position).getBackground());
                if(tmpgameList.get(position).getStartdate().equals(l.get(position).getEnddate())){
                    intent.putExtra("DATE", tmpgameList.get(position).getStartdate());
                }else{
                    intent.putExtra("DATE", tmpgameList.get(position).getStartdate() + " - " + l.get(position).getEnddate());
                }
                intent.putExtra("DESCRIPTION", tmpgameList.get(position).getDescription());
                intent.putExtra("TASKS", tmpgameList.get(position).getTasks());
                intent.putExtra("TIME", tmpgameList.get(position).getStarttime() + " - " + l.get(position).getEndtime());
                intent.putExtra("MONEY", tmpgameList.get(position).getReward());
                intent.putExtra("PEOPLE", tmpgameList.get(position).getFollowers());
                intent.putExtra("SHARE", tmpgameList.get(position).getSiteurl());
                intent.putExtra("ADDRESS", tmpgameList.get(position).getAddress());
                intent.putExtra("CATEGORY", tmpgameList.get(position).getCategory());
                intent.putExtra("CATEGORYID", tmpgameList.get(position).getCategoryId());
                intent.putExtra("OWNER", tmpgameList.get(position).isOwner()+"");
                intent.putExtra("STATISTIC", "false");
                startActivity(intent);
            }
        });



        if(!subscribeCheck) {
            updateSubscribe();
            subscribeCheck = true;
        }
        Log.i("LOG_branch", "4 "+MainActivity.gameId);
        if(MainActivity.gameId!=null){
            for(int i = 0; i < tmpgameList.size(); i++){
                if(tmpgameList.get(i).getGameid().equals(MainActivity.gameId)){
                    Intent intent = new Intent(context, GameInfoActivity.class);
                    intent.putExtra("GAMEID", tmpgameList.get(i).getGameid());
                    intent.putExtra("SUBSCRIBE", tmpgameList.get(i).getSubscribe());
                    intent.putExtra("TITLE", tmpgameList.get(i).getTitle());
                    intent.putExtra("NAME", tmpgameList.get(i).getCompany());
                    intent.putExtra("LOGO", tmpgameList.get(i).getLogo());
                    intent.putExtra("BACKGROUND", tmpgameList.get(i).getBackground());
                    if(tmpgameList.get(i).getStartdate().equals(l.get(i).getEnddate())){
                        intent.putExtra("DATE", tmpgameList.get(i).getStartdate());
                    }else{
                        intent.putExtra("DATE", tmpgameList.get(i).getStartdate() + " - " + l.get(i).getEnddate());
                    }
                    intent.putExtra("DESCRIPTION", tmpgameList.get(i).getDescription());
                    intent.putExtra("TASKS", tmpgameList.get(i).getTasks());
                    intent.putExtra("TIME", tmpgameList.get(i).getStarttime() + " - " + l.get(i).getEndtime());
                    intent.putExtra("MONEY", tmpgameList.get(i).getReward());
                    intent.putExtra("PEOPLE", tmpgameList.get(i).getFollowers());
                    intent.putExtra("SHARE", tmpgameList.get(i).getSiteurl());
                    intent.putExtra("ADDRESS", tmpgameList.get(i).getAddress());
                    intent.putExtra("CATEGORY", tmpgameList.get(i).getCategory());
                    intent.putExtra("CATEGORYID", tmpgameList.get(i).getCategoryId());
                    intent.putExtra("STATISTIC", "false");
                    intent.putExtra("OWNER", tmpgameList.get(i).isOwner()+"");
                    MainActivity.gameId=null;
                    startActivity(intent);
                }
            }
        }


    }

    public void checkTutorial(){
        if(context!=null) {
            final SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            if (sharedPref.getString("tutorial_games", "false").equals("true")) {
                tutorialView.setVisibility(View.VISIBLE);
                MainActivity.bar.setVisibility(View.GONE);
                tutorialButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tutorialText.getText().toString().equals(getString(R.string.tutorial_gamelist))) {
                            tutorialView.setVisibility(View.GONE);
                            MainActivity.bar.setVisibility(View.VISIBLE);
                            sharedPref.edit().putString("tutorial_games", "false").commit();
                        } else {
                            tutorialText.setText(getString(R.string.tutorial_gamelist));
                        }
                    }
                });
                tutorialView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tutorialButton.performClick();
                    }
                });
            }
        }
    }

    public void setError(String msg){
        load.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        categories.setVisibility(View.GONE);
        error.setText(msg);
        refresh.setVisibility(View.GONE);
    }

}
