package com.game.xogame.views.create;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.game.xogame.R;

import java.util.ArrayList;

public class ModeratedActivity extends AppCompatActivity {

    private String imagePath;
    private String nameStr = "";
    private String descriptionStr = "";
    private String lat;
    private String lng;
    private String street;
    private String radius;
    private String category;
    private ArrayList<String> list;

    private ImageView back;

    private ImageView photo;
    private EditText name;
    private EditText description;
    private TextView categoryText;
    private TextView auditoryText;
    private TextView taskText;
    private TextView rewardText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderated);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        list = new ArrayList<>();
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
                list = extras.getStringArrayList("tasks");
            if(extras.getString("category")!=null && !extras.getString("category").equals(""))
                category = extras.getString("category");
            if(extras.getString("name")!=null && !extras.getString("name").equals(""))
                nameStr = extras.getString("name");
            if(extras.getString("description")!=null && !extras.getString("description").equals(""))
                descriptionStr = extras.getString("description");
            if(extras.getString("photo")!=null && !extras.getString("photo").equals(""))
                imagePath = extras.getString("photo");
        }

        init();
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

        name.setText(nameStr+"");
        description.setText(descriptionStr+"");
        auditoryText.setText(street+"");
        categoryText.setText(category+"");
        taskText.setText(list.size()+" заданий");
        rewardText.setText((list.size()*5)+" $");

        if(imagePath!=null && !imagePath.equals("")){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.unknow_wide);
            requestOptions.error(R.drawable.unknow_wide);
            requestOptions.centerCrop();
            Glide.with(this).setDefaultRequestOptions(requestOptions).load(imagePath+"").thumbnail(0.3f).into(photo);
        }
    }

}
