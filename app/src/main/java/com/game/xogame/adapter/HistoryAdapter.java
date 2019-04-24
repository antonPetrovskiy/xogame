package com.game.xogame.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.entity.Game;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<Game> {
    public static List<Game> gameList;
    Context context;
    private LayoutInflater mInflater;
    private LinearLayout lay;

    // Constructors
    public HistoryAdapter(Context context, List<Game> objects) {
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
        final HistoryAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_games, parent, false);
            vh = HistoryAdapter.ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
            final Game item = getItem(position);
            vh.rootView.setClipToOutline(true);
            vh.textViewName1.setText(item.getTitle());
            vh.textViewName2.setText(item.getReward()+" ₴");
            vh.textViewTasks.setText(item.getStartdate());
            vh.textViewDate.setVisibility(View.GONE);
            vh.rootView.setBackgroundResource(R.drawable.mygame_bg);
            if(!item.getBackground().equals("")){
                Picasso.with(context).load(item.getBackground()+"").placeholder(R.drawable.unknow_wide).error(R.drawable.unknow_wide).into(vh.imageView1);
            }
            Picasso.with(context).load(item.getLogo()+"").placeholder(R.drawable.unknow).error(R.drawable.unknow).into(vh.imageView);
        } else {
            vh = (HistoryAdapter.ViewHolder) convertView.getTag();
        }



        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final ImageView imageView;
        public final ImageView imageView1;
        public final TextView textViewName1;
        public final TextView textViewName2;
        public final TextView textViewTasks;
        public final TextView textViewDate;

        private ViewHolder(LinearLayout rootView, ImageView imageView, ImageView imageView1, TextView textViewName1, TextView textViewName2, TextView textViewTasks, TextView textViewDate) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.imageView1 = imageView1;
            this.textViewName1 = textViewName1;
            this.textViewName2 = textViewName2;
            this.textViewTasks = textViewTasks;
            this.textViewDate = textViewDate;
        }

        public static HistoryAdapter.ViewHolder create(LinearLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            ImageView imageView1 = (ImageView) rootView.findViewById(R.id.imageView1);
            TextView textViewName1 = (TextView) rootView.findViewById(R.id.name1);
            TextView textViewName2 = (TextView) rootView.findViewById(R.id.name2);
            TextView textViewTasks = (TextView) rootView.findViewById(R.id.name3);
            TextView textViewDate = (TextView) rootView.findViewById(R.id.name4);
            return new HistoryAdapter.ViewHolder(rootView, imageView, imageView1, textViewName1, textViewName2, textViewTasks, textViewDate);
        }

    }
}

