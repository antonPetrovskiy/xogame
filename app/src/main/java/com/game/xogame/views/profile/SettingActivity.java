package com.game.xogame.views.profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.Objects;

public class SettingActivity extends AppCompatActivity {

    private EditText name;
    private EditText gender;
    private EditText age;
    private EditText email;
    private EditText country;
    private EditText city;
    private EditText card;
    public ScrollView main;

    public TextView exit;
    private TextView rules;
    private TextView support;
    private TextView rateus;
    private LinearLayout load;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public ImageView save;
    public ImageView back;

    ApiService api;
    SettingPresenter presenter;


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
        card = findViewById(R.id.editText8);
        save = findViewById(R.id.imageView2);
        back = findViewById(R.id.imageView1);
        load = findViewById(R.id.targetView);
        exit = findViewById(R.id.textView14);
        rules = findViewById(R.id.textView16);
        support = findViewById(R.id.textView13);
        rateus = findViewById(R.id.textView15);
        main = findViewById(R.id.all);

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
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/auser" + sharedPref.getString("userid","null"))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        alertD.cancel();
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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paparazzi.games/contact-us.php")));
            }
        });

        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://paparazzi.games/Rules.html")));
            }
        });

    }

    public void onBack() {
        super.onBackPressed();
    }

    public LinearLayout getLoadView() {
        return load;
    }


    public String getName() {
        return name.getText().toString();
    }

    public String getGender() {
        return gender.getText().toString();
    }

    public String getAge() {
        return age.getText().toString();
    }

    public String getEmail() {
        return email.getText().toString();
    }

    public String getCountry() {
        return country.getText().toString();
    }

    public String getCity() {
        return city.getText().toString();
    }

    public String getCard() {
        return card.getText().toString();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

//    public void setInfo(String info) {
//        this.info.setText(info);
//    }

    public void setGender(String gender) {
        this.gender.setText(gender);
    }

    public void setAge(String age) {
        this.age.setText(age);
    }

    public void setEmail(String email) {
        this.email.setText(email);
    }

    public void setCountry(String country) {
        this.country.setText(country);
    }

    public void setCity(String city) {
        this.city.setText(city);
    }

    public void setCard(String card) {
        this.card.setText(card);
    }
}
