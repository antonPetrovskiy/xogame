package com.game.xogame.views.create;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.CreateGameModel;
import com.game.xogame.presenters.DatePresenter;

import java.util.Calendar;
import java.util.Objects;


public class DateActivity extends AppCompatActivity {
    public ApiService api;
    private DatePresenter presenter;
    public ImageView back;
    public ImageView save;
    public RelativeLayout startDay;
    public RelativeLayout endDay;
    public RelativeLayout startTime;
    public RelativeLayout endTime;
    public TextView textStartDay;
    public TextView textEndDay;
    public TextView textStartTime;
    public TextView textEndTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener1;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener1;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
    }

    public void init(){
        api = RetroClient.getApiService();
        final CreateGameModel model = new CreateGameModel(api, getApplicationContext());
        presenter = new DatePresenter(model);
        presenter.attachView(this);
        textStartDay = findViewById(R.id.text2);
        textEndDay = findViewById(R.id.text4);
        textStartTime = findViewById(R.id.text7);
        textEndTime = findViewById(R.id.text9);
        back = findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        save = findViewById(R.id.imageView0);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setDateGame(getIntent().getStringExtra("gameid"),textStartDay.getText().toString(),textEndDay.getText().toString(),textStartTime.getText().toString(),textEndTime.getText().toString());
            }
        });
        startDay = findViewById(R.id.relativeLayout);
        startDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date;
                        if (month < 10) {
                            date = day + ".0" + month + "." + year;
                        } else {
                            date = day + "." + month + "." + year;
                        }
                        textStartDay.setText(date);
                    }
                };
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener1,
                        year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        endDay = findViewById(R.id.relativeLayout1);
        endDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date;
                        if (month < 10) {
                            date = day + ".0" + month + "." + year;
                        } else {
                            date = day + "." + month + "." + year;
                        }
                        textEndDay.setText(date);
                    }
                };
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        startTime = findViewById(R.id.relativeLayout2);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String date;
                        if (hourOfDay < 10) {
                            date = "0" + hourOfDay + ":";
                        } else {
                            date = "" + hourOfDay + ":";
                        }
                        if (minute < 10) {
                            date += "0" + minute;
                        } else {
                            date += "" + minute;
                        }
                        textStartTime.setText(date);
                    }
                };
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        DateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener1,
                        hour, minute, true);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        endTime = findViewById(R.id.relativeLayout3);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String date;
                        if (hourOfDay < 10) {
                            date = "0" + hourOfDay + ":";
                        } else {
                            date = "" + hourOfDay + ":";
                        }
                        if (minute < 10) {
                            date += "0" + minute;
                        } else {
                            date += "" + minute;
                        }
                        textEndTime.setText(date);
                    }
                };
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        DateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener2,
                        hour, minute, true);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    public void success(){
        finish();
    }

    public void showToast(String s) {
        //load.setVisibility(View.GONE);
        LayoutInflater layoutInflater = LayoutInflater.from(DateActivity.this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(this).create();
        TextView btnAdd1 = promptView.findViewById(R.id.textView1);
        btnAdd1.setText(s);
        alertD.setView(promptView);
        alertD.show();

    }
}
