package com.game.xogame.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.entity.Game;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;


public class WinsAdapter extends ArrayAdapter<Game> {
    private static List<Game> winGameList;
    private Context context;
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final WinsAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_mywins, parent, false);
            vh = WinsAdapter.ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (WinsAdapter.ViewHolder) convertView.getTag();
        }

        final Game item = getItem(position);
        Log.i("LOG_adapterWin" , Objects.requireNonNull(item).getTitle()+" "+item.getRewardstatus());

        vh.textViewName1.setText(item.getTitle());
        vh.textViewName2.setText(item.getReward()+" ₴");
        vh.textViewTasks.setText(item.getTitle()+"");
        vh.textViewDate.setText(item.getEnddate()+"");
        vh.rootView.setClipToOutline(true);
        if(item.getRewardstatus().equals("0")){
            vh.textViewModeration.setVisibility(View.VISIBLE);
            vh.textViewName2.setAlpha(0.5f);
        }else if (item.getRewardstatus().equals("1")){
            vh.textViewModeration.setVisibility(View.GONE);
            vh.textViewName2.setAlpha(1f);
        }
        Picasso.with(context).load(item.getLogo()+"").placeholder(R.drawable.unknow).error(R.drawable.unknow).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        private final RelativeLayout rootView;
        private final RelativeLayout botView;
        public final ImageView imageView;
        private final TextView textViewName1;
        private final TextView textViewName2;
        private final TextView textViewTasks;
        private final TextView textViewDate;
        private final TextView textViewModeration;

        private ViewHolder(RelativeLayout rootView, RelativeLayout botView, ImageView imageView, TextView textViewName1, TextView textViewName2, TextView textViewTasks, TextView textViewDate, TextView textViewModeration) {
            this.rootView = rootView;
            this.botView = botView;
            this.imageView = imageView;
            this.textViewName1 = textViewName1;
            this.textViewName2 = textViewName2;
            this.textViewTasks = textViewTasks;
            this.textViewDate = textViewDate;
            this.textViewModeration = textViewModeration;
        }

        public static WinsAdapter.ViewHolder create(RelativeLayout rootView) {
            RelativeLayout botView = rootView.findViewById(R.id.bot);
            ImageView imageView = rootView.findViewById(R.id.imageView);
            TextView textViewName1 = rootView.findViewById(R.id.name1);
            TextView textViewName2 = rootView.findViewById(R.id.name2);
            TextView textViewTasks = rootView.findViewById(R.id.name3);
            TextView textViewDate = rootView.findViewById(R.id.name4);
            TextView textViewModeration = rootView.findViewById(R.id.name5);
            return new WinsAdapter.ViewHolder(rootView, botView, imageView, textViewName1, textViewName2, textViewTasks, textViewDate, textViewModeration);
        }

    }
}

