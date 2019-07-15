package com.game.xogame.views.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.exifinterface.media.ExifInterface;

import com.game.xogame.R;
import com.game.xogame.adapter.GamesAdapter;
import com.game.xogame.entity.Game;
import com.game.xogame.presenters.MainPresenter;
import com.game.xogame.views.create.MyCreatedActivity;
import com.game.xogame.views.game.GameInfoActivity;
import com.game.xogame.views.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


public class FragmentProfile extends Fragment {

    public static final int REQ_CODE_PICK_PHOTO = 0;
    public static final int REQ_CODE_CAPTURE = 2;
    static private Context context;
    static private MainPresenter presenter;
    private static FragmentProfile fragment;
    public boolean isPhoto = false;
    String imagePath;
    String imageurl;
    File directory;
    File myFile;
    private ImageView settings;
    private ImageView photo_view;
    private TextView textHolder;
    private ImageView photo_edit;
    private TextView name;
    private TextView nickName;
    private ImageView myGames;
    private LinearLayout myWins;
    private LinearLayout creategame;
    private LinearLayout load;
    private Button find;
    private ListView list1;
    private ListView list2;
    private SwipeRefreshLayout pullToRefresh;
    private TextView current;
    private TextView future;
    private RelativeLayout empty;
    private GamesAdapter adapter1;
    private GamesAdapter adapter2;
    private View rootView;
    private List<Game> gameNowList;
    private List<Game> gameFutureList;


    private RelativeLayout tutorialView;
    private TextView tutorialText;
    private Button tutorialButton;
    private ImageView tutorialArrowRight;
    private ImageView tutorialIconRight;

    public FragmentProfile() {
    }


