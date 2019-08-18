package com.game.xogame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.game.xogame.R;
import com.game.xogame.entity.GameNew;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GamesNewAdapter extends ArrayAdapter<GameNew> {
    private List<GameNew> gameList;
    private Context context;
    private LayoutInflater mInflater;

    // Constructors
    public GamesNewAdapter(Context context, List<GameNew> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        gameList = objects;
    }

    @Override
    public GameNew getItem(int position) {
        return gameList.get(position);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final GamesNewAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_games, parent, false);
            vh = GamesNewAdapter.ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);

        } else {
            vh = (GamesNewAdapter.ViewHolder) convertView.getTag();
        }

        final GameNew item = getItem(position);
        assert item != null;
        //Log.i("LOG_allgames" , item.getBackground()+" 0123");

        vh.rootView.setClipToOutline(true);

        vh.textViewName1.setText(item.getCompany()+"");
        vh.textViewName2.setText(item.getName_game()+"");
        if(item.getStartdate().equals(item.getEnddate())){
            vh.textViewDate.setText(item.getStartdate()+"   "+item.getStart_task_time()+" - "+item.getEnd_task_time());
        }else{
            vh.textViewDate.setText(item.getStartdate()+" - "+item.getEnddate()+"   "+item.getStart_task_time()+" - "+item.getEnd_task_time());
        }
        vh.textViewTasks.setText(item.getTasksGame().size()+" "+context.getString(R.string.adapterGames_tasks));
        vh.textViewPrize.setText(item.getReward()+" $");
        vh.textViewPlay.setText(item.getFollowers()+" "+context.getString(R.string.adapterGames_participates));

        switch(item.getStatus()){
            case "Draft" :
                vh.textViewStatus.setVisibility(View.VISIBLE);
                vh.textViewStatus.setText(context.getString(R.string.status_draft));
                vh.textViewStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                vh.textViewDate.setVisibility(View.GONE);
                vh.textViewPlay.setVisibility(View.GONE);
                break;
            case "Moderation" :
                vh.textViewStatus.setVisibility(View.VISIBLE);
                vh.textViewStatus.setText(context.getString(R.string.status_moderation));
                vh.textViewStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9BB2C0")));
                vh.textViewDate.setVisibility(View.GONE);
                vh.textViewPlay.setVisibility(View.GONE);
                break;
            case "Active" :
                vh.textViewStatus.setVisibility(View.VISIBLE);
                vh.textViewStatus.setText(context.getString(R.string.status_active));
                vh.textViewStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00C088")));
                break;
            case "Canceled" :
                vh.textViewStatus.setVisibility(View.VISIBLE);
                vh.textViewStatus.setText(context.getString(R.string.status_canceled));
                vh.textViewStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6800")));
                vh.textViewDate.setVisibility(View.GONE);
                vh.textViewPlay.setVisibility(View.GONE);
                break;
            case "Ended" :
                vh.textViewStatus.setVisibility(View.VISIBLE);
                vh.textViewStatus.setText(context.getString(R.string.status_ended));
                vh.textViewStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9B800")));
                break;
            case "Date and time" :
                vh.textViewStatus.setVisibility(View.VISIBLE);
                vh.textViewStatus.setText(context.getString(R.string.status_date));
                vh.textViewStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2876BE")));
                vh.textViewDate.setVisibility(View.GONE);
                vh.textViewPlay.setVisibility(View.GONE);
                break;
            default :
                vh.textViewStatus.setVisibility(View.GONE);
                vh.textViewPlay.setVisibility(View.VISIBLE);
                vh.textViewDate.setVisibility(View.VISIBLE);
        }

        vh.textViewHolder.setText(item.getCompany().substring(0,1).toUpperCase()+"");
        vh.imageView.setImageResource(getPlaceholder(item.getCompany()+""));
        if(item.getSub()==null || item.getSub().equals("0")){
            vh.imageView2.setVisibility(View.GONE);
        }else{
            vh.imageView2.setVisibility(View.VISIBLE);
        }
        Picasso.with(context).load(item.getLogo()+"").placeholder(getPlaceholder(item.getCompany()+"")).error(getPlaceholder(item.getCompany()+"")).into(vh.imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                vh.textViewHolder.setText("");
            }

            @Override
            public void onError() {

            }
        });

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.unknow_wide);
        requestOptions.error(R.drawable.unknow_wide);
        requestOptions.centerCrop();


        if(item.getBackground()!=null && !item.getBackground().equals("")){
            Glide.with(context).setDefaultRequestOptions(requestOptions).load(item.getBackground()+"").thumbnail(0.3f).into(vh.imageView1);
            //Picasso.with(context).load(item.getBackground()+"").placeholder(R.drawable.unknow_wide).error(R.drawable.unknow_wide).into(vh.imageView1);
        }

        return vh.rootView;
    }

    private int getPlaceholder(String s){
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZЯЧСМИТЬБЮФЫВАПРОЛДЖЭЁЙЦУКЕНГШЩЗХЪІЄ0123456789";
        int n = abc.indexOf(s.substring(0,1).toUpperCase());
        Log.i("LOG_allgames" , n+" "+s.substring(0,1).toUpperCase());
        if(n == 0 || n == 20 || n == 40 || n == 60){
            return R.color.color1;
        }
        if(n == 1 || n == 21 || n == 41 || n == 61){
            return R.color.color2;
        }
        if(n == 2 || n == 22 || n == 42 || n == 62){
            return R.color.color3;
        }
        if(n == 3 || n == 23 || n == 43 || n == 63){
            return R.color.color4;
        }
        if(n == 4 || n == 24 || n == 44 || n == 64){
            return R.color.color5;
        }
        if(n == 5 || n == 25 || n == 45 || n == 65){
            return R.color.color6;
        }
        if(n == 6 || n == 26 || n == 46 || n == 66){
            return R.color.color7;
        }
        if(n == 7 || n == 27 || n == 47 || n == 67){
            return R.color.color8;
        }
        if(n == 8 || n == 28 || n == 48 || n == 68){
            return R.color.color9;
        }
        if(n == 9 || n == 29 || n == 49 || n == 69){
            return R.color.color10;
        }
        if(n == 10 || n == 30 || n == 50 || n == 70){
            return R.color.color11;
        }
        if(n == 11 || n == 31 || n == 51 || n == 71){
            return R.color.color12;
        }
        if(n == 12 || n == 32 || n == 52 || n == 72){
            return R.color.color13;
        }
        if(n == 13 || n == 33 || n == 53 || n == 73){
            return R.color.color14;
        }
        if(n == 14 || n == 34 || n == 54 || n == 74){
            return R.color.color15;
        }
        if(n == 15 || n == 35 || n == 55 || n == 75){
            return R.color.color16;
        }
        if(n == 16 || n == 36 || n == 56 || n == 76){
            return R.color.color17;
        }
        if(n == 17 || n == 37 || n == 57 || n == 77){
            return R.color.color18;
        }
        if(n == 18 || n == 38 || n == 58 || n == 78){
            return R.color.color19;
        }
        if(n == 19 || n == 39 || n == 59 || n == 79){
            return R.color.color20;
        }
        return R.color.color1;
    }

    private static class ViewHolder {
        public final ImageView imageView;
        public final ImageView imageView1;
        public final ImageView imageView2;
        private final RelativeLayout rootView;
        private final TextView textViewName1;
        private final TextView textViewName2;
        private final TextView textViewTasks;
        private final TextView textViewDate;
        private final TextView textViewPlay;
        private final TextView textViewPrize;
        private final TextView textViewHolder;
        private final TextView textViewStatus;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, ImageView imageView1, ImageView imageView2, TextView textViewName1, TextView textViewName2, TextView textViewTasks, TextView textViewDate, TextView textViewPrize, TextView textViewPlay, TextView textViewHolder, TextView textViewStatus) {
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
            this.textViewHolder = textViewHolder;
            this.textViewStatus = textViewStatus;
        }

        public static GamesNewAdapter.ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = rootView.findViewById(R.id.imageView);
            ImageView imageView1 = rootView.findViewById(R.id.imageView1);
            ImageView imageView2 = rootView.findViewById(R.id.imageView2);
            TextView textViewName1 = rootView.findViewById(R.id.name1);
            TextView textViewName2 = rootView.findViewById(R.id.name2);
            TextView textViewTasks = rootView.findViewById(R.id.name4);
            TextView textViewDate = rootView.findViewById(R.id.name3);
            TextView textViewPrize = rootView.findViewById(R.id.name5);
            TextView textViewPlay = rootView.findViewById(R.id.name6);
            TextView textViewHolder = rootView.findViewById(R.id.textHolder1);
            TextView textViewStatus = rootView.findViewById(R.id.status);
            return new GamesNewAdapter.ViewHolder(rootView, imageView, imageView1, imageView2, textViewName1, textViewName2, textViewTasks, textViewDate, textViewPrize, textViewPlay, textViewHolder, textViewStatus);
        }

    }
}

