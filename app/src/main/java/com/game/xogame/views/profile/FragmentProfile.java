package com.game.xogame.views.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.adapter.GamesAdapter;
import com.game.xogame.entity.Game;
import com.game.xogame.presenters.MainPresenter;
import com.game.xogame.views.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class FragmentProfile extends Fragment {

    private ImageView settings;
    private ImageView photo_view;
    private ImageView photo_edit;
    private TextView name;
    private TextView nickName;


    private ImageView myGames;
    private LinearLayout myWins;
    private LinearLayout load;
    private Button find;

    private ListView list1;
    private ListView list2;
    private int size1 = 0;
    private int size2 = 0;

    public boolean isPhoto = false;
    private RelativeLayout empty;
    private  GamesAdapter adapter1;
    private  GamesAdapter adapter2;
    private View rootView;
    static private Context context;
    static private MainPresenter presenter;

    String imagePath;
    File directory;
    File myFile;
    public static final int REQ_CODE_PICK_PHOTO = 0;
    public static final int REQ_CODE_CAPTURE = 2;

    public FragmentProfile() {
    }


    public static FragmentProfile newInstance(Context c, MainPresenter p) {
        FragmentProfile fragment = new FragmentProfile();
        context = c;
        presenter = p;

        //Bundle args = new Bundle();
        //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //fragment.setArguments(args);
        return fragment;
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
        if(!isPhoto)
            presenter.showUserInfo(this);
        super.onResume();
    }

    public void init(){
        photo_view = rootView.findViewById(R.id.imageProfile);
        photo_edit = rootView.findViewById(R.id.imageView);
        name = rootView.findViewById(R.id.textView1);
        nickName = rootView.findViewById(R.id.textView2);
        settings = rootView.findViewById(R.id.imageView14);

        myGames = rootView.findViewById(R.id.myGames);
        myWins = rootView.findViewById(R.id.myWins);
        load = rootView.findViewById(R.id.targetView);
        empty = rootView.findViewById(R.id.empty);
        list1 = rootView.findViewById(R.id.list1);
        list2 = rootView.findViewById(R.id.list2);
        find = rootView.findViewById(R.id.imageButton);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
            }
        });

        myGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyGamesActivity.class);
                startActivity(intent);
            }
        });

        myWins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyWinsActivity.class);
                startActivity(intent);
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mViewPager.setCurrentItem(0);
            }
        });

        photo_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    public void onClick(View v) {
                        alertD.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        startActivityForResult(intent,  REQ_CODE_PICK_PHOTO);

                        //Crop.pickImage(InformationActivity.this);
                    }
                });

                alertD.setView(promptView);
                alertD.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isPhoto = true;
        if (requestCode == REQ_CODE_PICK_PHOTO && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){


                imagePath = getFilePath(data);
                File file = new File(imagePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
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
            photo_view.setImageBitmap(myBitmap);
            imagePath = myFile.getAbsolutePath();
            presenter.editPhoto(this);
        }
    }

    public void setList(List<Game> list){
//        List<Game> l1 = new ArrayList<>();
//        for(int i = 0; i < list.size(); i ++){
//            if(list.get(i).getSubscribe().equals("1")){
//                l1.add(list.get(i));
//            }
//        }
//
        if(adapter1==null) {
            adapter1 = new GamesAdapter(context, list);
        }else{
            adapter1.notifyDataSetChanged();
        }
        list1.setAdapter(adapter1);
        load.setVisibility(View.GONE);
        if(list.size()==0) {
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }
//        size1 = list.size();
//
//
//        if(adapter2==null) {
//            adapter2 = new GamesAdapter(context, l1);
//        }else{
//            adapter2.notifyDataSetChanged();
//        }
//        list2.setAdapter(adapter2);
//        size2 = l1.size();
//

    }


    private String getFilePath(Intent data) {
        String imagePath;
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        imagePath = cursor.getString(columnIndex);
        cursor.close();

        return imagePath;

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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public String getPhoto(){
        return imagePath+"";
    }

    public void setPhoto(String s){
        Picasso.with(context).load(s).placeholder(R.drawable.camera_background).error(R.drawable.camera_background).into(photo_view);
    }

    public void setName(String s){
        name.setText(s);
    }

    public void setNickName(String s){
        nickName.setText(s);
    }


}
