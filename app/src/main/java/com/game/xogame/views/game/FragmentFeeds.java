package com.game.xogame.views.game;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
    private ImageView sort;
    private ImageView rating;
    private View rootView;
    static private Context context;
    static FeedsAdapter adapter;
    private SwipeRefreshLayout pullToRefresh;
    private String flag = "false";
    private int preLast;
    private RelativeLayout empty;
    private Button find;
    private List<Feed> listFeeds = new LinkedList<>();

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
        listFeeds = new LinkedList<>();
        adapter = null;
        presenter.showFeeds(this,flag,"0");

        return rootView;
    }

    public void init(){
        load = rootView.findViewById(R.id.targetView);
        listView = rootView.findViewById(R.id.gamelist);
        empty = rootView.findViewById(R.id.empty);
        find = rootView.findViewById(R.id.imageButton);
        sort = rootView.findViewById(R.id.imageView1);
        rating = rootView.findViewById(R.id.imageView2);
        pullToRefresh = rootView.findViewById(R.id.swiperefresh);
        api = RetroClient.getApiService();


        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RatingActivity.class);
                startActivity(intent);
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag.equals("false")){
                    flag = "true";
                    sort.setImageResource(R.drawable.feed_type_used);
                    listFeeds = new LinkedList<>();
                    adapter = null;
                    presenter.showFeeds(FragmentFeeds.this,flag,"0");
                }else{
                    flag = "false";
                    sort.setImageResource(R.drawable.feed_type);
                    listFeeds = new LinkedList<>();
                    adapter = null;
                    presenter.showFeeds(FragmentFeeds.this,flag,"0");
                }
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mViewPager.setCurrentItem(0);
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                listFeeds = new LinkedList<>();
                adapter = null;
                presenter.showFeeds(FragmentFeeds.this,flag,"0");
                pullToRefresh.setRefreshing(false);
            }
        });

    }



    public void setList(final List<Feed> list){


        if(list.size()>0)
            Log.i("LOG_scroll" , list.get(list.size()-1).getFeedId()+"");
        listFeeds.addAll(list);
        if (adapter == null) {
            adapter = new FeedsAdapter(context, listFeeds);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        load.setVisibility(View.GONE);
        if(listFeeds.size()==0) {
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount)
                {
                    //Log.i("LOG_scroll" , "not last  "+list.get(list.size()-1).getFeedId());
                    if(preLast!=lastItem)
                    {
                        Log.i("LOG_scroll" , "last");
                        //to avoid multiple calls for last item
                        if(list.size()>0)
                            presenter.showFeeds(FragmentFeeds.this,flag, (Integer.parseInt(list.get(list.size()-1).getFeedId())-1)+"");
                        preLast = lastItem;
                    }
                }
            }
        });
    }

}
