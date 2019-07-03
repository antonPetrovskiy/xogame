package com.game.xogame.views.authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.adapter.TutorialAdapter;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class TutorialActivity extends AppCompatActivity {

    private List<Integer> itemList;
    private ViewPager infoCards;
    private TutorialAdapter viewPagerAdapter;
    public static ImageView skip;
    private TextView text;
    private ImageView number;
    private LinearLayout load;
    public static int page;
    private ConstraintLayout lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        if(sharedPref.getString("tutorial_guide", "false").equals("true")){
            Intent intent = new Intent(TutorialActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            sharedPref.edit().putString("tutorial_guide","true").commit();
        }
        init();


    }

    public void init(){
        infoCards = findViewById(R.id.pagerView);
        itemList = new LinkedList<>();

        switch (getResources().getConfiguration().locale.getLanguage()) {
            case "ru":
                itemList.add(R.drawable.tutorial1_ru);
                itemList.add(R.drawable.tutorial2_ru);
                itemList.add(R.drawable.tutorial3_ru);
                itemList.add(R.drawable.tutorial4_ru);
                itemList.add(R.drawable.tutorial5_ru);
                itemList.add(R.drawable.tutorial6_ru);
                itemList.add(R.drawable.tutorial7_ru);
                break;
            case "uk":
                itemList.add(R.drawable.tutorial1_ua);
                itemList.add(R.drawable.tutorial2_ua);
                itemList.add(R.drawable.tutorial3_ua);
                itemList.add(R.drawable.tutorial4_ua);
                itemList.add(R.drawable.tutorial5_ua);
                itemList.add(R.drawable.tutorial6_ua);
                itemList.add(R.drawable.tutorial7_ua);
                break;
            case "en":
                itemList.add(R.drawable.tutorial1_en);
                itemList.add(R.drawable.tutorial2_en);
                itemList.add(R.drawable.tutorial3_en);
                itemList.add(R.drawable.tutorial4_en);
                itemList.add(R.drawable.tutorial5_en);
                itemList.add(R.drawable.tutorial6_en);
                itemList.add(R.drawable.tutorial7_en);
                break;
            default:
                itemList.add(R.drawable.tutorial1_en);
                itemList.add(R.drawable.tutorial2_en);
                itemList.add(R.drawable.tutorial3_en);
                itemList.add(R.drawable.tutorial4_en);
                itemList.add(R.drawable.tutorial5_en);
                itemList.add(R.drawable.tutorial6_en);
                itemList.add(R.drawable.tutorial7_en);
                break;
        }

        skip = findViewById(R.id.imageView1);
        load = findViewById(R.id.targetView);
        text = findViewById(R.id.textView);
        number = findViewById(R.id.imageView5);
        lay = findViewById(R.id.tutoriall);
        viewPagerAdapter = new TutorialAdapter(this,itemList);
        infoCards.setAdapter(viewPagerAdapter);
        infoCards.setOffscreenPageLimit(7);
        Picasso.with(this).load(R.drawable.tutorial_n1).into(number);


        lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page == 6)
                    skip.performClick();
            }
        });



        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        infoCards.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setPosition(position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public void setPosition(int currentPage){
        page = currentPage;
        switch (currentPage) {
            case 0:
                text.setText(getString(R.string.activityTutorial_welcome));
                Picasso.with(this).load(R.drawable.tutorial_n1).placeholder(R.drawable.tutorial_n0).into(number);
                break;
            case 1:
                text.setText(getString(R.string.activityTutorial_subscribe));
                Picasso.with(this).load(R.drawable.tutorial_n2).placeholder(R.drawable.tutorial_n0).into(number);
                break;
            case 2:
                text.setText(getString(R.string.activityTutorial_dotasks));
                Picasso.with(this).load(R.drawable.tutorial_n3).placeholder(R.drawable.tutorial_n0).into(number);
                break;
            case 3:
                text.setText(getString(R.string.activityTutorial_befast));
                Picasso.with(this).load(R.drawable.tutorial_n4).placeholder(R.drawable.tutorial_n0).into(number);
                break;
            case 4:
                text.setText(getString(R.string.activityTutorial_win));
                Picasso.with(this).load(R.drawable.tutorial_n5).placeholder(R.drawable.tutorial_n0).into(number);
                break;
            case 5:
                text.setText(getString(R.string.activityTutorial_type));
                Picasso.with(this).load(R.drawable.tutorial_n6).placeholder(R.drawable.tutorial_n0).into(number);
                break;
            case 6:
                text.setText(getString(R.string.activityTurorial_signin));
                Picasso.with(this).load(R.drawable.tutorial_n7).placeholder(R.drawable.tutorial_n0).into(number);
                break;

        }
    }


}
