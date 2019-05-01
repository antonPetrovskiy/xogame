package com.game.xogame.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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


    // Constructors
    public FeedsAdapter(Context context, List<Feed> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        feedList = objects;

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


            vh.placeholder1.setText(item.getCompany().substring(0, 1));
            vh.imageViewCompany.setImageResource(getPlaceholder(item.getCompany()));
        if(item.getUserName().length()!=0 ) {
            vh.placeholder2.setText(item.getUserName().substring(0, 1));
        }else{
            vh.placeholder2.setText("a");
        }
            vh.imageViewPhoto.setImageResource(getPlaceholder(item.getCompany()));



        Picasso.with(context).load(item.getLogoSponsorUrl()+"").placeholder(getPlaceholder(item.getCompany())).error(getPlaceholder(item.getCompany())).into(vh.imageViewCompany, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                vh.placeholder1.setText("");
            }

            @Override
            public void onError() {

            }
        });
        Picasso.with(context).load(item.getUserPhotoUrl()+"").placeholder(getPlaceholder(item.getUserNickname())).error(getPlaceholder(item.getUserNickname())).into(vh.imageViewUser, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                vh.placeholder2.setText("");
            }

            @Override
            public void onError() {

            }
        });
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.unknow);
        requestOptions.error(R.drawable.unknow);
        requestOptions.override(1024,1024);
        requestOptions.centerCrop();

        Glide.with(context).setDefaultRequestOptions(requestOptions).load(item.getTaskPhotoUrl()).thumbnail(0.3f).into(vh.imageViewPhoto);
        //Picasso.with(context).load(item.getTaskPhotoUrl()+"").centerCrop().resize(1024,1024).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(vh.imageViewPhoto);
        if(item.getUserLike().equals("0")){
            vh.imageViewLike.setImageResource(R.drawable.like_unused);
            vh.imageViewLike.setTag("unused");
        }else{
            vh.imageViewLike.setImageResource(R.drawable.like);
            vh.imageViewLike.setTag("used");
        }

        vh.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(vh.imageViewLike.getTag()).equals("used")){
                    vh.imageViewLike.setImageResource(R.drawable.like_unused);
                    vh.textViewLike.setText(Integer.parseInt(item.getFeedLikes())+"");
                    vh.imageViewLike.setTag("unused");
                }else{
                    vh.imageViewLike.setImageResource(R.drawable.like);
                    vh.textViewLike.setText((Integer.parseInt(item.getFeedLikes())+1)+"");
                    vh.imageViewLike.setTag("used");
                }


                SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
                String id = sharedPref.getString("token", "null");
                ApiService api = RetroClient.getApiService();
                if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                    Call<DefaultCallback> call = api.setLike(id,item.getFeedId());
                    call.enqueue(new Callback<DefaultCallback>() {
                        @Override
                        public void onResponse(Call<DefaultCallback> call, Response<DefaultCallback> response) {
                            if (response.isSuccessful()) {
                                Log.i("LOG_like" , "Success(error): " + response.body().getStatus()+item.getFeedId());
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

    public int getPlaceholder(String s){
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZЯЧСМИТЬБЮФЫВАПРОЛДЖЭЁЙЦУКЕНГШЩЗХЪІЄ0123456789";
        int n = abc.indexOf(s.substring(0,1).toUpperCase());
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
        public final TextView placeholder1;
        public final TextView placeholder2;


        private ViewHolder(LinearLayout rootView, ImageView imageView, ImageView imageView1, ImageView imageView2, ImageView imageView3, TextView textView1, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, TextView textView8, TextView textView9, TextView placeholder1, TextView placeholder2) {
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
            this.placeholder1 = placeholder1;
            this.placeholder2 = placeholder2;
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
            TextView placeholder1 = (TextView) rootView.findViewById(R.id.textHolder1);
            TextView placeholder2 = (TextView) rootView.findViewById(R.id.textHolder2);
            return new FeedsAdapter.ViewHolder(rootView, imageViewCompany, imageViewUser, imageViewPhoto, imageViewLike, textViewCompany, textViewName, textViewNickname, textViewTime, textViewDescription, textViewTask, textViewLike, textViewTag, textViewTitle, placeholder1, placeholder2);
        }

    }
}

