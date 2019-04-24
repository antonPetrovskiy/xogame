package com.game.xogame.adapter;


import android.content.Context;
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


public class WinsAdapter extends ArrayAdapter<Game> {
    public static List<Game> winGameList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public WinsAdapter(Context context, List<Game> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        winGameList = objects;
    }

    @Override
    public Game getItem(int position) {
        return winGameList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final WinsAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_mywins, parent, false);
            vh = WinsAdapter.ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (WinsAdapter.ViewHolder) convertView.getTag();
        }

        final Game item = getItem(position);
        Log.i("LOG_adapterWin" , item.getTitle()+" "+item.getLogo());

        vh.textViewName1.setText(item.getTitle());
        vh.textViewName2.setText(item.getReward()+" ₴");
        vh.textViewTasks.setText(item.getEnddate());
        vh.textViewDate.setText("1/"+item.getFollowers());
        vh.rootView.setClipToOutline(true);
        Picasso.with(context).load(item.getLogo()+"").placeholder(R.drawable.unknow).error(R.drawable.unknow).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final ImageView imageView;
        public final TextView textViewName1;
        public final TextView textViewName2;
        public final TextView textViewTasks;
        public final TextView textViewDate;

        private ViewHolder(LinearLayout rootView, ImageView imageView, TextView textViewName1, TextView textViewName2, TextView textViewTasks, TextView textViewDate) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewName1 = textViewName1;
            this.textViewName2 = textViewName2;
            this.textViewTasks = textViewTasks;
            this.textViewDate = textViewDate;
        }

        public static WinsAdapter.ViewHolder create(LinearLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewName1 = (TextView) rootView.findViewById(R.id.name1);
            TextView textViewName2 = (TextView) rootView.findViewById(R.id.name2);
            TextView textViewTasks = (TextView) rootView.findViewById(R.id.name3);
            TextView textViewDate = (TextView) rootView.findViewById(R.id.name4);
            return new WinsAdapter.ViewHolder(rootView, imageView, textViewName1, textViewName2, textViewTasks, textViewDate);
        }

    }
}

