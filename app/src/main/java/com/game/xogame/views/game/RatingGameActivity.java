package com.game.xogame.views.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.RatingModel;
import com.game.xogame.presenters.MyGamesPresenter;
import com.game.xogame.presenters.RatingPresenter;
import com.squareup.picasso.Picasso;

public class RatingGameActivity extends AppCompatActivity {
    private ApiService api;
    private RatingPresenter presenter;

    private ImageView photo;
    private TextView sponsor;
    private TextView title;
    private TextView time;
    private TextView active;
    private TextView people;
    private TextView tasks;
    private TextView prise;
    private TextView placeholder1;
    private TextView placeholder2;
    private TextView placeholder3;
    private TextView placeholder4;
    private TextView placeholder5;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_game);
        api = RetroClient.getApiService();
        RatingModel gamesModel = new RatingModel(api, getApplicationContext());
        presenter = new RatingPresenter(gamesModel);
        presenter.attacRatingGamehView(this);
        init();
    }

    public void init(){
        sponsor = findViewById(R.id.textView1);
        title = findViewById(R.id.textView2);
        time = findViewById(R.id.textView3);
        active = findViewById(R.id.textView4);
        people = findViewById(R.id.textView5);
        tasks = findViewById(R.id.textView6);
        prise = findViewById(R.id.textView7);
        photo = findViewById(R.id.imageView);
        placeholder1 = findViewById(R.id.textHolder1);
        placeholder2 = findViewById(R.id.textHolder2);
        placeholder3 = findViewById(R.id.textHolder3);
        placeholder4 = findViewById(R.id.textHolder4);
        placeholder5 = findViewById(R.id.textHolder5);



        back = findViewById(R.id.imageView0);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setGameViews(String photo, String sponsor, String title, String time, String active, String people, String tasks, String prise){
        placeholder1.setText(sponsor.substring(0, 1));
        this.photo.setImageResource(getPlaceholder(sponsor));
        Picasso.with(this).load(photo+"").placeholder(getPlaceholder(sponsor)).error(getPlaceholder(sponsor)).into(this.photo, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                placeholder1.setText("");
            }
            @Override
            public void onError() {

            }
        });

        this.sponsor.setText(sponsor+"");
        this.title.setText(title+"");
        this.time.setText(time+"");
        this.active.setText(active+"");
        this.people.setText(people+"");
        this.tasks.setText(tasks+"");
        this.prise.setText(prise+"");
    }

    public void setGameViews(String sponsor, String title, String time, String active, String people, String tasks, String prise){

        this.sponsor.setText(sponsor+"");
        this.title.setText(title+"");
        this.time.setText(time+"");
        this.active.setText(active+"");
        this.people.setText(people+"");
        this.tasks.setText(tasks+"");
        this.prise.setText(prise+"");
    }

    public int getPlaceholder(String s){
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZЯЧСМИТЬБЮФЫВАПРОЛДЖЭЁЙЦУКЕНГШЩЗХЪІЄ0123456789";
        int n = abc.indexOf(s.substring(0,1).toUpperCase());
        if(n == 0 || n == 20 || n == 40 || n == 60){
            return R.color.color1;
        }
        if(n == 1 || n == 21 || n == 41 || n == 61){
            return R.color.color2;
        }
        if(n == 2 || n == 22 || n == 42 || n == 62){
            return R.color.color3;
        }
        if(n == 3 || n == 23 || n == 43 || n == 63){
            return R.color.color4;
        }
        if(n == 4 || n == 24 || n == 44 || n == 64){
            return R.color.color5;
        }
        if(n == 5 || n == 25 || n == 45 || n == 65){
            return R.color.color6;
        }
        if(n == 6 || n == 26 || n == 46 || n == 66){
            return R.color.color7;
        }
        if(n == 7 || n == 27 || n == 47 || n == 67){
            return R.color.color8;
        }
        if(n == 8 || n == 28 || n == 48 || n == 68){
            return R.color.color9;
        }
        if(n == 9 || n == 29 || n == 49 || n == 69){
            return R.color.color10;
        }
        if(n == 10 || n == 30 || n == 50 || n == 70){
            return R.color.color11;
        }
        if(n == 11 || n == 31 || n == 51 || n == 71){
            return R.color.color12;
        }
        if(n == 12 || n == 32 || n == 52 || n == 72){
            return R.color.color13;
        }
        if(n == 13 || n == 33 || n == 53 || n == 73){
            return R.color.color14;
        }
        if(n == 14 || n == 34 || n == 54 || n == 74){
            return R.color.color15;
        }
        if(n == 15 || n == 35 || n == 55 || n == 75){
            return R.color.color16;
        }
        if(n == 16 || n == 36 || n == 56 || n == 76){
            return R.color.color17;
        }
        if(n == 17 || n == 37 || n == 57 || n == 77){
            return R.color.color18;
        }
        if(n == 18 || n == 38 || n == 58 || n == 78){
            return R.color.color19;
        }
        if(n == 19 || n == 39 || n == 59 || n == 79){
            return R.color.color20;
        }
        return R.color.color1;
    }

}
