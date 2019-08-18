package com.game.xogame.views.create;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.game.xogame.R;
import com.game.xogame.adapter.FeedsAdapter;
import com.game.xogame.adapter.FeedsAdapterMy;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Feed;
import com.game.xogame.models.CreateGameModel;
import com.game.xogame.presenters.CreatedFeedsPresenter;
import com.game.xogame.presenters.MainPresenter;
import com.game.xogame.views.game.FragmentFeeds;

import java.util.LinkedList;
import java.util.List;

public class CreatedFeedsActivity extends AppCompatActivity {

    static FeedsAdapterMy adapter;
    private static CreatedFeedsPresenter presenter;
    public ApiService api;
    public LinearLayout load;
    private TextView error;
    private ListView listView;
    private TextView name;
    private ImageView back;
    private ScrollView pullToRefresh;
    private String flag = "false";
    private RelativeLayout empty;
    private List<Feed> listFeeds = new LinkedList<>();


    private LinearLayout info;
    private ImageView bg;
    private TextView date;
    private TextView tasks;
    private TextView paparazzi;
    private TextView time;
    private TextView money;
    private TextView category;
    private TextView description;
    private TextView street;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_feeds);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        api = RetroClient.getApiService();
        CreateGameModel modelGames = new CreateGameModel(api, getApplicationContext());
        presenter = new CreatedFeedsPresenter(modelGames);
        presenter.attachView(this);
        init();
        listFeeds = new LinkedList<>();
        adapter = null;
        presenter.showFeeds(getIntent().getStringExtra("gameid"));

    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        load = findViewById(R.id.targetView);
        listView = findViewById(R.id.gamelist);
        empty = findViewById(R.id.empty);
        error = findViewById(R.id.error);
        back = findViewById(R.id.imageView1);
        name = findViewById(R.id.textView1);
        pullToRefresh = findViewById(R.id.swiperefresh);
        name.setText(getIntent().getStringExtra("name")+"");

        bg = findViewById(R.id.imageView4);
        date = findViewById(R.id.textView28);
        tasks = findViewById(R.id.textView29);
        paparazzi = findViewById(R.id.textView31);
        time = findViewById(R.id.textView32);
        money = findViewById(R.id.textView33);
        category = findViewById(R.id.textView34);
        description = findViewById(R.id.textView30);
        street = findViewById(R.id.textView303);
        info = findViewById(R.id.linearLayout6);

        if(getIntent().getStringExtra("type")!=null && getIntent().getStringExtra("type").equals("ended")){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.unknow_wide);
            requestOptions.error(R.drawable.unknow_wide);
            requestOptions.centerCrop();
            Glide.with(this).setDefaultRequestOptions(requestOptions).load(getIntent().getStringExtra("photo")+"").thumbnail(0.3f).into(bg);
            date.setText(getIntent().getStringExtra("date"));
            tasks.setText(getIntent().getStringExtra("tasks") + " " + getString(R.string.activityGameInfo_tasks));
            paparazzi.setText(getIntent().getStringExtra("people") + " " + getString(R.string.activityGameInfo_people));
            time.setText(getIntent().getStringExtra("time"));
            money.setText(getIntent().getStringExtra("money") + " $");
            category.setText(getIntent().getStringExtra("category"));
            description.setText(getIntent().getStringExtra("description"));
            street.setText(getIntent().getStringExtra("street"));

        }else{
            info.setVisibility(View.GONE);
        }

        api = RetroClient.getApiService();

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        back.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        load.setVisibility(View.VISIBLE);
                        onBackPressed();
                        break;
                }
                return true;
            }
        });


//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//                listFeeds = new LinkedList<>();
//                adapter = null;
//                presenter.showFeeds(getIntent().getStringExtra("gameid"));
//                pullToRefresh.setRefreshing(false);
//            }
//        });



    }



    public void setList(final List<Feed> list) {
        if (list.size() > 0)
            //Log.i("LOG_scroll", list.get(list.size() - 1).getFeedId() + "");
        listFeeds.addAll(list);
        if (adapter == null) {
            adapter = new FeedsAdapterMy(this, listFeeds);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(listView);
        } else {
            adapter.notifyDataSetChanged();
        }
        load.setVisibility(View.GONE);
        if (listFeeds.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            pullToRefresh.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            if(getIntent().getStringExtra("type")!=null && getIntent().getStringExtra("type").equals("ended")){
                info.setVisibility(View.VISIBLE);
            }else{
                info.setVisibility(View.GONE);
            }
        }

    }

    public void setError(String msg){
        info.setVisibility(View.GONE);
        load.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        pullToRefresh.setVisibility(View.GONE);
        error.setText(msg);
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

}
