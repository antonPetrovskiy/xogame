package com.game.xogame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.entity.Feed;
import com.game.xogame.entity.Rating;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RatingAdapter extends ArrayAdapter<Rating> {
    public static List<Rating> ratingList;
    private Context context;
    private LayoutInflater mInflater;
    private LinearLayout lay;

    // Constructors
    public RatingAdapter(Context context, List<Rating> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        ratingList = objects;
    }

    @Override
    public Rating getItem(int position) {
        return ratingList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final RatingAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_rating, parent, false);
            vh = RatingAdapter.ViewHolder.create((LinearLayout) view);
            view.setTag(vh);



        } else {
            vh = (RatingAdapter.ViewHolder) convertView.getTag();
        }
        vh.rootView.setScrollContainer(false);
        vh.rootView.setNestedScrollingEnabled(false);
        vh.rootView.setClickable(false);

        final Rating item = getItem(position);
        vh.rootView.setClipToOutline(true);
        vh.textViewCompany.setText(item.getCompany());
        vh.textViewTitle.setText(item.getTitle());
        vh.textViewTime.setText(item.getStartdate() + " " + item.getStarttime());
        if(item.getActive().equals("0")){
            vh.textViewTimeLeft.setText("");
        }else{
            vh.textViewTimeLeft.setText("активно");
        }

        vh.textViewPeople.setText(item.getFollowers());
        vh.textViewTasks.setText(item.getTasks());
        vh.textViewPrise.setText(item.getReward());
        Picasso.with(context).load(item.getLogo()+"").placeholder(android.R.color.holo_red_dark).error(android.R.color.holo_red_dark).into(vh.imageViewCompany);
//        Picasso.with(context).load(item.getUserPhotoUrl()+"").placeholder(android.R.color.holo_red_dark).error(android.R.color.holo_red_dark).into(vh.imageViewUser);
//        Picasso.with(context).load(item.getTaskPhotoUrl()+"").placeholder(R.drawable.unknow).error(R.drawable.unknow).into(vh.imageViewPhoto);


        return vh.rootView;
    }


    private static class ViewHolder {
        public final LinearLayout rootView;
        public final ImageView imageViewCompany;

        public final TextView textViewCompany;
        public final TextView textViewTitle;
        public final TextView textViewTime;
        public final TextView textViewTimeLeft;
        public final TextView textViewPeople;
        public final TextView textViewTasks;
        public final TextView textViewPrise;


        private ViewHolder(LinearLayout rootView, ImageView imageView, TextView textView1, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7) {
            this.rootView = rootView;
            this.imageViewCompany = imageView;

            this.textViewCompany = textView1;
            this.textViewTitle = textView2;
            this.textViewTime = textView3;
            this.textViewTimeLeft = textView4;
            this.textViewPeople = textView5;
            this.textViewTasks = textView6;
            this.textViewPrise = textView7;
        }

        public static RatingAdapter.ViewHolder create(LinearLayout rootView) {
            ImageView imageViewCompany = (ImageView) rootView.findViewById(R.id.imageView);
            //ImageView imageViewUser = (ImageView) rootView.findViewById(R.id.imageView2);
            //ImageView imageViewPhoto = (ImageView) rootView.findViewById(R.id.imageView3);
            //ImageView imageViewLike = (ImageView) rootView.findViewById(R.id.imageView4);
            //ImageView imageViewLike = (ImageView) rootView.findViewById(R.id.imageView5);

            TextView textViewCompany = (TextView) rootView.findViewById(R.id.textView1);
            TextView textViewTitle = (TextView) rootView.findViewById(R.id.textView2);
            TextView textViewTime = (TextView) rootView.findViewById(R.id.textView3);
            TextView textViewTimeLeft = (TextView) rootView.findViewById(R.id.textView4);
            TextView textViewPeople = (TextView) rootView.findViewById(R.id.textView5);
            TextView textViewTasks = (TextView) rootView.findViewById(R.id.textView6);
            TextView textViewPrise = (TextView) rootView.findViewById(R.id.textView7);
//            TextView textViewTag = (TextView) rootView.findViewById(R.id.textView9);
//            TextView textViewTitle = (TextView) rootView.findViewById(R.id.textView0);
            return new RatingAdapter.ViewHolder(rootView, imageViewCompany, textViewCompany, textViewTitle, textViewTime, textViewTimeLeft, textViewPeople, textViewTasks, textViewPrise);
        }

    }
}
