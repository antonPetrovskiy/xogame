package com.game.xogame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

        if(item.getTop().size()>0){
            vh.top1Layout.setVisibility(View.VISIBLE);
            if(item.getTop().get(0).getName().length()!=0 ) {
                vh.placeholder1.setText(item.getTop().get(0).getName().substring(0, 1).toUpperCase());
            }else{
                vh.placeholder1.setText("А");
            }
            vh.photo1.setImageResource(getPlaceholder(item.getTop().get(0).getNickname()));
            Picasso.with(context).load(item.getTop().get(0).getPhoto()+"").placeholder(getPlaceholder(item.getTop().get(0).getNickname())).error(getPlaceholder(item.getTop().get(0).getNickname())).into(vh.photo1, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    vh.placeholder1.setText("");
                }
                @Override
                public void onError() {

                }
            });
            vh.name1.setText(item.getTop().get(0).getName());
            vh.nickname1.setText(item.getTop().get(0).getNickname());
            vh.place1.setText(item.getTop().get(0).getPosition()+" место");
            vh.task1.setText(item.getTop().get(0).getComplited()+"/"+item.getTasks());
            int n = 100/Integer.parseInt(item.getTasks());
            n = n*Integer.parseInt(item.getTop().get(0).getComplited());
            vh.bar1.setProgress(n);
        }else{
            vh.top1Layout.setVisibility(View.GONE);
        }

        if(item.getTop().size()>1){
            vh.top2Layout.setVisibility(View.VISIBLE);
            if(item.getTop().get(1).getName().length()!=0 ) {
                vh.placeholder2.setText(item.getTop().get(1).getName().substring(0, 1));
            }else{
                vh.placeholder2.setText("a");
            }
            vh.photo2.setImageResource(getPlaceholder(item.getTop().get(1).getNickname()));
            Picasso.with(context).load(item.getTop().get(1).getPhoto()+"").placeholder(getPlaceholder(item.getTop().get(1).getNickname())).error(getPlaceholder(item.getTop().get(1).getNickname())).into(vh.photo2, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    vh.placeholder2.setText("");
                }
                @Override
                public void onError() {

                }
            });
            vh.name2.setText(item.getTop().get(1).getName());
            vh.nickname2.setText(item.getTop().get(1).getNickname());
            vh.place2.setText(item.getTop().get(1).getPosition()+" место");
            vh.task2.setText(item.getTop().get(1).getComplited()+"/"+item.getTasks());
            int n = 100/Integer.parseInt(item.getTasks());
            n = n*Integer.parseInt(item.getTop().get(1).getComplited());
            vh.bar2.setProgress(n);
        }else{
            vh.top2Layout.setVisibility(View.GONE);
        }

        if(item.getTop().size()>2){
            vh.top3Layout.setVisibility(View.VISIBLE);
            if(item.getTop().get(2).getName().length()!=0 ) {
                vh.placeholder3.setText(item.getTop().get(2).getName().substring(0, 1).toUpperCase());
            }else{
                vh.placeholder3.setText("А");
            }
            vh.photo3.setImageResource(getPlaceholder(item.getTop().get(2).getNickname()));
            Picasso.with(context).load(item.getTop().get(2).getPhoto()+"").placeholder(getPlaceholder(item.getTop().get(2).getNickname())).error(getPlaceholder(item.getTop().get(2).getNickname())).into(vh.photo3, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    vh.placeholder3.setText("");
                }
                @Override
                public void onError() {

                }
            });
            vh.name3.setText(item.getTop().get(2).getName());
            vh.nickname3.setText(item.getTop().get(2).getNickname());
            vh.place3.setText(item.getTop().get(2).getPosition()+" место");
            vh.task3.setText(item.getTop().get(2).getComplited()+"/"+item.getTasks());
            int n = 100/Integer.parseInt(item.getTasks());
            n = n*Integer.parseInt(item.getTop().get(2).getComplited());
            vh.bar3.setProgress(n);
        }else{
            vh.top3Layout.setVisibility(View.GONE);
        }

        if(item.getTop().size()>3){
            vh.top4Layout.setVisibility(View.VISIBLE);
            if(item.getTop().get(3).getName().length()!=0 ) {
                vh.placeholder4.setText(item.getTop().get(3).getName().substring(0, 1));
            }else{
                vh.placeholder4.setText("a");
            }
            vh.photo4.setImageResource(getPlaceholder(item.getTop().get(3).getNickname()));
            Picasso.with(context).load(item.getTop().get(3).getPhoto()+"").placeholder(getPlaceholder(item.getTop().get(3).getNickname())).error(getPlaceholder(item.getTop().get(3).getNickname())).into(vh.photo4, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    vh.placeholder4.setText("");
                }
                @Override
                public void onError() {

                }
            });
            vh.name4.setText(item.getTop().get(3).getName());
            vh.nickname4.setText(item.getTop().get(3).getNickname());
            vh.place4.setText(item.getTop().get(3).getPosition()+" место");
            vh.task4.setText(item.getTop().get(3).getComplited()+"/"+item.getTasks());
            int n = 100/Integer.parseInt(item.getTasks());
            n = n*Integer.parseInt(item.getTop().get(3).getComplited());
            vh.bar4.setProgress(n);
        }else{
            vh.top4Layout.setVisibility(View.GONE);
        }

        vh.gameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vh.topLayout.getVisibility()==View.GONE){
                    vh.topLayout.setVisibility(View.VISIBLE);
                }else{
                    vh.topLayout.setVisibility(View.GONE);
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
        public final TextView textViewCompany;
        public final TextView textViewTitle;
        public final TextView textViewTime;
        public final TextView textViewTimeLeft;
        public final TextView textViewPeople;
        public final TextView textViewTasks;
        public final TextView textViewPrise;

        public final LinearLayout gameLayout;
        public final LinearLayout topLayout;
        public final LinearLayout top1Layout;
        public final LinearLayout top2Layout;
        public final LinearLayout top3Layout;
        public final LinearLayout top4Layout;

        public final TextView placeholder1;
        public final TextView placeholder2;
        public final TextView placeholder3;
        public final TextView placeholder4;
        public final ImageView photo1;
        public final ImageView photo2;
        public final ImageView photo3;
        public final ImageView photo4;
        public final TextView name1;
        public final TextView name2;
        public final TextView name3;
        public final TextView name4;
        public final TextView nickname1;
        public final TextView nickname2;
        public final TextView nickname3;
        public final TextView nickname4;
        public final TextView place1;
        public final TextView place2;
        public final TextView place3;
        public final TextView place4;
        public final TextView task1;
        public final TextView task2;
        public final TextView task3;
        public final TextView task4;
        public final ProgressBar bar1;
        public final ProgressBar bar2;
        public final ProgressBar bar3;
        public final ProgressBar bar4;

        private ViewHolder(LinearLayout rootView, LinearLayout gameLayout, LinearLayout topLayout, LinearLayout top1Layout, LinearLayout top2Layout, LinearLayout top3Layout, LinearLayout top4Layout, ImageView imageView, TextView textView1, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7,
                           TextView placeholder1, TextView placeholder2, TextView placeholder3, TextView placeholder4, ImageView photo1, ImageView photo2, ImageView photo3, ImageView photo4, TextView name1, TextView name2, TextView name3, TextView name4,
                           TextView nickname1, TextView nickname2, TextView nickname3, TextView nickname4, TextView place1, TextView place2, TextView place3, TextView place4,
                           TextView task1, TextView task2, TextView task3, TextView task4, ProgressBar bar1, ProgressBar bar2, ProgressBar bar3, ProgressBar bar4) {
            this.rootView = rootView;
            this.imageViewCompany = imageView;
            this.textViewCompany = textView1;
            this.textViewTitle = textView2;
            this.textViewTime = textView3;
            this.textViewTimeLeft = textView4;
            this.textViewPeople = textView5;
            this.textViewTasks = textView6;
            this.textViewPrise = textView7;

            this.gameLayout = gameLayout;
            this.topLayout = topLayout;
            this.top1Layout = top1Layout;
            this.top2Layout = top2Layout;
            this.top3Layout = top3Layout;
            this.top4Layout = top4Layout;

            this.placeholder1 = placeholder1;
            this.placeholder2 = placeholder2;
            this.placeholder3 = placeholder3;
            this.placeholder4 = placeholder4;
            this.photo1 = photo1;
            this.photo2 = photo2;
            this.photo3 = photo3;
            this.photo4 = photo4;
            this.name1 = name1;
            this.name2 = name2;
            this.name3 = name3;
            this.name4 = name4;
            this.nickname1 = nickname1;
            this.nickname2 = nickname2;
            this.nickname3 = nickname3;
            this.nickname4 = nickname4;
            this.place1 = place1;
            this.place2 = place2;
            this.place3 = place3;
            this.place4 = place4;
            this.task1 = task1;
            this.task2 = task2;
            this.task3 = task3;
            this.task4 = task4;
            this.bar1 = bar1;
            this.bar2 = bar2;
            this.bar3 = bar3;
            this.bar4 = bar4;
        }

        public static RatingAdapter.ViewHolder create(LinearLayout rootView) {
            ImageView imageViewCompany = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewCompany = (TextView) rootView.findViewById(R.id.textView1);
            TextView textViewTitle = (TextView) rootView.findViewById(R.id.textView2);
            TextView textViewTime = (TextView) rootView.findViewById(R.id.textView3);
            TextView textViewTimeLeft = (TextView) rootView.findViewById(R.id.textView4);
            TextView textViewPeople = (TextView) rootView.findViewById(R.id.textView5);
            TextView textViewTasks = (TextView) rootView.findViewById(R.id.textView6);
            TextView textViewPrise = (TextView) rootView.findViewById(R.id.textView7);

            LinearLayout gameLayout = rootView.findViewById(R.id.game);
            LinearLayout topLayout = rootView.findViewById(R.id.top);
            LinearLayout top1Layout = rootView.findViewById(R.id.layUser1);
            LinearLayout top2Layout = rootView.findViewById(R.id.layUser2);
            LinearLayout top3Layout = rootView.findViewById(R.id.layUser3);
            LinearLayout top4Layout = rootView.findViewById(R.id.layUser4);

            TextView placeholder1 = rootView.findViewById(R.id.textHolder2);
            TextView placeholder2 = rootView.findViewById(R.id.textHolder3);
            TextView placeholder3 = rootView.findViewById(R.id.textHolder4);
            TextView placeholder4 = rootView.findViewById(R.id.textHolder5);
            ImageView photo1 = rootView.findViewById(R.id.imageView11);
            ImageView photo2 = rootView.findViewById(R.id.imageView12);
            ImageView photo3 = rootView.findViewById(R.id.imageView13);
            ImageView photo4 = rootView.findViewById(R.id.imageView14);
            TextView name1 = rootView.findViewById(R.id.textView11);
            TextView name2 = rootView.findViewById(R.id.textView15);
            TextView name3 = rootView.findViewById(R.id.textView19);
            TextView name4 = rootView.findViewById(R.id.textView23);
            TextView nickname1 = rootView.findViewById(R.id.textView13);
            TextView nickname2 = rootView.findViewById(R.id.textView17);
            TextView nickname3 = rootView.findViewById(R.id.textView21);
            TextView nickname4 = rootView.findViewById(R.id.textView25);
            TextView place1 = rootView.findViewById(R.id.textView12);
            TextView place2 = rootView.findViewById(R.id.textView16);
            TextView place3 = rootView.findViewById(R.id.textView20);
            TextView place4 = rootView.findViewById(R.id.textView24);
            TextView task1 = rootView.findViewById(R.id.textView14);
            TextView task2 = rootView.findViewById(R.id.textView18);
            TextView task3 = rootView.findViewById(R.id.textView22);
            TextView task4 = rootView.findViewById(R.id.textView26);
            ProgressBar bar1 = rootView.findViewById(R.id.bar1);
            ProgressBar bar2 = rootView.findViewById(R.id.bar2);
            ProgressBar bar3 = rootView.findViewById(R.id.bar3);
            ProgressBar bar4 = rootView.findViewById(R.id.bar4);

            return new RatingAdapter.ViewHolder(rootView, gameLayout, topLayout, top1Layout, top2Layout, top3Layout, top4Layout, imageViewCompany, textViewCompany, textViewTitle, textViewTime, textViewTimeLeft, textViewPeople, textViewTasks, textViewPrise,
                    placeholder1, placeholder2, placeholder3, placeholder4, photo1, photo2, photo3, photo4, name1, name2, name3, name4,
                    nickname1, nickname2, nickname3, nickname4, place1, place2, place3, place4,
                    task1, task2, task3, task4, bar1, bar2, bar3, bar4);
        }

    }
}