    public static FragmentProfile newInstance(Context c, MainPresenter p) {
        if (fragment == null) {
            fragment = new FragmentProfile();
        }
        context = c;
        presenter = p;
        return fragment;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);

            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        ViewGroup.LayoutParams.MATCH_PARENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount()));

        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        createDirectory();
        init();
        presenter.showUserInfo(this);
        presenter.showMyGames(this);

        return rootView;
    }

    @Override
    public void onResume() {
        if (!isPhoto)
            presenter.showUserInfo(this);
        gameNowList = new LinkedList<>();
        gameFutureList = new LinkedList<>();
        adapter1 = null;
        adapter2 = null;
        presenter.showMyGames(this);
        super.onResume();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        photo_view = rootView.findViewById(R.id.imageProfile);
        textHolder = rootView.findViewById(R.id.textHolder);
        photo_edit = rootView.findViewById(R.id.imageView);
        name = rootView.findViewById(R.id.textView1);
        nickName = rootView.findViewById(R.id.textView2);
        settings = rootView.findViewById(R.id.imageView14);
        current = rootView.findViewById(R.id.textView9);
        future = rootView.findViewById(R.id.textView10);
        myGames = rootView.findViewById(R.id.myGames);
        myWins = rootView.findViewById(R.id.myWins1);
        creategame = rootView.findViewById(R.id.creategame);
        load = rootView.findViewById(R.id.targetView);
        empty = rootView.findViewById(R.id.empty);
        list1 = rootView.findViewById(R.id.list1);
        list2 = rootView.findViewById(R.id.list2);
        find = rootView.findViewById(R.id.imageButton);


        tutorialView = rootView.findViewById(R.id.tutorial);
        tutorialText = rootView.findViewById(R.id.tutorial_text);
        tutorialButton = rootView.findViewById(R.id.tutorial_button);
        tutorialArrowRight = rootView.findViewById(R.id.tutorial_right_arrow);
        tutorialIconRight = rootView.findViewById(R.id.tutorial_right_icon);

        pullToRefresh = rootView.findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gameNowList = new LinkedList<>();
                gameFutureList = new LinkedList<>();
                adapter1 = null;
                adapter2 = null;
                presenter.showMyGames(FragmentProfile.this);
                pullToRefresh.setRefreshing(false);
            }
        });
        settings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        settings.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        settings.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        Intent intent = new Intent(context, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        myGames.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        myGames.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        myGames.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        Intent intent = new Intent(context, MyGamesActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        myWins.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        myWins.animate().setDuration(200).alpha(0.5f).start();
                        return true;
                    case MotionEvent.ACTION_UP: // отпускание
                    case MotionEvent.ACTION_CANCEL:
                        myWins.animate().setDuration(100).alpha(1.0f).start();
                        Intent intent = new Intent(context, MyWinsActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        creategame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        creategame.animate().setDuration(200).alpha(0.5f).start();
                        return true;
                    case MotionEvent.ACTION_UP: // отпускание
                    case MotionEvent.ACTION_CANCEL:
                        creategame.animate().setDuration(100).alpha(1.0f).start();
                        Intent intent = new Intent(context, MyCreatedActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        find.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        find.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        find.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        MainActivity.button4.performClick();
                        break;
                }
                return true;
            }
        });


        photo_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageurl!=null && !imageurl.equals("") && !imageurl.equals("null")) {
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.popup_image, null);
                    final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();
                    Objects.requireNonNull(alertD.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                    alertD.getWindow().setDimAmount(0.9f);
                    ImageView btnAdd1 = promptView.findViewById(R.id.imageView5);
                    Picasso.with(context).load(imageurl + "").placeholder(R.drawable.unknow).error(R.drawable.unknow).into(btnAdd1);
                    alertD.setView(promptView);
                    alertD.show();
                }
            }
        });

        photo_edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        photo_edit.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        photo_edit.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                        View promptView = layoutInflater.inflate(R.layout.popup_imagechooser, null);
                        final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();

                        TextView btnAdd1 = promptView.findViewById(R.id.textView1);
                        TextView btnAdd2 = promptView.findViewById(R.id.textView2);

                        btnAdd1.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                alertD.cancel();
                                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                StrictMode.setVmPolicy(builder.build());
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri());
                                startActivityForResult(intent, REQ_CODE_CAPTURE);
                            }
                        });

                        btnAdd2.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("IntentReset")
                            public void onClick(View v) {
                                alertD.cancel();
                                Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                                Log.i("LOG_photo1" , "-1");
                                startActivityForResult(intent, REQ_CODE_PICK_PHOTO);
                            }
                        });

                        alertD.setView(promptView);
                        alertD.show();
                        break;
                }
                return true;
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isPhoto = true;
        if (requestCode == REQ_CODE_PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if ((data != null) && (data.getData() != null)) {
                imagePath = getFilePath(data);
                File file = new File(imagePath);

                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                try (FileOutputStream out = new FileOutputStream(file)) {
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (IOException e) {
                    e.printStackTrace();
                }


                photo_view.setImageBitmap(myBitmap);
                presenter.editPhoto(this);
            }
        }


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
            photo_view.setImageBitmap(myBitmap);
            imagePath = myFile.getAbsolutePath();
            presenter.editPhoto(this);
        }
    }

    public void update(){
        presenter.showUserInfo(this);
    }

    public void setNowList(List<Game> list) {
        gameNowList = list;
        final List<Game> l = list;
        if (adapter1 == null) {
            adapter1 = new GamesAdapter(context, gameNowList);
        } else {
            adapter1.notifyDataSetChanged();
        }
        list1.setAdapter(adapter1);
        setListViewHeightBasedOnChildren(list1);
        if (gameNowList.size() != 0) {
            current.setVisibility(View.VISIBLE);
        }else{
            current.setVisibility(View.GONE);
        }


        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, GameInfoActivity.class);
                intent.putExtra("GAMEID", l.get(position).getGameid());
                intent.putExtra("SUBSCRIBE", l.get(position).getSubscribe());
                intent.putExtra("TITLE", l.get(position).getTitle());
                intent.putExtra("NAME", l.get(position).getCompany());
                intent.putExtra("LOGO", l.get(position).getLogo());
                intent.putExtra("BACKGROUND", l.get(position).getBackground());
                intent.putExtra("DATE", l.get(position).getStartdate() + "-" + l.get(position).getEnddate());
                intent.putExtra("DESCRIPTION", l.get(position).getDescription());
                intent.putExtra("TASKS", l.get(position).getTasks());
                intent.putExtra("TIME", l.get(position).getStarttime() + "-" + l.get(position).getEndtime());
                intent.putExtra("MONEY", l.get(position).getReward());
                intent.putExtra("PEOPLE", l.get(position).getFollowers());
                intent.putExtra("ADDRESS", l.get(position).getAddress());
                intent.putExtra("STATISTIC", "true");
                startActivity(intent);
            }
        });

    }

    public void setFutureList(List<Game> list) {
        final List<Game> l = list;
        gameFutureList = list;
        if (adapter2 == null) {
            adapter2 = new GamesAdapter(context, gameFutureList);
        } else {
            adapter2.notifyDataSetChanged();
        }
        list2.setAdapter(adapter2);
        setListViewHeightBasedOnChildren(list2);
        if (gameFutureList.size() != 0) {
            future.setVisibility(View.VISIBLE);
        }else{
            future.setVisibility(View.GONE);
        }
        load.setVisibility(View.GONE);
        if (gameNowList.size() == 0 && gameFutureList.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, GameInfoActivity.class);
                intent.putExtra("GAMEID", l.get(position).getGameid());
                intent.putExtra("SUBSCRIBE", l.get(position).getSubscribe());
                intent.putExtra("TITLE", l.get(position).getTitle());
                intent.putExtra("NAME", l.get(position).getCompany());
                intent.putExtra("LOGO", l.get(position).getLogo());
                intent.putExtra("BACKGROUND", l.get(position).getBackground());
                intent.putExtra("DATE", l.get(position).getStartdate() + "-" + l.get(position).getEnddate());
                intent.putExtra("DESCRIPTION", l.get(position).getDescription());
                intent.putExtra("TASKS", l.get(position).getTasks());
                intent.putExtra("TIME", l.get(position).getStarttime() + "-" + l.get(position).getEndtime());
                intent.putExtra("MONEY", l.get(position).getReward());
                intent.putExtra("PEOPLE", l.get(position).getFollowers());
                intent.putExtra("SHARE", l.get(position).getSiteurl());
                intent.putExtra("AVALIBLE", l.get(position).getGameAvaible());
                intent.putExtra("ADDRESS", l.get(position).getAddress());
                intent.putExtra("STATISTIC", "false");
                startActivity(intent);
            }
        });

    }

    private String getFilePath(Intent data) {
        String imagePath;
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        assert selectedImage != null;
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        imagePath = cursor.getString(columnIndex);
        cursor.close();

        return imagePath;

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

    public String getPhoto() {
        return imagePath + "";
    }

    public void setPhoto(String s) {
        if(s.equals(""))
            s = "q";
        imageurl = s;
        textHolder.setText(nickName.getText().toString().substring(0, 1).toUpperCase());
        this.photo_view.setImageResource(getPlaceholder(nickName.getText().toString()));
        Picasso.with(context).load(s + "").placeholder(getPlaceholder(nickName.getText().toString())).error(getPlaceholder(nickName.getText().toString())).into(this.photo_view, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                textHolder.setText("");
            }

            @Override
            public void onError() {

            }
        });
        //Picasso.with(context).load(s).placeholder(R.drawable.camera_background).centerCrop().resize(1024, 1024).error(R.drawable.camera_background).into(photo_view);
    }

    public void setName(String s) {
        name.setText(s);
    }

    public void setNickName(String s) {
        nickName.setText(s);
    }

    private int getPlaceholder(String s) {
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZЯЧСМИТЬБЮФЫВАПРОЛДЖЭЁЙЦУКЕНГШЩЗХЪІЄ0123456789";
        int n = abc.indexOf(s.substring(0, 1).toUpperCase());
        if (n == 0 || n == 20 || n == 40 || n == 60) {
            return R.color.color1;
        }
        if (n == 1 || n == 21 || n == 41 || n == 61) {
            return R.color.color2;
        }
        if (n == 2 || n == 22 || n == 42 || n == 62) {
            return R.color.color3;
        }
        if (n == 3 || n == 23 || n == 43 || n == 63) {
            return R.color.color4;
        }
        if (n == 4 || n == 24 || n == 44 || n == 64) {
            return R.color.color5;
        }
        if (n == 5 || n == 25 || n == 45 || n == 65) {
            return R.color.color6;
        }
        if (n == 6 || n == 26 || n == 46 || n == 66) {
            return R.color.color7;
        }
        if (n == 7 || n == 27 || n == 47 || n == 67) {
            return R.color.color8;
        }
        if (n == 8 || n == 28 || n == 48 || n == 68) {
            return R.color.color9;
        }
        if (n == 9 || n == 29 || n == 49 || n == 69) {
            return R.color.color10;
        }
        if (n == 10 || n == 30 || n == 50 || n == 70) {
            return R.color.color11;
        }
        if (n == 11 || n == 31 || n == 51 || n == 71) {
            return R.color.color12;
        }
        if (n == 12 || n == 32 || n == 52 || n == 72) {
            return R.color.color13;
        }
        if (n == 13 || n == 33 || n == 53 || n == 73) {
            return R.color.color14;
        }
        if (n == 14 || n == 34 || n == 54 || n == 74) {
            return R.color.color15;
        }
        if (n == 15 || n == 35 || n == 55 || n == 75) {
            return R.color.color16;
        }
        if (n == 16 || n == 36 || n == 56 || n == 76) {
            return R.color.color17;
        }
        if (n == 17 || n == 37 || n == 57 || n == 77) {
            return R.color.color18;
        }
        if (n == 18 || n == 38 || n == 58 || n == 78) {
            return R.color.color19;
        }
        if (n == 19 || n == 39 || n == 59 || n == 79) {
            return R.color.color20;
        }
        return R.color.color1;
    }

    public void checkTutorial(){
        final SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
        if(sharedPref.getString("tutorial_profile", "false").equals("true")){
            tutorialView.setVisibility(View.VISIBLE);
            MainActivity.bar.setVisibility(View.GONE);
            tutorialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tutorialText.getText().toString().equals(getString(R.string.tutorial_history))){
                        tutorialArrowRight.clearAnimation();
                        tutorialView.setVisibility(View.GONE);
                        MainActivity.bar.setVisibility(View.VISIBLE);
                        sharedPref.edit().putString("tutorial_profile","false").commit();
                    }else {
                        tutorialText.setText(getString(R.string.tutorial_history));
                        tutorialArrowRight.setVisibility(View.VISIBLE);
                        tutorialIconRight.setVisibility(View.VISIBLE);
                        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
                                0.0f, 8.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
                        animation.setDuration(300);  // animation duration
                        animation.setRepeatCount(50);  // animation repeat count
                        animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
                        tutorialArrowRight.startAnimation(animation);
                    }
                }
            });
            tutorialView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tutorialButton.performClick();
                }
            });
        }
    }

}
