package com.game.xogame.views.create;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.game.xogame.R;

public class CreateAuditoryActivity extends AppCompatActivity {

    String[] cityList = {"Білгород-Дністровський", "Бердянськ", "Берегове", "Буковель", "Вінниця", "Верховина", "Вилкове", "Ворохта", "Дніпро", "Донецьк", "Житомир", "Запоріжжя", "Затока", "Івано-Франківськ", "Кам'янець-подільский", "Київ", "Кирилівка", "Клеван", "Коблево", "Коломия", "Кривий Ріг", "Кропивницький", "Луцьк", "Львів", "Миколаїв", "Мукачево", "Одеса", "Пилипець", "Полтава", "Припять", "Славське", "Тернопіль", "Трускавець", "Ужгород", "Умань", "Харків", "Херсон", "Хмельницький", "Хотин", "Черкаси", "Чернівці", "Чернігів", "Чорнобиль", "Яремче",};
    String[] countryList = {"Україна"};
    private ImageView back;
    private Spinner country;
    private Spinner city;
    private EditText oblast;
    private EditText district;
    private EditText street;
    private EditText house;
    private SeekBar radius;
    private ImageView save;
    private TextView km;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_auditory);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init(){
        oblast = findViewById(R.id.editText3);
        district = findViewById(R.id.editText4);
        street = findViewById(R.id.editText5);
        house = findViewById(R.id.editText6);

        // адаптер
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner, countryList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner, cityList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        country = findViewById(R.id.editText1);
        city = findViewById(R.id.editText2);
        country.setAdapter(adapter1);
        city.setAdapter(adapter2);
        // заголовок
        //spinner.setPrompt("Title");
        // выделяем элемент
        //spinner.setSelection(2);

        back = findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        save = findViewById(R.id.button4);
        save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        save.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        save.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        if(!country.getSelectedItem().toString().equals("") && !city.getSelectedItem().toString().equals("")) {
                            Intent intent = new Intent(CreateAuditoryActivity.this, CreateGameActivity.class);
                            intent.putExtra("country",country.getSelectedItem().toString()+"");
                            intent.putExtra("city",city.getSelectedItem().toString()+"");
                            intent.putExtra("oblast",oblast.getText().toString()+"");
                            intent.putExtra("district",district.getText().toString()+"");
                            intent.putExtra("street",street.getText().toString()+"");
                            intent.putExtra("house",house.getText().toString()+"");
                            intent.putExtra("radius",radius.getProgress()+"");
                            startActivity(intent);
                            finish();
                        }else{
                            showToast(getString(R.string.activityCreateAuditory_enter));
                        }
                        break;
                }
                return true;
            }
        });

        km = findViewById(R.id.textView8);
        radius = findViewById(R.id.seekBar);
        radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                km.setText(progress+" km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void showToast(String s) {
        LayoutInflater layoutInflater = LayoutInflater.from(CreateAuditoryActivity.this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(this).create();
        TextView btnAdd1 = promptView.findViewById(R.id.textView1);
        btnAdd1.setText(s);
        alertD.setView(promptView);
        alertD.show();

    }
}
