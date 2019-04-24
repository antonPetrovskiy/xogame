package com.game.xogame.views.main;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.GamesModel;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.presenters.MainPresenter;
import com.game.xogame.views.game.FragmentGames;
import com.game.xogame.views.game.FragmentFeeds;
import com.game.xogame.views.profile.FragmentProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity{

    public static SectionsPagerAdapter mSectionsPagerAdapter;
    public static ViewPager mViewPager;

    private ApiService api;
    private MainPresenter presenter;

    public static String token;

    private LottieAnimationView button3;
    private LottieAnimationView button2;
    private LottieAnimationView button1;
    private ImageView button4;
    private ImageView button5;
    private ImageView button6;
    public int currentItem=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        token = sharedPref.getString("token", "null");

        api = RetroClient.getApiService();
        UserInfoModel model = new UserInfoModel(api, getApplicationContext());
        GamesModel modelGames = new GamesModel(api, getApplicationContext());
        presenter = new MainPresenter(model,modelGames);
        presenter.attachMainView(this);
        initPUSH();




        button1 = findViewById(R.id.imageView1);
        button2 = findViewById(R.id.imageView2);
        button3 = findViewById(R.id.imageView3);
        button4 = findViewById(R.id.imageView7);
        button5 = findViewById(R.id.imageView8);
        button6 = findViewById(R.id.imageView6);
        button1.setAnimation("hide_right.json");
        button2.setAnimation("hide_left.json");
        button3.setAnimation("hide_left.json");
        button1.setSpeed(-6f);
        button2.setSpeed(6f);
        button3.setSpeed(6f);
        button1.playAnimation();
        button2.playAnimation();
        button3.playAnimation();

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentItem==1){

                }
                if(currentItem==2){
                    button2.setAnimation("hide_left.json");
                    button2.setSpeed(6f);
                    button2.playAnimation();
                    button1.setSpeed(-6f);
                    button1.playAnimation();
                }
                if(currentItem==3){
                    button3.setSpeed(6f);
                    button3.playAnimation();
                    button1.setSpeed(-6f);
                    button1.playAnimation();
                }
                mViewPager.setCurrentItem(0);
                currentItem = 1;
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentItem==1){
                    button1.setSpeed(6f);
                    button1.playAnimation();
                    button2.setAnimation("hide_left.json");
                    button2.setSpeed(-6f);
                    button2.playAnimation();
                }
                if(currentItem==2){

                }
                if(currentItem==3){
                    button3.setSpeed(6f);
                    button3.playAnimation();
                    button2.setAnimation("hide_right.json");
                    button2.setSpeed(-6f);
                    button2.playAnimation();
                }
                mViewPager.setCurrentItem(1);
                currentItem = 2;
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentItem==1){
                    button1.setSpeed(6f);
                    button1.playAnimation();
                    button3.setSpeed(-6f);
                    button3.playAnimation();
                }
                if(currentItem==2){
                    button2.setAnimation("hide_right.json");
                    button2.setSpeed(6f);
                    button2.playAnimation();
                    button3.setSpeed(-6f);
                    button3.playAnimation();
                }
                if(currentItem==3){

                }
                mViewPager.setCurrentItem(2);
                currentItem = 3;
            }
        });


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(0);
   
        //presenter.showUserInfo();


    }

    @Override
    public void onBackPressed() {

    }

    public void showToast(String s){
        Toast.makeText(getApplicationContext(), s,
                Toast.LENGTH_LONG).show();
    }

    public void initPUSH(){
        FirebaseMessaging.getInstance().subscribeToTopic("game1")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return FragmentGames.newInstance(getApplicationContext(),presenter);
                case 1:
                    return FragmentFeeds.newInstance(getApplicationContext(), presenter);
                case 2:
                    return FragmentProfile.newInstance(getApplicationContext(),presenter);
                default:
                    return null;//Это для того, что бы что-то вернулось, если порядковый номер вдруг будет больше 2. И в данном случае приложение закроется с ошибкой.

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


    }
}
