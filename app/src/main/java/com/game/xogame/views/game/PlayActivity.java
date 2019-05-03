package com.game.xogame.views.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.xogame.GamePush;
import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.PlayModel;
import com.game.xogame.presenters.EditInfoPresenter;
import com.game.xogame.presenters.PlayPresenter;
import com.game.xogame.views.authentication.ConfirmPhoneActivity;
import com.game.xogame.views.authentication.EditInfoActivity;
import com.game.xogame.views.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class PlayActivity extends AppCompatActivity {
    private ApiService api;
    private PlayPresenter presenter;

    //1st screen
    private TextView title;
    private TextView task;
    private TextView number;
    private TextView time;
    private ImageView logo;
    private ImageView camera;
    private CardView card;
    private LinearLayout buttons;
    long sec;
    String path;
    String current;
    String done;
    //2nd screen
    private RelativeLayout screen2;
    private TextView time2;
    private ImageView photo;
    private EditText tags;
    private Button send;
    public CountDownTimer timer;

    private String taskId;
    private String imagePath;
    File directory;
    File myFile;
    private LinearLayout load;
    public static final int REQ_CODE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        Log.i("WTF", "time1 "+extras.getString("TIME"));
        Log.i("WTF", "time2 "+Long.parseLong(extras.getString("TIME")));
        long currentTime1 = Calendar.getInstance().getTimeInMillis();
        long currentTime2 = Long.parseLong(extras.getString("TIME"));
        sec = currentTime1-currentTime2;


        createDirectory();
        init();

        taskId = extras.getString("TASKID");
        title.setText(extras.getString("TITLE"));
        Picasso.with(this).load(extras.getString("LOGO")).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(logo);
        task.setText(extras.getString("TASK"));
        number.setText(extras.getString("NUMBERTASK")+"/"+extras.getString("TASKS"));
        path = extras.getString("LOGO");


        //sec=Integer.parseInt((currentTime1-currentTime2)+"");
        //task.setText((sec/100)+"");
    }

    public void init(){
        api = RetroClient.getApiService();
        PlayModel usersModel = new PlayModel(api, getApplicationContext());
        presenter = new PlayPresenter(usersModel);
        presenter.attachView(this);

        title = findViewById(R.id.textView1);
        task = findViewById(R.id.textView2);
        number = findViewById(R.id.textView3);
        time = findViewById(R.id.textView4);
        logo = findViewById(R.id.imageView1);
        camera = findViewById(R.id.imageView2);
        card = findViewById(R.id.card);
        buttons = findViewById(R.id.buttons);

        screen2 = findViewById(R.id.screen2);
        tags = findViewById(R.id.editText1);
        time2 = findViewById(R.id.textView5);
        photo = findViewById(R.id.imageView3);
        send = findViewById(R.id.imageButton);
        load = findViewById(R.id.targetView);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri());
                startActivityForResult(intent, REQ_CODE_CAPTURE);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load.setVisibility(View.VISIBLE);
                send.setClickable(false);
                presenter.sendTask();
                done = current;
            }
        });

        timer = new CountDownTimer(120000-sec, 1000) {
                public void onTick(long millisUntilFinished) {
                    if(millisUntilFinished<60000) {
                        time.setText("0:" + (millisUntilFinished / 1000));
                        time2.setText("0:" + (millisUntilFinished / 1000) + "");
                    }else{
                        time.setText("1:" + ((millisUntilFinished / 1000)-60));
                        time2.setText("1:" + ((millisUntilFinished / 1000)-60) + "");
                    }
                    long temp = (120000-millisUntilFinished)/1000;
                    current = temp+"";
                }

                public void onFinish() {
                    //if(current == null)
                    time.setText("0:00");
                    time2.setText("0:00");
                    toMainActivityLose();
                }
            }.start();
    }

    public String getImage(){
        return imagePath+"";
    }

    public String getComment(){
        return tags.getText()+"";
    }

    public String getTaskid(){
        return taskId+"";
    }

    public String getTasktime(){
        return current+"";
    }

    public void toMainActivityLose(){
        load.setVisibility(View.GONE);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.error, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(this).create();

        TextView btnAdd1 = promptView.findViewById(R.id.textView1);
        btnAdd1.setText("Время вышло");
        alertD.setView(promptView);
        alertD.show();
        alertD.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    public void toMainActivityWin(){
        load.setVisibility(View.GONE);
        LayoutInflater layoutInflater = LayoutInflater.from(PlayActivity.this);
        View promptView = layoutInflater.inflate(R.layout.popup_taskdone, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        ImageView image = promptView.findViewById(R.id.imageView);
        TextView title = promptView.findViewById(R.id.textView1);
        TextView time = promptView.findViewById(R.id.textView2);
        TextView position = promptView.findViewById(R.id.textView3);
        TextView task = promptView.findViewById(R.id.textView4);
        title.setText(title.getText()+"");
        task.setText(task.getText()+"");
        if(Integer.parseInt(current)<60){
            time.setText("0:"+current);
        }else{
            int n = Integer.parseInt(current)-60;
            time.setText("1:"+n);
        }
        Picasso.with(this).load(path).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(image);

        alertD.setView(promptView);
        alertD.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        alertD.show();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_CAPTURE && resultCode == Activity.RESULT_OK) {

            ExifInterface ei = null;
            try {
                ei = new ExifInterface(myFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(myFile.getAbsolutePath(), options);
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            try (FileOutputStream out = new FileOutputStream(myFile)) {
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap myBitmap = BitmapFactory.decodeFile(myFile.getAbsolutePath());
            screen2.setVisibility(View.VISIBLE);
            card.setVisibility(View.GONE);
            buttons.setVisibility(View.GONE);

            photo.setImageBitmap(myBitmap);
            imagePath = myFile.getAbsolutePath();
            //presenter.editPhoto(this);
        }
    }

    private Uri generateFileUri() {
        File file = null;
        file = new File(directory.getPath() + "/" + "photo_" + System.currentTimeMillis() + ".jpg");
        Log.i("PHOTO", "fileName = " + file);
        myFile = file;
        return Uri.fromFile(file);
    }

    private void createDirectory() {
        directory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyFolder");
        if (!directory.exists())
            directory.mkdirs();
    }
}
