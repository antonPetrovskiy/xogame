package com.game.xogame.views.game;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.game.xogame.R;

public class ReportActivity extends AppCompatActivity {

    private ImageView back;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(){
        back = findViewById(R.id.imageView1);
        send = findViewById(R.id.imageButton);
        send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        send.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        send.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        onBackPressed();
                        break;
                }
                return true;
            }
        });

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        back.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        back.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        onBackPressed();
                        break;
                }
                return true;
            }
        });

    }
}
