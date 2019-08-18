package com.game.xogame.views.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.entity.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    public GridLayout grid;
    public List<Game> gallery;
    public ImageView back;
    public TextView categoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gallery = new ArrayList<>();
        String category = getIntent().getStringExtra("category");
        categoryView = findViewById(R.id.textView);
        categoryView.setText(category);
        if(category.equals("all")){
            gallery = FragmentGames.tmpgameList;
            categoryView.setText(getString(R.string.category_all));
        }else{
            Log.i("LOG_category" , category +" ");
            Log.i("LOG_category" , FragmentGames.tmpgameList.size() +" size");
            for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                Log.i("LOG_category" , FragmentGames.tmpgameList.get(i).getCategoryId() +" ");
            }
            switch (category) {
                case "auto":
                    categoryView.setText(getString(R.string.category_auto));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("0")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "sport":
                    categoryView.setText(getString(R.string.category_sport));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("1")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "food":
                    categoryView.setText(getString(R.string.category_food));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("2")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "travel":
                    categoryView.setText(getString(R.string.category_travel));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("3")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "fun":
                    categoryView.setText(getString(R.string.category_fun));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("4")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "tv":
                    categoryView.setText(getString(R.string.category_tv));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("5")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "beauty":
                    categoryView.setText(getString(R.string.category_beauty));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("6")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "fashion":
                    categoryView.setText(getString(R.string.category_beauty));
                    for(int i = 0; i < FragmentGames.gameList.size(); i++){
                        if(FragmentGames.gameList.get(i).getCategoryId().equals("7")){
                            gallery.add(FragmentGames.gameList.get(i));
                        }
                    }
                    break;
                case "decor":
                    categoryView.setText(getString(R.string.category_decor));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("8")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "iscustvo":
                    categoryView.setText(getString(R.string.category_iscustvo));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("9")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "art":
                    categoryView.setText(getString(R.string.category_art));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("10")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "style":
                    categoryView.setText(getString(R.string.category_style));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("11")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "myday":
                    categoryView.setText(getString(R.string.category_myday));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("12")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
                case "other":
                    categoryView.setText(getString(R.string.category_other));
                    for(int i = 0; i < FragmentGames.tmpgameList.size(); i++){
                        if(FragmentGames.tmpgameList.get(i).getCategoryId().equals("13")){
                            gallery.add(FragmentGames.tmpgameList.get(i));
                        }
                    }
                    break;
            }
        }
        init();
    }

    public void init(){
        back = findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        grid = findViewById(R.id.grid);
        for(int i = 0; i < gallery.size(); i++){
            final int position = i;
            final RelativeLayout lay = new RelativeLayout(CategoryActivity.this);
            final ImageView image = new ImageView(CategoryActivity.this);
            final ImageView image1 = new ImageView(CategoryActivity.this);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            final GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            final RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(grid.getMeasuredWidth()/2,grid.getMeasuredWidth()/2);
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
                    int width = grid.getMeasuredWidth()/2;
                    int height = width/2;

                    layoutParams.width = width;
                    layoutParams.height = height;
                    image.setLayoutParams(layoutParams);
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    layoutParams1.width = Math.round(20 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
                    layoutParams1.height = Math.round(20 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
                    layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    int d = 12+Math.round(7* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
                    layoutParams1.setMargins(d,d,d,d);
                    image1.setLayoutParams(layoutParams1);
                    lay.setLayoutParams(layoutParams);



                    Picasso.with(CategoryActivity.this).load(gallery.get(n).getBackground()).placeholder(R.drawable.unknow_wide).error(R.drawable.unknow_wide).into(image);
                        image.setCropToPadding(true);
                        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        image.setPadding(12,12,12,12);

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
                            if(gallery.get(position).getStartdate().equals(gallery.get(position).getEnddate())){
                                intent.putExtra("DATE", gallery.get(position).getStartdate());
                            }else{
                                intent.putExtra("DATE", gallery.get(position).getStartdate() + " - " + gallery.get(position).getEnddate());
                            }
                            intent.putExtra("DESCRIPTION", gallery.get(position).getDescription());
                            intent.putExtra("TASKS", gallery.get(position).getTasks());
                            intent.putExtra("TIME", gallery.get(position).getStarttime() + " - " + gallery.get(position).getEndtime());
                            intent.putExtra("MONEY", gallery.get(position).getReward());
                            intent.putExtra("PEOPLE", gallery.get(position).getFollowers());
                            intent.putExtra("SHARE", gallery.get(position).getSiteurl());
                            intent.putExtra("ADDRESS", gallery.get(position).getAddress());
                            intent.putExtra("CATEGORY", gallery.get(position).getCategory());
                            intent.putExtra("CATEGORYID", gallery.get(position).getCategoryId());
                            intent.putExtra("OWNER", gallery.get(position).isOwner()+"");
                            intent.putExtra("STATISTIC", "false");
                            startActivity(intent);
                        }
                    });

                    lay.addView(image);
                    if(gallery.get(n).isOwner()){
                        image1.setImageResource(R.drawable.icon_category_my);
                        lay.addView(image1);
                    }else if(gallery.get(n).getSubscribe()){
                        image1.setImageResource(R.drawable.icon_category_sub);
                        lay.addView(image1);
                    }
                    grid.addView(lay,n);

                }
            });

        }
    }
}
