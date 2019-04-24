package com.game.xogame.views.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.game.xogame.R;
import com.game.xogame.adapter.GamesAdapter;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Game;
import com.game.xogame.presenters.MainPresenter;
import com.game.xogame.views.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentGames extends Fragment {

    private ApiService api;
    private static MainPresenter presenter;
    private LinearLayout load;
    private ListView listView;
    private View rootView;
    static private Context context;
    static GamesAdapter adapter;
    private SwipeRefreshLayout pullToRefresh;

    public FragmentGames() {
    }

    public static FragmentGames newInstance(Context c, MainPresenter p) {
        FragmentGames fragment = new FragmentGames();
        context = c;
        presenter = p;
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_games, container, false);
        rootView = inflater.inflate(R.layout.fragment_games, container, false);

        init();
        presenter.showGames(this);

        return rootView;
    }

    public void init(){
        load = rootView.findViewById(R.id.targetView);
        listView = rootView.findViewById(R.id.gamelist);
        pullToRefresh = rootView.findViewById(R.id.swiperefresh);
        api = RetroClient.getApiService();
        final List<Game> list = new ArrayList<>() ;
        adapter = new GamesAdapter(context,list);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                presenter.showGames(FragmentGames.this);
                adapter.notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });

    }



    public void setList(List<Game> list){
        final List<Game> l = list;
        adapter = new GamesAdapter(context, list);
        listView.setAdapter(adapter);
        load.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, GameInfoActivity.class);
                intent.putExtra("GAMEID",l.get(position).getGameid());
                intent.putExtra("SUBSCRIBE",l.get(position).getSubscribe());
                intent.putExtra("TITLE", l.get(position).getTitle());
                intent.putExtra("LOGO", l.get(position).getLogo());
                intent.putExtra("BACKGROUND", l.get(position).getBackground());
                intent.putExtra("DATE", l.get(position).getStartdate()+"-"+l.get(position).getEnddate());
                intent.putExtra("DESCRIPTION", l.get(position).getDescription());
                intent.putExtra("TASKS", l.get(position).getTasks());
                intent.putExtra("TIME", l.get(position).getStarttime()+"-"+l.get(position).getEndtime());
                intent.putExtra("MONEY", l.get(position).getReward());
                intent.putExtra("PEOPLE", l.get(position).getFollowers());
                startActivity(intent);
            }
        });
    }




}
