package com.game.xogame.views.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.game.xogame.R;
import com.game.xogame.entity.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    public GridLayout grid;
    public List<Game> gallery;
    public ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gallery = new ArrayList<>();
        String category = getIntent().getStringExtra("category");
        if(category.equals("all")){
            gallery = FragmentGames.gameList;
        }else{
            switch (category) {
                case "food":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("1")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                case "style":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("2")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                case "art":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("3")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                case "tv":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("4")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                case "fun":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("5")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                case "decor":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("6")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                case "nature":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("7")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                case "since":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("8")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                case "iscustvo":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("9")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                case "beauty":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("10")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                case "sport":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("11")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                case "texture":
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("12")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                default:
                    gallery = FragmentGames.gameList;
            }
        }
        init();
    }

    public void init(){
        back = findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryActivity.super.onBackPressed();
            }
        });
        grid = findViewById(R.id.grid);
        for(int i = 0; i < gallery.size(); i++){
            final int position = i;
            final ImageView image = new ImageView(CategoryActivity.this);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            final GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            final int n = i;
            ViewTreeObserver vto = grid.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        grid.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        grid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    int width = grid.getMeasuredWidth()/3;
                    int height = width;

                    layoutParams.width = width;
                    layoutParams.height = height;

                    image.setLayoutParams(layoutParams);

                        Picasso.with(CategoryActivity.this).load(gallery.get(n).getBackground()).placeholder(R.drawable.unknow_wide).error(R.drawable.unknow_wide).into(image);
                        image.setCropToPadding(true);
                        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        image.setPadding(10,10,10,10);
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CategoryActivity.this, GameInfoActivity.class);
                            intent.putExtra("GAMEID", gallery.get(position).getGameid());
                            intent.putExtra("SUBSCRIBE", gallery.get(position).getSubscribe());
                            intent.putExtra("TITLE", gallery.get(position).getTitle());
                            intent.putExtra("NAME", gallery.get(position).getCompany());
                            intent.putExtra("LOGO", gallery.get(position).getLogo());
                            intent.putExtra("BACKGROUND", gallery.get(position).getBackground());
                            intent.putExtra("DATE", gallery.get(position).getStartdate() + " - " + gallery.get(position).getEnddate());
                            intent.putExtra("DESCRIPTION", gallery.get(position).getDescription());
                            intent.putExtra("TASKS", gallery.get(position).getTasks());
                            intent.putExtra("TIME", gallery.get(position).getStarttime() + " - " + gallery.get(position).getEndtime());
                            intent.putExtra("MONEY", gallery.get(position).getReward());
                            intent.putExtra("PEOPLE", gallery.get(position).getFollowers());
                            intent.putExtra("SHARE", gallery.get(position).getSiteurl());
                            intent.putExtra("ADDRESS", gallery.get(position).getAddress());
                            intent.putExtra("CATEGORY", gallery.get(position).getCategory());
                            intent.putExtra("STATISTIC", "false");
                            startActivity(intent);
                        }
                    });


                    grid.addView(image,n);

                }
            });

        }
    }
}
