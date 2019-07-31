package com.game.xogame.views.create;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.CreateGameModel;
import com.game.xogame.presenters.CreateGamePresenter;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class CreateGameActivity extends AppCompatActivity {
    public static final int REQ_CODE_PICK_PHOTO = 0;
    private static String nameStr = "";
    private static String imagePath;
    private static EditText name;
    private static EditText description;
    private static String descriptionStr = "";
    private static String categoryID = "";
    private static String lat;
    private static String lng;
    private static String category;
    private static String street;
    private static String radius;
    public String gameid;
    private static ArrayList<String> list = new ArrayList<>();
    public ApiService api;
    private CreateGamePresenter presenter;
    private ImageView back;
    private Button pay;
    private ImageView save;
    private TextView info;
    private ImageView photo;
    private RelativeLayout addAuditory;
    private RelativeLayout addTask;
    private RelativeLayout addCategory;
    private LinearLayout auditoryLay;
    private LinearLayout taskLay;
    private LinearLayout categoryLay;
    private TextView categoryText;
    private TextView auditoryText;
    private TextView taskText;
    private TextView rewardText;
    private LinearLayout load;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                list = extras.getStringArrayList("tasks");
            if(extras.getString("category")!=null && !extras.getString("category").equals(""))
                category = extras.getString("category");
            if(extras.getString("categoryID")!=null && !extras.getString("categoryID").equals(""))
                categoryID = extras.getString("categoryID");
            if(extras.getString("name")!=null && !extras.getString("name").equals(""))
                nameStr = extras.getString("name");
            if(extras.getString("description")!=null && !extras.getString("description").equals(""))
                descriptionStr = extras.getString("description");
            if(extras.getString("photo")!=null && !extras.getString("photo").equals(""))
                imagePath = extras.getString("photo");
            if(extras.getString("gameid")!=null && !extras.getString("gameid").equals(""))
                gameid = extras.getString("gameid");

        }
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

        if(list!=null && list.size()>0){
            taskLay.setVisibility(View.VISIBLE);
            taskText.setText(list.size()+" заданий");
            rewardText.setText((list.size()*5)+" $");
        }

        if(category!=null && !category.equals("")){
            categoryLay.setVisibility(View.VISIBLE);
            categoryText.setText(category);
        }



        super.onResume();
    }

    @Override
    public void onBackPressed() {
        back.performClick();
    }

    public void init(){
        api = RetroClient.getApiService();
        final CreateGameModel model = new CreateGameModel(api, getApplicationContext());
        presenter = new CreateGamePresenter(model);

        presenter.attachView(this);
        auditoryLay = findViewById(R.id.layAuditory);
        taskLay = findViewById(R.id.layTask);
        categoryLay = findViewById(R.id.layCategory);
        auditoryText = findViewById(R.id.textViewAuditory);
        taskText = findViewById(R.id.textViewTasks);
        categoryText = findViewById(R.id.textViewCategory);
        rewardText = findViewById(R.id.textViewReward);
        name = findViewById(R.id.editText1);
        description = findViewById(R.id.editText2);
        load = findViewById(R.id.targetView);





        back = findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(CreateGameActivity.this);
                @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.popup_yesno, null);
                final AlertDialog alertD = new AlertDialog.Builder(CreateGameActivity.this).create();
                TextView title = promptView.findViewById(R.id.textView1);
                Button btnAdd1 = promptView.findViewById(R.id.button1);
                Button btnAdd2 = promptView.findViewById(R.id.button2);
                title.setText(getString(R.string.activityCreateGame_delete));
                btnAdd1.setText(getString(R.string.popupSignout_no));
                btnAdd2.setText(getString(R.string.popupSignout_yes));

                btnAdd1.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ApplySharedPref")
                    public void onClick(View v) {
                        alertD.cancel();
                    }
                });

                btnAdd2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        refresh();
                        if(gameid!=null && !gameid.equals("")) {
                            presenter.deleteGame(gameid);
                        }else{
                            end();
                        }
                        alertD.cancel();
                    }
                });

                alertD.setView(promptView);
                alertD.show();
            }
        });
        save = findViewById(R.id.button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(CreateGameActivity.this);
                @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.popup_yesno, null);
                final AlertDialog alertD = new AlertDialog.Builder(CreateGameActivity.this).create();
                TextView title = promptView.findViewById(R.id.textView1);
                Button btnAdd1 = promptView.findViewById(R.id.button1);
                Button btnAdd2 = promptView.findViewById(R.id.button2);
                title.setText(getString(R.string.activityCreateGame_save));
                btnAdd1.setText(getString(R.string.popupSignout_no));
                btnAdd2.setText(getString(R.string.popupSignout_yes));

                btnAdd1.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ApplySharedPref")
                    public void onClick(View v) {
                        alertD.cancel();
                    }
                });

                btnAdd2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertD.cancel();
                        if(street!=null && !street.equals("") && !name.getText().toString().equals("") && !description.getText().toString().equals("") && imagePath!=null && list.size()>0 && lat!=null && !lat.equals("") && categoryID!=null && !categoryID.equals("")) {
                            String[] arr = new String[list.size()];
                            for (int i = 0; i < arr.length; i++) {
                                arr[i] = list.get(i);
                            }
                            load.setVisibility(View.VISIBLE);
                            presenter.createGame(name.getText().toString(), description.getText().toString(), imagePath, arr, lat, lng, street, radius, categoryID, "", false);
                        }else{
                            showToast(getString(R.string.activityCreateGame_fillInfo));
                        }
                    }
                });

                alertD.setView(promptView);
                alertD.show();
            }
        });
        addAuditory = findViewById(R.id.addAuditory);
        addAuditory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameStr = name.getText().toString()+"";
                descriptionStr = description.getText().toString()+"";
                Intent intent = new Intent(CreateGameActivity.this, CreateAuditoryActivity.class);
                startActivity(intent);
            }
        });
        addTask = findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameStr = name.getText().toString()+"";
                descriptionStr = description.getText().toString()+"";
                Intent intent = new Intent(CreateGameActivity.this, CreateTaskActivity.class);
                if(list!=null && list.size()!=0){
                    intent.putStringArrayListExtra("list",list);
                }
                startActivity(intent);
            }
        });
        addCategory = findViewById(R.id.addCategory);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameStr = name.getText().toString()+"";
                descriptionStr = description.getText().toString()+"";
                Intent intent = new Intent(CreateGameActivity.this, CreateCategoryActivity.class);
                startActivity(intent);
            }
        });
        info = findViewById(R.id.imageView0);
        name = findViewById(R.id.editText1);
        description = findViewById(R.id.editText2);
        photo = findViewById(R.id.imageView1);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                Log.i("LOG_photo1" , "-1");
                nameStr = name.getText().toString()+"";
                descriptionStr = description.getText().toString()+"";
                startActivityForResult(intent, REQ_CODE_PICK_PHOTO);
            }
        });

        pay = findViewById(R.id.imageButton);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(street!=null && !street.equals("") && !name.getText().toString().equals("") && !description.getText().toString().equals("") && imagePath!=null && list.size()>0 && lat!=null && !lat.equals("") && categoryID!=null && !categoryID.equals("")) {
                    String[] arr = new String[list.size()];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = list.get(i);
                    }
                    presenter.createGame(name.getText().toString(), description.getText().toString(), imagePath, arr, lat, lng, street, radius, categoryID, gameid, true);
                    load.setVisibility(View.VISIBLE);
                }else{
                    showToast(getString(R.string.activityCreateGame_fillInfo));
                }
            }
        });
    }

    public void end(){
        load.setVisibility(View.GONE);
        nameStr = "";
        descriptionStr = "";
        imagePath = "";
        street = "";
        radius = "";
        lat = "";
        lng = "";
        list = new ArrayList<>();
        finish();
    }

    public void toPay(String url){
        load.setVisibility(View.GONE);
        refresh();
        Intent intent = new Intent(CreateGameActivity.this, PayActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
        finish();
    }

    public void refresh(){
        nameStr = "";
        descriptionStr = "";
        imagePath = "";
        street = "";
        radius = "";
        lat = "";
        lng = "";
        category = "";
        categoryID = "";
        list = new ArrayList<>();
    }

    private void performCrop(Uri picUri) {
        CropImage.activity(picUri).setAspectRatio(16,9).setFixAspectRatio(true)
                .start(CreateGameActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if ((data != null) && (data.getData() != null)) {
                imagePath = getFilePath(data);
                File file = new File(imagePath);
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                try (FileOutputStream out = new FileOutputStream(file)) {
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (IOException e) {
                    e.printStackTrace();
                }
                performCrop(Uri.fromFile(new File(imagePath)));
                //photo.setImageBitmap(myBitmap);
            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                photo.setImageURI(resultUri);
                imagePath = resultUri.getPath();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private String getFilePath(Intent data) {
        String imagePath;
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        assert selectedImage != null;
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        imagePath = cursor.getString(columnIndex);
        cursor.close();

        return imagePath;
    }

    public void showToast(String s) {
        load.setVisibility(View.GONE);
        LayoutInflater layoutInflater = LayoutInflater.from(CreateGameActivity.this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(this).create();
        TextView btnAdd1 = promptView.findViewById(R.id.textView1);
        btnAdd1.setText(s);
        alertD.setView(promptView);
        alertD.show();

    }

}
