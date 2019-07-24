package com.game.xogame.views.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.game.xogame.R;

import java.util.ArrayList;

public class CreateCategoryActivity extends AppCompatActivity {

    public ArrayList<String> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        init();

    }

    public void init(){
        ListView list = findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CreateCategoryActivity.this, CreateGameActivity.class);
                intent.putExtra("category",arr.get(position));
                intent.putExtra("categoryID",position+"");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        arr = new ArrayList<>();
        arr.add("Комиксы");
        arr.add("Еда");
        arr.add("Стиль");
        arr.add("Игры");
        arr.add("Архитектура");
        arr.add("ТВ и фильмы");
        arr.add("Юмор");
        arr.add("Декор");
        arr.add("Природа");
        arr.add("Наука");
        arr.add("Искуство");
        arr.add("Красота");
        arr.add("Пропала собачка");
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,R.layout.spinner, arr);
        list.setAdapter(adapter);


        ImageView back = findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}

