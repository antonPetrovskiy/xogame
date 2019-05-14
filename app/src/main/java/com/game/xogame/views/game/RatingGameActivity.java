package com.game.xogame.views.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.Rating;
import com.game.xogame.models.RatingModel;
import com.game.xogame.presenters.RatingPresenter;
import com.game.xogame.views.profile.UserProfileActivity;
import com.squareup.picasso.Picasso;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RatingGameActivity extends AppCompatActivity {
    ImageView back;
    public ApiService api;
    public RatingPresenter presenter;
    private LinearLayout load;
    private LinearLayout game;
    private LinearLayout user1;
    private LinearLayout user2;
    private LinearLayout user3;
    private LinearLayout user4;
    private ImageView photo;
    private TextView sponsor;
    private TextView title;
    private TextView time;
    private TextView people;
    private TextView tasks;
    private TextView prise;
    private TextView placeholder;
    private TextView placeholder1;
    private TextView placeholder2;
    private TextView placeholder3;
    private TextView placeholder4;
    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private ImageView photo4;
    private TextView name1;
    private TextView name2;
    private TextView name3;
    private TextView name4;
    private TextView nickname1;
    private TextView nickname2;
    private TextView nickname3;
    private TextView nickname4;
    private TextView place1;
    private TextView place2;
    private TextView place3;
    private TextView place4;
    private TextView task1;
    private TextView task2;
    private TextView task3;
    private TextView task4;
    private ProgressBar bar1;
    private ProgressBar bar2;
    private ProgressBar bar3;
    private ProgressBar bar4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        api = RetroClient.getApiService();
        RatingModel gamesModel = new RatingModel(api, getApplicationContext());
        presenter = new RatingPresenter(gamesModel);
        presenter.attacRatingGamehView(this);
        init();
        presenter.showGameRating(getIntent().getStringExtra("gameid"));
    }

    public void init() {
        load = findViewById(R.id.targetView);
        game = findViewById(R.id.game);
        user1 = findViewById(R.id.layUser1);
        user2 = findViewById(R.id.layUser2);
        user3 = findViewById(R.id.layUser3);
        user4 = findViewById(R.id.layUser4);
        sponsor = findViewById(R.id.textView1);
        title = findViewById(R.id.textView2);
        time = findViewById(R.id.textView3);
        people = findViewById(R.id.textView5);
        tasks = findViewById(R.id.textView6);
        prise = findViewById(R.id.textView7);
        photo = findViewById(R.id.imageView);
        placeholder = findViewById(R.id.textHolder1);
        placeholder1 = findViewById(R.id.textHolder2);
        placeholder2 = findViewById(R.id.textHolder3);
        placeholder3 = findViewById(R.id.textHolder4);
        placeholder4 = findViewById(R.id.textHolder5);
        photo1 = findViewById(R.id.imageView11);
        photo2 = findViewById(R.id.imageView12);
        photo3 = findViewById(R.id.imageView13);
        photo4 = findViewById(R.id.imageView14);
        name1 = findViewById(R.id.textView11);
        name2 = findViewById(R.id.textView15);
        name3 = findViewById(R.id.textView19);
        name4 = findViewById(R.id.textView23);
        nickname1 = findViewById(R.id.textView13);
        nickname2 = findViewById(R.id.textView17);
        nickname3 = findViewById(R.id.textView21);
        nickname4 = findViewById(R.id.textView25);
        place1 = findViewById(R.id.textView12);
        place2 = findViewById(R.id.textView16);
        place3 = findViewById(R.id.textView20);
        place4 = findViewById(R.id.textView24);
        task1 = findViewById(R.id.textView14);
        task2 = findViewById(R.id.textView18);
        task3 = findViewById(R.id.textView22);
        task4 = findViewById(R.id.textView26);
        bar1 = findViewById(R.id.bar1);
        bar2 = findViewById(R.id.bar2);
        bar3 = findViewById(R.id.bar3);
        bar4 = findViewById(R.id.bar4);


        back = findViewById(R.id.imageView0);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setGameViews(Rating game) {
        this.game.setVisibility(View.VISIBLE);
        load.setVisibility(View.GONE);
        if(game == null)
            return;
        placeholder.setText(game.getCompany().substring(0, 1));
        this.photo.setImageResource(getPlaceholder(game.getCompany()));
        Picasso.with(this).load(game.getLogo() + "").placeholder(getPlaceholder(game.getCompany())).error(getPlaceholder(game.getCompany())).into(this.photo, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                placeholder.setText("");
            }

            @Override
            public void onError() {

            }
        });
        this.sponsor.setText(game.getCompany() + "");
        this.title.setText(game.getTitle() + "");
        this.time.setText(game.getEnddate() + " " + game.getEndtime());
        //this.active.setText(game.getActive()+"");
        this.people.setText(game.getFollowers() + "");
        this.tasks.setText(game.getTasks() + "");
        this.prise.setText(game.getReward() + " ₴");
        final Rating game1 = game;

        if (game.getTop().size() > 0) {
            this.user1.setVisibility(View.VISIBLE);
            if (game.getTop().get(0).getName().length() != 0) {
                this.placeholder1.setText(game.getTop().get(0).getName().substring(0, 1));
            } else {
                this.placeholder1.setText("A");
            }
            this.photo1.setImageResource(getPlaceholder(game.getTop().get(0).getNickname()));
            Picasso.with(this).load(game.getTop().get(0).getPhoto() + "").placeholder(getPlaceholder(game.getTop().get(0).getNickname())).error(getPlaceholder(game.getTop().get(0).getNickname())).into(this.photo1, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    placeholder1.setText("");
                }

                @Override
                public void onError() {

                }
            });
            this.name1.setText(game.getTop().get(0).getName());
            this.nickname1.setText(game.getTop().get(0).getNickname());
            this.place1.setText(game.getTop().get(0).getPosition() + " " + getString(R.string.adapterRating_place));
            this.task1.setText(game.getTop().get(0).getComplited() + "/" + game.getTasks());
            int n = 1000 / Integer.parseInt(game.getTasks());
            n = n * Integer.parseInt(game.getTop().get(0).getComplited());
            bar1.setProgress(n);
            photo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RatingGameActivity.this, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", game1.getTop().get(0).getName());
                    intent.putExtra("NICKNAME", game1.getTop().get(0).getNickname());
                    intent.putExtra("PHOTO", game1.getTop().get(0).getPhoto());
                    intent.putExtra("USERID", game1.getTop().get(0).getUserid());
                    startActivity(intent);
                }
            });
            name1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RatingGameActivity.this, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", game1.getTop().get(0).getName());
                    intent.putExtra("NICKNAME", game1.getTop().get(0).getNickname());
                    intent.putExtra("PHOTO", game1.getTop().get(0).getPhoto());
                    intent.putExtra("USERID", game1.getTop().get(0).getUserid());
                    startActivity(intent);
                }
            });
        }

        if (game.getTop().size() > 1) {
            this.user2.setVisibility(View.VISIBLE);
            if (game.getTop().get(1).getName().length() != 0) {
                this.placeholder2.setText(game.getTop().get(1).getName().substring(0, 1));
            } else {
                this.placeholder2.setText("A");
            }
            this.photo2.setImageResource(getPlaceholder(game.getTop().get(1).getNickname()));
            Picasso.with(this).load(game.getTop().get(1).getPhoto() + "").placeholder(getPlaceholder(game.getTop().get(1).getNickname())).error(getPlaceholder(game.getTop().get(1).getNickname())).into(this.photo2, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    placeholder2.setText("");
                }

                @Override
                public void onError() {

                }
            });
            this.name2.setText(game.getTop().get(1).getName());
            this.nickname2.setText(game.getTop().get(1).getNickname());
            this.place2.setText(game.getTop().get(1).getPosition() + " " + getString(R.string.adapterRating_place));
            this.task2.setText(game.getTop().get(1).getComplited() + "/" + game.getTasks());
            int n = 1000 / Integer.parseInt(game.getTasks());
            n = n * Integer.parseInt(game.getTop().get(1).getComplited());
            bar2.setProgress(n);
            photo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RatingGameActivity.this, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", game1.getTop().get(1).getName());
                    intent.putExtra("NICKNAME", game1.getTop().get(1).getNickname());
                    intent.putExtra("PHOTO", game1.getTop().get(1).getPhoto());
                    intent.putExtra("USERID", game1.getTop().get(1).getUserid());
                    startActivity(intent);
                }
            });
            name2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RatingGameActivity.this, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", game1.getTop().get(1).getName());
                    intent.putExtra("NICKNAME", game1.getTop().get(1).getNickname());
                    intent.putExtra("PHOTO", game1.getTop().get(1).getPhoto());
                    intent.putExtra("USERID", game1.getTop().get(1).getUserid());
                    startActivity(intent);
                }
            });
        }

        if (game.getTop().size() > 2) {
            this.user3.setVisibility(View.VISIBLE);
            if (game.getTop().get(2).getName().length() != 0) {
                this.placeholder3.setText(game.getTop().get(2).getName().substring(0, 1));
            } else {
                this.placeholder3.setText("A");
            }
            this.photo3.setImageResource(getPlaceholder(game.getTop().get(2).getNickname()));
            Picasso.with(this).load(game.getTop().get(2).getPhoto() + "").placeholder(getPlaceholder(game.getTop().get(2).getNickname())).error(getPlaceholder(game.getTop().get(2).getNickname())).into(this.photo3, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    placeholder3.setText("");
                }

                @Override
                public void onError() {

                }
            });
            this.name3.setText(game.getTop().get(2).getName());
            this.nickname3.setText(game.getTop().get(2).getNickname());
            this.place3.setText(game.getTop().get(2).getPosition() + " " + getString(R.string.adapterRating_place));
            this.task3.setText(game.getTop().get(2).getComplited() + "/" + game.getTasks());
            int n = 1000 / Integer.parseInt(game.getTasks());
            n = n * Integer.parseInt(game.getTop().get(2).getComplited());
            bar3.setProgress(n);
            photo3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RatingGameActivity.this, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", game1.getTop().get(2).getName());
                    intent.putExtra("NICKNAME", game1.getTop().get(2).getNickname());
                    intent.putExtra("PHOTO", game1.getTop().get(2).getPhoto());
                    intent.putExtra("USERID", game1.getTop().get(2).getUserid());
                    startActivity(intent);
                }
            });
            name3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RatingGameActivity.this, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", game1.getTop().get(2).getName());
                    intent.putExtra("NICKNAME", game1.getTop().get(2).getNickname());
                    intent.putExtra("PHOTO", game1.getTop().get(2).getPhoto());
                    intent.putExtra("USERID", game1.getTop().get(2).getUserid());
                    startActivity(intent);
                }
            });
        }

        if (game.getTop().size() > 3) {
            this.user4.setVisibility(View.VISIBLE);
            if (game.getTop().get(3).getName().length() != 0) {
                this.placeholder4.setText(game.getTop().get(3).getName().substring(0, 1));
            } else {
                this.placeholder4.setText("A");
            }
            this.photo4.setImageResource(getPlaceholder(game.getTop().get(3).getNickname()));
            Picasso.with(this).load(game.getTop().get(3).getPhoto() + "").placeholder(getPlaceholder(game.getTop().get(3).getNickname())).error(getPlaceholder(game.getTop().get(3).getNickname())).into(this.photo4, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    placeholder4.setText("");
                }

                @Override
                public void onError() {

                }
            });
            this.name4.setText(game.getTop().get(3).getName());
            this.nickname4.setText(game.getTop().get(3).getNickname());
            this.place4.setText(game.getTop().get(3).getPosition() + " " + getString(R.string.adapterRating_place));
            this.task4.setText(game.getTop().get(3).getComplited() + "/" + game.getTasks());
            int n = 1000 / Integer.parseInt(game.getTasks());
            n = n * Integer.parseInt(game.getTop().get(3).getComplited());
            bar4.setProgress(n);
            photo4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RatingGameActivity.this, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", game1.getTop().get(3).getName());
                    intent.putExtra("NICKNAME", game1.getTop().get(3).getNickname());
                    intent.putExtra("PHOTO", game1.getTop().get(3).getPhoto());
                    intent.putExtra("USERID", game1.getTop().get(3).getUserid());
                    startActivity(intent);
                }
            });
            name4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RatingGameActivity.this, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", game1.getTop().get(3).getName());
                    intent.putExtra("NICKNAME", game1.getTop().get(3).getNickname());
                    intent.putExtra("PHOTO", game1.getTop().get(3).getPhoto());
                    intent.putExtra("USERID", game1.getTop().get(3).getUserid());
                    startActivity(intent);
                }
            });
        }

    }


    public int getPlaceholder(String s) {
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZЯЧСМИТЬБЮФЫВАПРОЛДЖЭЁЙЦУКЕНГШЩЗХЪІЄ0123456789";
        int n = abc.indexOf(s.substring(0, 1).toUpperCase());
        if (n == 0 || n == 20 || n == 40 || n == 60) {
            return R.color.color1;
        }
        if (n == 1 || n == 21 || n == 41 || n == 61) {
            return R.color.color2;
        }
        if (n == 2 || n == 22 || n == 42 || n == 62) {
            return R.color.color3;
        }
        if (n == 3 || n == 23 || n == 43 || n == 63) {
            return R.color.color4;
        }
        if (n == 4 || n == 24 || n == 44 || n == 64) {
            return R.color.color5;
        }
        if (n == 5 || n == 25 || n == 45 || n == 65) {
            return R.color.color6;
        }
        if (n == 6 || n == 26 || n == 46 || n == 66) {
            return R.color.color7;
        }
        if (n == 7 || n == 27 || n == 47 || n == 67) {
            return R.color.color8;
        }
        if (n == 8 || n == 28 || n == 48 || n == 68) {
            return R.color.color9;
        }
        if (n == 9 || n == 29 || n == 49 || n == 69) {
            return R.color.color10;
        }
        if (n == 10 || n == 30 || n == 50 || n == 70) {
            return R.color.color11;
        }
        if (n == 11 || n == 31 || n == 51 || n == 71) {
            return R.color.color12;
        }
        if (n == 12 || n == 32 || n == 52 || n == 72) {
            return R.color.color13;
        }
        if (n == 13 || n == 33 || n == 53 || n == 73) {
            return R.color.color14;
        }
        if (n == 14 || n == 34 || n == 54 || n == 74) {
            return R.color.color15;
        }
        if (n == 15 || n == 35 || n == 55 || n == 75) {
            return R.color.color16;
        }
        if (n == 16 || n == 36 || n == 56 || n == 76) {
            return R.color.color17;
        }
        if (n == 17 || n == 37 || n == 57 || n == 77) {
            return R.color.color18;
        }
        if (n == 18 || n == 38 || n == 58 || n == 78) {
            return R.color.color19;
        }
        if (n == 19 || n == 39 || n == 59 || n == 79) {
            return R.color.color20;
        }
        return R.color.color1;
    }

}
