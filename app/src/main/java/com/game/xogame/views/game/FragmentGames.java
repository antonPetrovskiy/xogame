package com.game.xogame.views.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.game.xogame.R;
import com.game.xogame.adapter.GamesAdapter;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Game;
import com.game.xogame.presenters.MainPresenter;
import com.game.xogame.views.main.MainActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentGames extends Fragment {

    private ApiService api;
    public static MainPresenter presenter;
    private LinearLayout load;
    private RelativeLayout empty;
    private ListView listView;
    private View rootView;
    private Button refresh;
    static private Context context;
    static GamesAdapter adapter;
    private SwipeRefreshLayout pullToRefresh;
    private List<Game> gameList;
    private List<Game> tmpgameList;
    private EditText search;
    private static FragmentGames fragment;

    public FragmentGames() {
    }

    public static FragmentGames newInstance(Context c, MainPresenter p) {
        if(fragment==null){
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
    public void init(){
        load = rootView.findViewById(R.id.targetView);
        listView = rootView.findViewById(R.id.gamelist);
        pullToRefresh = rootView.findViewById(R.id.swiperefresh);
        empty = rootView.findViewById(R.id.empty);
        refresh = rootView.findViewById(R.id.refresh);
        search = rootView.findViewById(R.id.search);
        api = RetroClient.getApiService();

        gameList = new LinkedList<>();
        adapter = null;

        if(!context.getSharedPreferences("myPref", MODE_PRIVATE).getString("lat", "null").equals("null")){
            presenter.showGames(FragmentGames.this);
        }else{
            empty.setVisibility(View.VISIBLE);
            load.setVisibility(View.GONE);
        }

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!context.getSharedPreferences("myPref", MODE_PRIVATE).getString("lat", "null").equals("null")){
                    gameList = new LinkedList<>();
                    adapter = null;
                    presenter.showGames(FragmentGames.this);
                    empty.setVisibility(View.GONE);
                }else{
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
                        ((MainActivity)getActivity()).coord();
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
                tmpgameList= new LinkedList<>();
//                Toast toast = Toast.makeText(rootView.getContext(),
//                        gameList.size()+"no", Toast.LENGTH_SHORT);
//                toast.show();
                for(int j = 0; j < gameList.size(); j ++){

                    if(gameList.get(j).getTitle().toLowerCase().contains(s.toString().toLowerCase())){

                        tmpgameList.add(gameList.get(j));
                    }
                }
                adapter = new GamesAdapter(context, tmpgameList);
                listView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

    }

    @Override
    public void onResume() {
        update();
        super.onResume();
    }

    public void update(){
        if(!context.getSharedPreferences("myPref", MODE_PRIVATE).getString("lat", "null").equals("null")){
            gameList = new LinkedList<>();
            adapter = null;
            presenter.showGames(FragmentGames.this);
            empty.setVisibility(View.GONE);
        }else{
            empty.setVisibility(View.VISIBLE);
            load.setVisibility(View.GONE);
        }

    }


    public void setList(List<Game> list){
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

        load.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, GameInfoActivity.class);
                intent.putExtra("GAMEID",tmpgameList.get(position).getGameid());
                intent.putExtra("SUBSCRIBE",tmpgameList.get(position).getSubscribe());
                intent.putExtra("TITLE", tmpgameList.get(position).getTitle());
                intent.putExtra("NAME", tmpgameList.get(position).getCompany());
                intent.putExtra("LOGO", tmpgameList.get(position).getLogo());
                intent.putExtra("BACKGROUND", tmpgameList.get(position).getBackground());
                intent.putExtra("DATE", tmpgameList.get(position).getStartdate()+"-"+l.get(position).getEnddate());
                intent.putExtra("DESCRIPTION", tmpgameList.get(position).getDescription());
                intent.putExtra("TASKS", tmpgameList.get(position).getTasks());
                intent.putExtra("TIME", tmpgameList.get(position).getStarttime()+" - "+l.get(position).getEndtime());
                intent.putExtra("MONEY", tmpgameList.get(position).getReward());
                intent.putExtra("PEOPLE", tmpgameList.get(position).getFollowers());
                intent.putExtra("STATISTIC", "false");
                startActivity(intent);
            }
        });
    }




}
