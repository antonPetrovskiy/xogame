package com.game.xogame.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.game.xogame.R;
import com.game.xogame.views.authentication.TutorialActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TutorialAdapter extends PagerAdapter {
    private TutorialActivity activity;
    private LayoutInflater layoutInflater;
    public List<Integer> itemList;

    public TutorialAdapter(TutorialActivity activity, List<Integer> itemList){
        this.activity = activity;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.card, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        Picasso.with(activity).load(itemList.get(position)).into(imageView);

        Log.i("LOG_pager" , position + "  " + itemList.get(position));
        //activity.setPosition(position);
        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.page == 6){
                    activity.skip.performClick();
                }
            }
        });

        return view;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        activity.setPosition(super.getItemPosition(object));
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View v = (View) object;
        vp.removeView(v);
    }
}
