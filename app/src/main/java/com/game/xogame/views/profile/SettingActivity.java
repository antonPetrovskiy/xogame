package com.game.xogame.views.profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.presenters.SettingPresenter;
import com.game.xogame.views.authentication.LoginActivity;
import com.game.xogame.views.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.Objects;

public class SettingActivity extends AppCompatActivity {
    public static MainActivity activity;
    public ScrollView main;
    public TextView exit;
    public ImageView save;
    public ImageView back;
    ApiService api;
    SettingPresenter presenter;
    private EditText name;
    private EditText gender;
    private EditText age;
    private EditText email;
    private EditText country;
    private EditText city;
    private TextView rules;
    private TextView support;
    private TextView rateus;
    private LinearLayout load;
    private WebView web;
    public TextView confirm;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();

        api = RetroClient.getApiService();
        UserInfoModel usersModel = new UserInfoModel(api, getApplicationContext());
        presenter = new SettingPresenter(usersModel);
        presenter.attachSettingView(this);

        presenter.showUserInfo();
    }

    public void init() {
        name = findViewById(R.id.editText1);
        gender = findViewById(R.id.editText3);
        age = findViewById(R.id.editText4);
        email = findViewById(R.id.editText5);
        country = findViewById(R.id.editText6);
        city = findViewById(R.id.editText7);
        save = findViewById(R.id.imageView2);
        back = findViewById(R.id.imageView1);
        load = findViewById(R.id.targetView);
        exit = findViewById(R.id.textView14);
        rules = findViewById(R.id.textView16);
        support = findViewById(R.id.textView13);
        rateus = findViewById(R.id.textView15);
        main = findViewById(R.id.all);
        web = findViewById(R.id.web);
        confirm = findViewById(R.id.textView00);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((email.getText().toString()+"").equals("") || !email.getText().toString().contains("@") || !email.getText().toString().contains(".")) {
                    error(getString(R.string.activitySetting_wrongcard));
                } else {
                    presenter.sentVerify();
                    getLoadView().setVisibility(View.VISIBLE);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    presenter.editInfo();
                    getLoadView().setVisibility(View.VISIBLE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(SettingActivity.this);
                @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.popup_genderchooser, null);
                final AlertDialog alertD = new AlertDialog.Builder(SettingActivity.this).create();

                TextView btnAdd1 = promptView.findViewById(R.id.textView3);
                TextView btnAdd2 = promptView.findViewById(R.id.textView2);

                btnAdd1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        gender.setText(getString(R.string.popupGenderChooser_man));
                        alertD.cancel();
                    }
                });

                btnAdd2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        gender.setText(getString(R.string.popupGenderChooser_woman));
                        alertD.cancel();
                    }
                });

                alertD.setView(promptView);
                alertD.show();
            }
        });

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date;
                        if (month < 10) {
                            date = day + ".0" + month + "." + year;
                        } else {
                            date = day + "." + month + "." + year;
                        }

                        age.setText(date);
                    }
                };

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SettingActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 568036800000L);
                dialog.show();
            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(SettingActivity.this);
                @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.popup_genderchooser, null);
                final AlertDialog alertD = new AlertDialog.Builder(SettingActivity.this).create();
                TextView title = promptView.findViewById(R.id.textView1);
                TextView btnAdd1 = promptView.findViewById(R.id.textView3);
                TextView btnAdd2 = promptView.findViewById(R.id.textView2);
                title.setText(getString(R.string.popupSignout_signout));
                btnAdd1.setText(getString(R.string.popupSignout_yes));
                btnAdd2.setText(getString(R.string.popupSignout_no));

                btnAdd1.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ApplySharedPref")
                    public void onClick(View v) {
                        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
                        sharedPref.edit().putString("token", "null").commit();
                        sharedPref.edit().putString("lat", "null").commit();
                        sharedPref.edit().putString("lng", "null").commit();
                        sharedPref.edit().putString("tutorial_guide", "false").commit();

//                        sharedPref.edit().putString("tutorial_games", "true").commit();
//                        sharedPref.edit().putString("tutorial_game", "true").commit();
//                        sharedPref.edit().putString("tutorial_feeds", "true").commit();
//                        sharedPref.edit().putString("tutorial_money", "true").commit();
//                        sharedPref.edit().putString("tutorial_profile", "true").commit();

                        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/auser" + sharedPref.getString("userid", "null"))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/anews")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        alertD.cancel();
                        if(activity!=null)
                            activity.finish();
                        finish();
                    }
                });

                btnAdd2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertD.cancel();
                    }
                });

                alertD.setView(promptView);
                alertD.show();
            }
        });

        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.game.paparazzi")));
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.setVisibility(View.VISIBLE);
                switch (getResources().getConfiguration().locale.getLanguage()) {
                    case "ru":
                        web.loadUrl("https://paparazzi.games/info/ru/feedback.php");
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paparazzi.games/lang/ru/contact-us.php")));
                        break;
                    case "uk":
                        web.loadUrl("https://paparazzi.games/info/ua/feedback.php");
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paparazzi.games/lang/ua/contact-us.php")));
                        break;
                    case "en":
                        web.loadUrl("https://paparazzi.games/info/en/feedback.php");
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paparazzi.games/lang/en/contact-us.php")));
                        break;
                    default:
                        web.loadUrl("https://paparazzi.games/info/en/feedback.php");
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paparazzi.games/lang/en/contact-us.php")));
                        break;
                }
            }
        });

        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.setVisibility(View.VISIBLE);
                switch (getResources().getConfiguration().locale.getLanguage()) {
                    case "ru":
                        web.loadUrl("https://paparazzi.games/info/ru/game-rules.html");
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paparazzi.games/lang/ru/Rules.html")));
                        break;
                    case "uk":
                        web.loadUrl("https://paparazzi.games/info/ua/game-rules.html");
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paparazzi.games/lang/ua/Rules.html")));
                        break;
                    case "en":
                        web.loadUrl("https://paparazzi.games/info/en/game-rules.html");
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paparazzi.games/lang/en/Rules.html")));
                        break;
                    default:
                        web.loadUrl("https://paparazzi.games/info/en/game-rules.html");
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paparazzi.games/lang/en/Rules.html")));
                        break;
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    public void onBack() {
        if(web.getVisibility()==View.GONE){
            super.onBackPressed();
        }else {
            web.setVisibility(View.GONE);
        }
    }

    public LinearLayout getLoadView() {
        return load;
    }


    public String getName() {
        return name.getText().toString();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public String getGender() {
        return gender.getText().toString();
    }

    public void setGender(String gender) {
        this.gender.setText(gender);
    }

    public String getAge() {
        return age.getText().toString();
    }

    public void setAge(String age) {
        this.age.setText(age);
    }

    public String getEmail() {
        return email.getText().toString();
    }

    public void setEmail(String email) {
        this.email.setText(email);
    }

    public String getCountry() {
        return country.getText().toString();
    }

    public void setCountry(String country) {
        this.country.setText(country);
    }

    public String getCity() {
        return city.getText().toString();
    }

    public void setCity(String city) {
        this.city.setText(city);
    }

    public void error(String s) {
        LayoutInflater layoutInflater = LayoutInflater.from(SettingActivity.this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        TextView tw = promptView.findViewById(R.id.textView1);
        tw.setText(s);
        alertD.setView(promptView);
        alertD.show();
    }
}
