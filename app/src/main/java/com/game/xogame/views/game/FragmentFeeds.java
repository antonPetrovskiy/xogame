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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.game.xogame.R;
import com.game.xogame.adapter.FeedsAdapter;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Feed;
import com.game.xogame.entity.Game;
import com.game.xogame.presenters.MainPresenter;
import com.game.xogame.views.main.MainActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class FragmentFeeds extends Fragment {

    private ApiService api;
    private static MainPresenter presenter;
    private LinearLayout load;
    private ListView listView;
    private View rootView;
    static private Context context;
    static FeedsAdapter adapter;
    private SwipeRefreshLayout pullToRefresh;

    private RelativeLayout empty;
    private Button find;

    public FragmentFeeds() {

    }

    public static FragmentFeeds newInstance(Context c, MainPresenter p) {
        FragmentFeeds fragment = new FragmentFeeds();
        context = c;
        presenter = p;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feeds, container, false);

        init();
        presenter.showFeeds(this);

        return rootView;
    }

    public void init(){
        load = rootView.findViewById(R.id.targetView);
        listView = rootView.findViewById(R.id.gamelist);
        empty = rootView.findViewById(R.id.empty);
        find = rootView.findViewById(R.id.imageButton);
        pullToRefresh = rootView.findViewById(R.id.swiperefresh);
        api = RetroClient.getApiService();
        final List<Feed> list = new LinkedList<>();
        adapter = new FeedsAdapter(context,list);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mViewPager.setCurrentItem(0);
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                presenter.showFeeds(FragmentFeeds.this);
                adapter.notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });

    }

    public void setList(List<Feed> list){
        adapter = new FeedsAdapter(context, list);
        listView.setAdapter(adapter);
        load.setVisibility(View.GONE);
        if(list.size()==0) {
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }
    }

}
