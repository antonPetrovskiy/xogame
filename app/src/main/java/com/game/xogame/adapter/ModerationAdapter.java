package com.game.xogame.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
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
import com.game.xogame.entity.Moderation;

import java.util.List;

public class ModerationAdapter extends ArrayAdapter<Moderation> {
    private static List<Moderation> moderationList;
    private Context context;
    private LayoutInflater mInflater;


    // Constructors
    public ModerationAdapter(Context context, List<Moderation> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        moderationList = objects;

    }

    @Override
    public Moderation getItem(int position) {
        return moderationList.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ModerationAdapter.ViewHolder vh;
        final Moderation item = getItem(position);
        if (convertView == null) {
            View view;
            view = mInflater.inflate(R.layout.layout_moderation, parent, false);
            vh = ModerationAdapter.ViewHolder.create((ConstraintLayout) view);
            view.setTag(vh);
        } else {
            vh = (ModerationAdapter.ViewHolder) convertView.getTag();
        }

        vh.rootView.setScrollContainer(false);
        vh.rootView.setNestedScrollingEnabled(false);
        vh.rootView.setClickable(false);
        vh.rootView.setClipToOutline(true);


        vh.textViewDescription.setText(item.getTasktext());
        vh.textViewTask.setText(item.getTasknum());
        vh.textViewTag.setText("#" + item.getReason());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.unknow);
        requestOptions.error(R.drawable.unknow);
        requestOptions.override(1024, 1024);
        requestOptions.centerCrop();
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(item.getTaskphoto()).thumbnail(0.3f).into(vh.imageViewPhoto);


        return vh.rootView;
    }



    private static class ViewHolder {
        private final ConstraintLayout rootView;

        private final ImageView imageViewPhoto;
        private final TextView textViewDescription;
        private final TextView textViewTask;
        private final TextView textViewTag;


        private ViewHolder(ConstraintLayout rootView, ImageView imageView2, TextView textView5, TextView textView6, TextView textView8) {
            this.rootView = rootView;
            this.imageViewPhoto = imageView2;
            this.textViewDescription = textView5;
            this.textViewTask = textView6;
            this.textViewTag = textView8;
        }

        public static ModerationAdapter.ViewHolder create(ConstraintLayout rootView) {
            ImageView imageViewPhoto = rootView.findViewById(R.id.imageView3);
            TextView textViewDescription = rootView.findViewById(R.id.textView6);
            TextView textViewTask = rootView.findViewById(R.id.textView4);
            TextView textViewTag = rootView.findViewById(R.id.textView9);

            return new ModerationAdapter.ViewHolder(rootView, imageViewPhoto, textViewDescription, textViewTask, textViewTag);
        }

    }


}

