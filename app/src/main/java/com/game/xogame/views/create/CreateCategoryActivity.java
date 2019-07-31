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
        arr.add(getString(R.string.category_food));
        arr.add(getString(R.string.category_style));
        arr.add(getString(R.string.category_art));
        arr.add(getString(R.string.category_tv));
        arr.add(getString(R.string.category_fun));
        arr.add(getString(R.string.category_decor));
        arr.add(getString(R.string.category_nature));
        arr.add(getString(R.string.category_since));
        arr.add(getString(R.string.category_iscustvo));
        arr.add(getString(R.string.category_beauty));
        arr.add(getString(R.string.category_sport));
        arr.add(getString(R.string.category_texture));
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

