package com.game.xogame.views.create;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.adapter.CreateTaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity {

    private ImageView back;
    private ImageView info;
    private LinearLayout plus;
    private TextView task;
    private TextView money;
    private ImageView save;
    private ArrayList<String> list;
    private CreateTaskAdapter viewPagerAdapter;
    private ListView taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
    }

    public void init(){
        taskList = findViewById(R.id.tasklist);
        task = findViewById(R.id.countTask);
        money = findViewById(R.id.countMoney);
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
                for(int i = 0; i < list.size(); i ++){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(save.getWindowToken(), 0);
                    if(list.get(i).equals("")){
                        showToast(getString(R.string.activityCreateTask_fillAll));
                        return;
                    }
                }
                Intent intent = new Intent(CreateTaskActivity.this, CreateGameActivity.class);
                intent.putStringArrayListExtra("tasks",list);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        plus = findViewById(R.id.imageViewPlus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTaskAdapter.itemList.add("");
                //list.add("");
                setList(CreateTaskAdapter.itemList);
            }
        });
        list = new ArrayList<>();
        list.add("");

        if(getIntent().getStringArrayListExtra("list")!=null){
            list=getIntent().getStringArrayListExtra("list");
        }
        setList(list);
    }

    public List<String> getList(){
        return list;
    }

    public void setList(List<String> list) {
        if(viewPagerAdapter==null) {
            viewPagerAdapter = new CreateTaskAdapter(this, list);
        }
        viewPagerAdapter.notifyDataSetChanged();
        taskList.setAdapter(viewPagerAdapter);
        money.setText((5*list.size())+" $");
        task.setText(list.size()+"");
    }

    public void showToast(String s) {
        LayoutInflater layoutInflater = LayoutInflater.from(CreateTaskActivity.this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(this).create();
        TextView btnAdd1 = promptView.findViewById(R.id.textView1);
        btnAdd1.setText(s);
        alertD.setView(promptView);
        alertD.show();

    }
}
