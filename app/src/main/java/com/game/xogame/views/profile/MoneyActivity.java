package com.game.xogame.views.profile;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.xogame.R;

public class MoneyActivity extends AppCompatActivity {

    private String type;
    private String count;

    private TextView header;
    private TextView money;
    private TextView way;
    private TextView info;
    private ImageView back;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            type = null;
            count = null;
        } else {
            type= extras.getString("type");
            count= extras.getString("money");
        }

        init();

    }

    public void init(){
        header = findViewById(R.id.textView1);
        money = findViewById(R.id.textView2);
        way = findViewById(R.id.textView3);
        info = findViewById(R.id.textView4);
        back = findViewById(R.id.imageView1);
        ok = findViewById(R.id.imageButton);

        if(type.equals("phone")){
            header.setText("Перевести деньги на телефон");
            money.setText(count+" ₴");
            way.setText("Телефон");
            info.setText("+380958403060");
        }else{
            header.setText("Перевести деньги на карту");
            money.setText(count+" ₴");
            way.setText("Кредитная карта");
            info.setText("1234 1234 1234 1234");
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



}
