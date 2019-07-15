package com.game.xogame.views.create;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private static String imagePath;
    private static EditText name;
    private static EditText description;
    private static String country;
    private static String city;
    private static String oblast;
    private static String district;
    private static String street;
    private static String house;
    private static String radius;
    private static ArrayList<String> list;
    final int PIC_CROP = 1;
    public ApiService api;
    private CreateGamePresenter presenter;
    private ImageView back;
    private ImageView save;
    private TextView info;
    private ImageView photo;
    private TextView auditory;
    private TextView gameInfo;
    private TextView tasks;
    private TextView end;
    private RelativeLayout addAuditory;
    private RelativeLayout addTask;
    private LinearLayout auditoryLay;
    private LinearLayout taskLay;
    private TextView auditoryText;
    private TextView taskText;
    private TextView rewardText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("country")!=null && !extras.getString("country").equals(""))
                country = extras.getString("country");
            if(extras.getString("city")!=null && !extras.getString("city").equals(""))
                city = extras.getString("city");
            if(extras.getString("oblast")!=null && !extras.getString("oblast").equals(""))
                oblast = extras.getString("oblast");
            if(extras.getString("district")!=null && !extras.getString("district").equals(""))
                district = extras.getString("district");
            if(extras.getString("street")!=null && !extras.getString("street").equals(""))
                street = extras.getString("street");
            if(extras.getString("house")!=null && !extras.getString("house").equals(""))
                house = extras.getString("house");
            if(extras.getString("radius")!=null && !extras.getString("radius").equals(""))
                radius = extras.getString("radius");
            if(extras.getStringArrayList("tasks")!=null && extras.getStringArrayList("tasks").size()!=0)
                list = extras.getStringArrayList("tasks");
        }
    }

    @Override
    protected void onResume() {
        String auditory = "";
        if(street!=null)
            auditory+=street;
        if(house!=null) {
            auditory += (" " + house + ", ");
        }else if(!auditory.equals("")){
            auditory += ", ";
        }
        if(district!=null)
            auditory+=(district + ", ");
        if(oblast!=null)
            auditory+=(oblast + ", ");
        if(city!=null)
            auditory+=(city + ", ");
        if(country!=null)
            auditory+=country;

        if(country!=null && !country.equals("")){
            auditoryLay.setVisibility(View.VISIBLE);
            auditoryText.setText(auditory);
        }

        if(list!=null && list.size()>0){
            taskLay.setVisibility(View.VISIBLE);
            taskText.setText(list.size()+" заданий");
            rewardText.setText((list.size()*100)+" грн");
        }


        super.onResume();
    }

    public void init(){
        api = RetroClient.getApiService();
        final CreateGameModel model = new CreateGameModel(api, getApplicationContext());
        presenter = new CreateGamePresenter(model);

        list = new ArrayList<>();
        presenter.attachView(this);
        auditoryLay = findViewById(R.id.layAuditory);
        taskLay = findViewById(R.id.layTask);
        auditoryText = findViewById(R.id.textViewAuditory);
        taskText = findViewById(R.id.textViewTasks);
        rewardText = findViewById(R.id.textViewReward);
        name = findViewById(R.id.editText1);
        description = findViewById(R.id.editText2);

        back = findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addAuditory = findViewById(R.id.addAuditory);
        addAuditory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGameActivity.this, CreateAuditoryActivity.class);
                startActivity(intent);
            }
        });
        addTask = findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGameActivity.this, CreateTaskActivity.class);
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
                startActivityForResult(intent, REQ_CODE_PICK_PHOTO);
            }
        });
        list.add("Напиши по поводу зарплаты");
        list.add("Позвони по поводу зарплаты");
        list.add("Скажи по поводу зарплаты");
        save = findViewById(R.id.button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(city!=null && country!=null && !name.getText().toString().equals("") && !description.getText().toString().equals("") && imagePath!=null && list.size()>0) {
                    String[] arr = new String[list.size()];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = list.get(i);
                    }
                    String cityId;
                    switch (city) {
                        case "Київ":
                            cityId = "1";
                            break;
                        default:
                            cityId = "1";
                            break;
                    }
                    presenter.createGame(name.getText().toString(), description.getText().toString(), imagePath, arr, "5", cityId, (street + " " + house), radius);
                }else{
                    showToast("Заполните всю информацию");
                }
            }
        });
    }

    public void refreshAll(){

    }

    private void performCrop(Uri picUri) {


// start cropping activity for pre-acquired image saved on the device
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

        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");

                photo.setImageBitmap(selectedBitmap);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                photo.setImageURI(resultUri);
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
        LayoutInflater layoutInflater = LayoutInflater.from(CreateGameActivity.this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(this).create();
        TextView btnAdd1 = promptView.findViewById(R.id.textView1);
        btnAdd1.setText(s);
        alertD.setView(promptView);
        alertD.show();

    }

}
