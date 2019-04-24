package com.game.xogame.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.DefaultCallback;
import com.game.xogame.entity.Feed;
import com.game.xogame.entity.Game;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class FeedsAdapter extends ArrayAdapter<Feed> {
    public static List<Feed> feedList;
    Context context;
    private LayoutInflater mInflater;
    private LinearLayout lay;

    private int[] colors;

    // Constructors
    public FeedsAdapter(Context context, List<Feed> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        feedList = objects;
        colors = context.getResources().getIntArray(R.array.rainbow);
    }

    @Override
    public Feed getItem(int position) {
        return feedList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FeedsAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_feed, parent, false);
            vh = FeedsAdapter.ViewHolder.create((LinearLayout) view);
            view.setTag(vh);



        } else {
            vh = (FeedsAdapter.ViewHolder) convertView.getTag();
        }
        vh.rootView.setScrollContainer(false);
        vh.rootView.setNestedScrollingEnabled(false);
        vh.rootView.setClickable(false);

        final Feed item = getItem(position);
        vh.rootView.setClipToOutline(true);
        vh.textViewCompany.setText(item.getCompany());
        vh.textViewTitle.setText(item.getTitle());
        vh.textViewName.setText(item.getUserName());
        vh.textViewNickname.setText(item.getUserNickname());
        vh.textViewTime.setText(item.getTaskTime()+"sec");
        vh.textViewDescription.setText(item.getTaskDescription());
        vh.textViewTask.setText(item.getTaskNumber()+"/"+item.getTasks());
        vh.textViewLike.setText(item.getFeedLikes());
        vh.textViewTag.setText(item.getTaskComment());



        Picasso.with(context).load(item.getLogoSponsorUrl()+"").placeholder(android.R.color.holo_red_dark).error(android.R.color.holo_red_dark).into(vh.imageViewCompany);
        Picasso.with(context).load(item.getUserPhotoUrl()+"").placeholder(android.R.color.holo_red_dark).error(android.R.color.holo_red_dark).into(vh.imageViewUser);
        Picasso.with(context).load(item.getTaskPhotoUrl()+"").placeholder(R.drawable.unknow).error(R.drawable.unknow).into(vh.imageViewPhoto);
        if(item.getUserLike().equals("0")){
            vh.imageViewLike.setImageResource(R.drawable.like_unused);
        }else{
            vh.imageViewLike.setImageResource(R.drawable.like);
        }
        //Picasso.with(context).load(item.getBackground()+"").placeholder(R.drawable.unknow).error(R.drawable.unknow).into(vh.imageViewLike);

        vh.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vh.imageViewLike.getDrawable() == context.getDrawable(R.drawable.like)){
                    vh.imageViewLike.setImageResource(R.drawable.like_unused);
                    vh.textViewLike.setText(Integer.parseInt(item.getFeedLikes()));
                }else{
                    vh.imageViewLike.setImageResource(R.drawable.like);
                    vh.textViewLike.setText(Integer.parseInt(item.getFeedLikes())+1);
                }


                SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
                String id = sharedPref.getString("token", "null");
                ApiService api = RetroClient.getApiService();
                if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                    Call<DefaultCallback> call = api.setLike(id,item.getFeedTaskId());
                    call.enqueue(new Callback<DefaultCallback>() {
                        @Override
                        public void onResponse(Call<DefaultCallback> call, Response<DefaultCallback> response) {
                            if (response.isSuccessful()) {
                                Log.i("LOG_like" , "Success(error): " + response.body().getStatus()+item.getFeedTaskId());
                            } else {
                                String jObjError = null;
                                try {
                                    jObjError = response.errorBody().string()+"";
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.i("LOG_like" , jObjError+" error");

                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultCallback> call, Throwable t) {
                            Log.i("LOG_like" , t.getMessage()+" fail");
                        }
                    });

                } else {
                    Log.i("LOG_gethistory" , "error internet");
                }
            }
        });

        return vh.rootView;
    }

    public int getPlaceholder(){
        int n = new Random().nextInt(5);
        return colors[n];
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final ImageView imageViewCompany;
        public final ImageView imageViewUser;
        public final ImageView imageViewPhoto;
        public final ImageView imageViewLike;
        public final TextView textViewCompany;
        public final TextView textViewTitle;
        public final TextView textViewName;
        public final TextView textViewNickname;
        public final TextView textViewTime;
        public final TextView textViewDescription;
        public final TextView textViewTask;
        public final TextView textViewLike;
        public final TextView textViewTag;


        private ViewHolder(LinearLayout rootView, ImageView imageView, ImageView imageView1, ImageView imageView2, ImageView imageView3, TextView textView1, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, TextView textView8, TextView textView9) {
            this.rootView = rootView;
            this.imageViewCompany = imageView;
            this.imageViewUser = imageView1;
            this.imageViewPhoto = imageView2;
            this.imageViewLike = imageView3;
            this.textViewCompany = textView9;
            this.textViewName = textView2;
            this.textViewNickname = textView3;
            this.textViewTime = textView4;
            this.textViewDescription = textView5;
            this.textViewTask = textView6;
            this.textViewLike = textView7;
            this.textViewTag = textView8;
            this.textViewTitle = textView1;
        }

        public static FeedsAdapter.ViewHolder create(LinearLayout rootView) {
            ImageView imageViewCompany = (ImageView) rootView.findViewById(R.id.imageView1);
            ImageView imageViewUser = (ImageView) rootView.findViewById(R.id.imageView2);
            ImageView imageViewPhoto = (ImageView) rootView.findViewById(R.id.imageView3);
            ImageView imageViewLike = (ImageView) rootView.findViewById(R.id.imageView4);
            TextView textViewCompany = (TextView) rootView.findViewById(R.id.textView1);
            TextView textViewName = (TextView) rootView.findViewById(R.id.textView2);
            TextView textViewNickname = (TextView) rootView.findViewById(R.id.textView3);
            TextView textViewTime = (TextView) rootView.findViewById(R.id.textView4);
            TextView textViewDescription = (TextView) rootView.findViewById(R.id.textView6);
            TextView textViewTask = (TextView) rootView.findViewById(R.id.textView7);
            TextView textViewLike = (TextView) rootView.findViewById(R.id.textView8);
            TextView textViewTag = (TextView) rootView.findViewById(R.id.textView9);
            TextView textViewTitle = (TextView) rootView.findViewById(R.id.textView0);
            return new FeedsAdapter.ViewHolder(rootView, imageViewCompany, imageViewUser, imageViewPhoto, imageViewLike, textViewCompany, textViewName, textViewNickname, textViewTime, textViewDescription, textViewTask, textViewLike, textViewTag, textViewTitle);
        }

    }
}

