package com.game.xogame.views.create;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.game.xogame.R;
import com.game.xogame.adapter.CreateTaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class ModeratedActivity extends AppCompatActivity {

    private String imagePath;
    private String nameStr = "";
    private String descriptionStr = "";
    private String lat;
    private String lng;
    private String street;
    private String radius;
    private String category;
    private ArrayList<String> taskList;
    private ListView listView;
    private CreateTaskAdapter adapter;
    private LinearLayout auditoryLay;
    private LinearLayout taskLay;
    private LinearLayout categoryLay;

    private ImageView back;

    private ImageView photo;
    private EditText name;
    private EditText description;
    private TextView categoryText;
    private TextView auditoryText;
    private TextView taskText;
    private TextView rewardText;

    private TextView badName;
    private TextView badPhoto;
    private TextView badDescription;
    private ImageView badTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderated);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        taskList = new ArrayList<>();
        listView = findViewById(R.id.tasklist);
        init();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("street")!=null && !extras.getString("street").equals(""))
                street = extras.getString("street");
            if(extras.getString("lat")!=null && !extras.getString("lat").equals(""))
                lat = extras.getString("lat");
            if(extras.getString("lng")!=null && !extras.getString("lng").equals(""))
                lng = extras.getString("lng");
            if(extras.getString("radius")!=null && !extras.getString("radius").equals(""))
                radius = extras.getString("radius");
            if(extras.getStringArrayList("tasks")!=null && extras.getStringArrayList("tasks").size()!=0)
                taskList = extras.getStringArrayList("tasks");
            if(extras.getString("category")!=null && !extras.getString("category").equals(""))
                category = extras.getString("category");
            if(extras.getString("name")!=null && !extras.getString("name").equals(""))
                nameStr = extras.getString("name");
            if(extras.getString("description")!=null && !extras.getString("description").equals(""))
                descriptionStr = extras.getString("description");
            if(extras.getString("photo")!=null && !extras.getString("photo").equals(""))
                imagePath = extras.getString("photo");

            if(extras.getString("type")!=null && extras.getString("type").equals("canceled")){
                if(extras.getString("badName")!=null && extras.getString("badName").equals("true"))
                    badName.setVisibility(View.VISIBLE);
                if(extras.getString("badPhoto")!=null && extras.getString("badPhoto").equals("true"))
                    badPhoto.setVisibility(View.VISIBLE);
                if(extras.getString("badDescription")!=null && extras.getString("badDescription").equals("true"))
                    badDescription.setVisibility(View.VISIBLE);
                if(extras.getString("badTasks")!=null && extras.getString("badTasks").equals("true"))
                    badTasks.setVisibility(View.VISIBLE);
            }
        }

        setList(taskList);
    }

    @Override
    protected void onResume() {
        name.setText(nameStr+"");
        description.setText(descriptionStr+"");

        if(imagePath!=null && !imagePath.equals("")){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.unknow_wide);
            requestOptions.error(R.drawable.unknow_wide);
            requestOptions.centerCrop();
            Glide.with(this).setDefaultRequestOptions(requestOptions).load(imagePath+"").thumbnail(0.3f).into(photo);
        }

        if(street!=null && !street.equals("")){
            auditoryLay.setVisibility(View.VISIBLE);
            auditoryText.setText(street);
        }

        if(taskList!=null && taskList.size()>0){
            taskLay.setVisibility(View.VISIBLE);
            taskText.setText(taskList.size()+" заданий");
            rewardText.setText((taskList.size()*5)+" $");
        }

        if(category!=null && !category.equals("")){
            categoryLay.setVisibility(View.VISIBLE);
            categoryText.setText(category);
        }



        super.onResume();
    }

    public void init(){
        back = findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        auditoryText = findViewById(R.id.textViewAuditory);
        taskText = findViewById(R.id.textViewTasks);
        categoryText = findViewById(R.id.textViewCategory);
        rewardText = findViewById(R.id.textViewReward);
        name = findViewById(R.id.editText1);
        description = findViewById(R.id.editText2);
        photo = findViewById(R.id.imageView1);
        auditoryLay = findViewById(R.id.layAuditory);
        taskLay = findViewById(R.id.layTask);
        categoryLay = findViewById(R.id.layCategory);

        name.setText(nameStr+"");
        description.setText(descriptionStr+"");
        auditoryText.setText(street+"");
        categoryText.setText(category+"");
        taskText.setText(taskList.size()+" заданий");
        rewardText.setText((taskList.size()*5)+" $");

        badName = findViewById(R.id.textView11);
        badPhoto = findViewById(R.id.textView12);
        badDescription = findViewById(R.id.textView13);
        badTasks = findViewById(R.id.imageViewError3);

        if(imagePath!=null && !imagePath.equals("")){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.unknow_wide);
            requestOptions.error(R.drawable.unknow_wide);
            requestOptions.centerCrop();
            Glide.with(this).setDefaultRequestOptions(requestOptions).load(imagePath+"").thumbnail(0.3f).into(photo);
        }
    }

    public void setList(List<String> list) {
        if(adapter==null) {
            adapter = new CreateTaskAdapter(this, list);
        }
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

}
