package com.game.xogame.views.profile;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.xogame.R;

public class MoneyActivity extends AppCompatActivity {

    private String type;
    private String count;

    public TextView header;
    public TextView money;
    public TextView way;
    public TextView info;
    public ImageView back;
    public Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            type = null;
            count = null;
        } else {
            type = extras.getString("type");
            count = extras.getString("money");
        }

        init();

    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        header = findViewById(R.id.textView1);
        money = findViewById(R.id.textView2);
        way = findViewById(R.id.textView3);
        info = findViewById(R.id.textView4);
        back = findViewById(R.id.imageView1);
        ok = findViewById(R.id.imageButton);

        //todo
        if (type.equals("phone")) {
            header.setText("Перевести деньги на телефон");
            money.setText(count + " ₴");
            way.setText("Телефон");
            info.setText("+380958403060");
        } else {
            header.setText("Перевести деньги на карту");
            money.setText(count + " ₴");
            way.setText("Кредитная карта");
            info.setText("1234 1234 1234 1234");
        }

        ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        ok.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        ok.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        onBackPressed();
                        break;
                }
                return true;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
