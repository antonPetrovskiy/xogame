package com.game.xogame.views.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.exifinterface.media.ExifInterface;

import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.PlayModel;
import com.game.xogame.presenters.PlayPresenter;
import com.game.xogame.views.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class PlayActivity extends AppCompatActivity {
    public ApiService api;
    private PlayPresenter presenter;

    //1st screen
    private TextView title;
    private TextView company;
    private TextView task;
    private TextView number;
    private TextView time;
    private ImageView logo;
    public ImageView camera;
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
    public Animation mEnlargeAnimation;

    private String taskId;
    private String imagePath;
    public String position_str;
    File directory;
    File myFile;
    private LinearLayout load;
    public static final int REQ_CODE_CAPTURE = 2;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        long currentTime1 = Calendar.getInstance().getTimeInMillis();
        long currentTime2 = extras.getLong("TIME");
        sec = currentTime1 - currentTime2;
        Log.i("LOG_play", "time1: " + currentTime1);
        Log.i("LOG_play", "time2: " + currentTime2);
        Log.i("LOG_play", "time: " + sec);
        createDirectory();
        init();

        taskId = extras.getString("TASKID");
        title.setText(extras.getString("TITLE"));
        Picasso.with(this).load(extras.getString("LOGO")).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(logo);
        task.setText(extras.getString("TASK"));
        number.setText(extras.getString("NUMBERTASK") + "/" + extras.getString("TASKS"));
        path = extras.getString("LOGO");
        company.setText(extras.getString("COMPANY"));
    }

    @SuppressLint("SetTextI18n")
    public void init() {
        api = RetroClient.getApiService();
        PlayModel usersModel = new PlayModel(api, getApplicationContext());
        presenter = new PlayPresenter(usersModel);
        presenter.attachView(this);

        title = findViewById(R.id.textView1);
        company = findViewById(R.id.textView6);
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
// подключаем файл анимации
        mEnlargeAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
        send = findViewById(R.id.imageButton);
        // при запуске начинаем с эффекта увеличения
        camera.startAnimation(mEnlargeAnimation);
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
                timer.cancel();
            }
        });


        timer = new CountDownTimer(120000 - sec, 10) {

            public void onTick(long millisUntilFinished) {
                long msec = (millisUntilFinished / 10);
                String s = msec+"";
                if(s.length()>1)
                    s = s.substring(s.length()-2);
                if (millisUntilFinished < 10000) {
                    time.setText("00:0" + (millisUntilFinished / 1000)+"."+s);
                    time2.setText("00:0" + (millisUntilFinished / 1000)+"."+s);
                } else if(millisUntilFinished < 60000){
                    time.setText("00:" + (millisUntilFinished / 1000)+"."+s);
                    time2.setText("00:" + (millisUntilFinished / 1000)+"."+s);
                } else if(millisUntilFinished < 70000){
                    time.setText("01:0" + ((millisUntilFinished / 1000) - 60)+"."+s);
                    time2.setText("01:0" + ((millisUntilFinished / 1000) - 60)+"."+s);
                } else{
                    time.setText("01:" + ((millisUntilFinished / 1000) - 60)+"."+s);
                    time2.setText("01:" + ((millisUntilFinished / 1000) - 60)+"."+s);
                }
                long temp = (120000 - millisUntilFinished);
                current = temp + "";
            }

            public void onFinish() {
                //if(current == null)
                time.setText("00:00.00");
                time2.setText("00:00.00");
                toMainActivityLose();
            }
        }.start();
    }

    public String getImage() {
        return imagePath + "";
    }

    public String getComment() {
        return tags.getText() + "";
    }

    public String getTaskid() {
        return taskId + "";
    }

    public String getTasktime() {
        return current + "";
    }

    public void toMainActivityLose() {
        load.setVisibility(View.GONE);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.fail);
        mp.start();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(this).create();

        TextView btnAdd1 = promptView.findViewById(R.id.textView1);
        btnAdd1.setText(getString(R.string.error_timeIsOut));
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

    @SuppressLint("SetTextI18n")
    public void toMainActivityWin() {
        load.setVisibility(View.GONE);
        LayoutInflater layoutInflater = LayoutInflater.from(PlayActivity.this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.popup_taskdone, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        ImageView image = promptView.findViewById(R.id.imageView);
        TextView title = promptView.findViewById(R.id.textView1);
        TextView time = promptView.findViewById(R.id.textView2);
        TextView position = promptView.findViewById(R.id.textView3);
        TextView task = promptView.findViewById(R.id.textView4);
        TextView company = promptView.findViewById(R.id.textView5);
        title.setText(title.getText() + "");
        company.setText(company.getText() + "");
        position.setText(position_str+" " + getString(R.string.adapterRating_place));
        task.setText(number.getText().toString());
        if (Integer.parseInt(done) < 60) {
            time.setText("0:" + done);
        } else {
            int n = Integer.parseInt(done) - 60;
            time.setText("1:" + n);
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
            assert ei != null;
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(myFile.getAbsolutePath(), options);
            switch (orientation) {
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
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out); // bmp is your Bitmap instance
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
        File file;
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
