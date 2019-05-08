package com.game.xogame.views.authentication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.LoginModel;
import com.game.xogame.presenters.EditInfoPresenter;
import com.game.xogame.views.main.MainActivity;

import java.io.File;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


public class EditInfoActivity extends AppCompatActivity {

    Button next;
    private ImageView photo_view;
    private EditText name_view;
    private EditText email_view;
    public static final int REQ_CODE_PICK_PHOTO = 0;
    public ApiService api;
    private EditInfoPresenter presenter;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init(){
        api = RetroClient.getApiService();
        LoginModel usersModel = new LoginModel(api, getApplicationContext());
        presenter = new EditInfoPresenter(usersModel);
        presenter.attachEditInfoView(this);


        next = findViewById(R.id.button4);
        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        next.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        next.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        if(!getName().equals("")) {
                            presenter.editInfo();
                        }else{
                            showToast("Введите никнейм");
                        }
                        break;
                }
                return true;
            }
        });

        photo_view = findViewById(R.id.imageView);
        photo_view.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(intent,  REQ_CODE_PICK_PHOTO);
            }
        });

        name_view = findViewById(R.id.editText);
        email_view = findViewById(R.id.editText2);


    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_PICK_PHOTO && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){
                imagePath = getFilePath(data);
                File file = new File(imagePath);
                setPhoto(file);

            }
        }

    }

    public void toMainActivity(){
        Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    public String getName(){
        return name_view.getText().toString()+"";
    }

    public String getEmail(){
        return email_view.getText().toString()+"";
    }

    public String getPhoto(){
        return imagePath+"";
    }

    public void setPhoto(File file){
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        photo_view.setImageBitmap(myBitmap);
    }

    public void showToast(String s){
        Toast.makeText(getApplicationContext(), s,
                Toast.LENGTH_LONG).show();
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
}
