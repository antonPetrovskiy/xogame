package com.game.xogame.views.game;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.xogame.R;

public class ModerationActivity extends AppCompatActivity {

    private TextView company;
    private TextView game;
    private TextView replaced;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_moderation);
    }

    public void init(){
        company = findViewById(R.id.textView01);
        game = findViewById(R.id.textView02);
        replaced = findViewById(R.id.textView03);
        logo = findViewById(R.id.imageView);
    }
}
