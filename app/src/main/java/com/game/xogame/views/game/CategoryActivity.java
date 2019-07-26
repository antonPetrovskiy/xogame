package com.game.xogame.views.game;

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

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    public GridLayout grid;
    public List<Game> gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gallery = FragmentGames.gameList;
        init();
    }

    public void init(){
        grid = findViewById(R.id.grid);
        for(int i = 0; i < gallery.size(); i++){
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




                    grid.addView(image,n);

                }
            });

        }
    }
}
