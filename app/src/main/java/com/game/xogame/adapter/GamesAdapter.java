package com.game.xogame.adapter;


import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.entity.Game;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GamesAdapter  extends ArrayAdapter<Game> {
    public List<Game> gameList;
    Context context;
    private LayoutInflater mInflater;
    private LinearLayout lay;

    // Constructors
    public GamesAdapter(Context context, List<Game> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        gameList = objects;
    }

    @Override
    public Game getItem(int position) {
        return gameList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final GamesAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_games, parent, false);
            vh = GamesAdapter.ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);

        } else {
            vh = (GamesAdapter.ViewHolder) convertView.getTag();
        }

        final Game item = getItem(position);
        Log.i("LOG_allgames" , item.getBackground()+" 0123");

        vh.rootView.setClipToOutline(true);
        vh.textViewName1.setText(item.getCompany());
        vh.textViewName2.setText(item.getTitle());
        vh.textViewDate.setText(item.getStartdate()+" - "+item.getEnddate()+"   "+item.getStarttime()+" - "+item.getEndtime());
        vh.textViewTasks.setText(item.getTasks()+" заданий");
        vh.textViewPrize.setText(item.getReward()+" ₴");
        vh.textViewPlay.setText(item.getFollowers()+" участников");

        if(item.getSubscribe()==null || item.getSubscribe().equals("0")){
            vh.imageView2.setVisibility(View.GONE);
        }else{
            vh.imageView2.setVisibility(View.VISIBLE);
        }
        Picasso.with(context).load(item.getLogo()+"").placeholder(R.drawable.unknow).error(R.drawable.unknow).into(vh.imageView);
        if(!item.getBackground().equals("")){
            Picasso.with(context).load(item.getBackground()+"").placeholder(R.drawable.unknow_wide).error(R.drawable.unknow_wide).into(vh.imageView1);
        }


//            new CountDownTimer(60000, 1000) {
//
//                public void onTick(long millisUntilFinished) {
//                    if(item.getReward().equals("666"))
//                        vh.textViewDate.setText("02:23:"+(millisUntilFinished / 1000)+"");
//                    if(item.getReward().equals("1"))
//                        vh.textViewDate.setText("19:05:"+(millisUntilFinished / 1000)+"");
//                    if(item.getReward().equals("апап"))
//                        vh.textViewDate.setText("02:23:"+(millisUntilFinished / 1000)+"");
//                    if(item.getReward().equals("2000"))
//                        vh.textViewDate.setText("11:44:"+(millisUntilFinished / 1000)+"");
//                }
//
//                public void onFinish() {
//                }
//            }.start();

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView imageView;
        public final ImageView imageView1;
        public final ImageView imageView2;
        public final TextView textViewName1;
        public final TextView textViewName2;
        public final TextView textViewTasks;
        public final TextView textViewDate;
        public final TextView textViewPlay;
        public final TextView textViewPrize;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, ImageView imageView1, ImageView imageView2, TextView textViewName1, TextView textViewName2, TextView textViewTasks, TextView textViewDate, TextView textViewPrize, TextView textViewPlay) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.imageView1 = imageView1;
            this.imageView2 = imageView2;
            this.textViewName1 = textViewName1;
            this.textViewName2 = textViewName2;
            this.textViewTasks = textViewTasks;
            this.textViewDate = textViewDate;
            this.textViewPlay = textViewPlay;
            this.textViewPrize = textViewPrize;
        }

        public static GamesAdapter.ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            ImageView imageView1 = (ImageView) rootView.findViewById(R.id.imageView1);
            ImageView imageView2 = (ImageView) rootView.findViewById(R.id.imageView2);
            TextView textViewName1 = (TextView) rootView.findViewById(R.id.name1);
            TextView textViewName2 = (TextView) rootView.findViewById(R.id.name2);
            TextView textViewTasks = (TextView) rootView.findViewById(R.id.name4);
            TextView textViewDate = (TextView) rootView.findViewById(R.id.name3);
            TextView textViewPrize = (TextView) rootView.findViewById(R.id.name5);
            TextView textViewPlay = (TextView) rootView.findViewById(R.id.name6);
            return new GamesAdapter.ViewHolder(rootView, imageView, imageView1, imageView2, textViewName1, textViewName2, textViewTasks, textViewDate, textViewPrize, textViewPlay);
        }

    }
}

