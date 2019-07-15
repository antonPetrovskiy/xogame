package com.game.xogame.views.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.game.xogame.BuildConfig;
import com.game.xogame.GamePush;
import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.GamesModel;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.presenters.MainPresenter;
import com.game.xogame.views.CustomViewPager;
import com.game.xogame.views.game.FragmentFeeds;
import com.game.xogame.views.game.FragmentGames;
import com.game.xogame.views.game.GameInfoActivity;
import com.game.xogame.views.game.ModerationActivity;
import com.game.xogame.views.game.PlayActivity;
import com.game.xogame.views.game.RatingGameActivity;
import com.game.xogame.views.game.WinActivity;
import com.game.xogame.views.profile.FragmentProfile;
import com.game.xogame.views.profile.MyGamesActivity;
import com.game.xogame.views.profile.SettingActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;


public class MainActivity extends AppCompatActivity {

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000000;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10;
    public  SectionsPagerAdapter mSectionsPagerAdapter;
    public  CustomViewPager mViewPager;
    public  String token;
    public int currentItem = 1;
    public ApiService api;
    private MainPresenter presenter;
    private LottieAnimationView button3;
    private LottieAnimationView button2;
    private LottieAnimationView button1;
    public static ImageView button4;
    public static ImageView button5;
    public static ImageView button6;

    public static String gameId;
    public static RelativeLayout bar;
    // location last updated time

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private boolean mRequestingLocationUpdates;

    private boolean mLocationPermissionGranted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        PlayActivity.activity = this;
        RatingGameActivity.activity = this;
        ModerationActivity.activity = this;
        WinActivity.activity = this;
        MyGamesActivity.activity = this;
        SettingActivity.activity = this;
        GamePush.activity = this;
        GameInfoActivity.activity = this;
        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        token = sharedPref.getString("token", "null");
        // Initialize the Branch object
        Branch.getAutoInstance(this);
        if(mSectionsPagerAdapter==null) {

            api = RetroClient.getApiService();
            UserInfoModel model = new UserInfoModel(api, getApplicationContext());
            GamesModel modelGames = new GamesModel(api, getApplicationContext());
            presenter = new MainPresenter(model, modelGames);
            presenter.attachMainView(this);

            String page;
            if (getIntent().getExtras() != null && getIntent().getExtras().getString("page") != null && !getIntent().getExtras().getString("page").equals("")) {
                page = getIntent().getExtras().getString("page");
            } else {
                page = "0";
            }
            if (getIntent().getExtras() != null && getIntent().getExtras().getString("gameid") != null && !getIntent().getExtras().getString("gameid").equals("")) {
                gameId = getIntent().getExtras().getString("gameid");
            }

            checkGamePush();

            init(page);
            initCoord();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "fcm";
            // The user-visible name of the channel.
            CharSequence name = "fcm";
            // The user-visible description of the channel.
            String description = "fcm";
            int importance = NotificationManager.IMPORTANCE_MAX;
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            mChannel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getPackageName()+"/"+R.raw.winpush),att);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                                  @Override
                                  public void onPermissionGranted(PermissionGrantedResponse response) {

                                  }

                                  @Override
                                  public void onPermissionDenied(PermissionDeniedResponse response) {
                                      if (response.isPermanentlyDenied()) {
                                          // open device settings when the permission is
                                          // denied permanently
                                          Dexter.withActivity(MainActivity.this)
                                                  .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

                                      }
                                  }

                                  @Override
                                  public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                  }
                              });


        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        while(!mLocationPermissionGranted) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    public void init(String page) {
        button1 = findViewById(R.id.imageView1);
        button2 = findViewById(R.id.imageView2);
        button3 = findViewById(R.id.imageView3);
        button4 = findViewById(R.id.imageView7);
        button5 = findViewById(R.id.imageView8);
        button6 = findViewById(R.id.imageView6);
        bar = findViewById(R.id.bar);

        if (mSectionsPagerAdapter == null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        }

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(Integer.parseInt(page));



        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem == 1) {
                    FragmentGames fragment = (FragmentGames) mSectionsPagerAdapter.getItem(0);
                    fragment.update();
                }
                if (currentItem == 2) {
                    button1.setAnimation("hide_right.json");
                    button2.setAnimation("hide_left.json");
                    button2.setSpeed(6f);
                    button2.playAnimation();
                    button1.setSpeed(-6f);
                    button1.playAnimation();
                }
                if (currentItem == 3) {
                    button3.setAnimation("hide_left.json");
                    button1.setAnimation("hide_right.json");
                    button3.setSpeed(6f);
                    button3.playAnimation();
                    button1.setSpeed(-6f);
                    button1.playAnimation();
                }
                button4.setColorFilter(Color.parseColor("#ffffff"));
                button5.setColorFilter(Color.parseColor("#9EA4AC"));
                button6.setColorFilter(Color.parseColor("#9EA4AC"));
                mViewPager.setCurrentItem(0);
                currentItem = 1;
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentFeeds fragment = (FragmentFeeds) mSectionsPagerAdapter.getItem(1);
                fragment.checkTutorial();
                if (currentItem == 1) {
                    button1.setSpeed(6f);
                    button1.setAnimation("hide_right.json");
                    button1.playAnimation();
                    button2.setAnimation("hide_left.json");
                    button2.setSpeed(-6f);
                    button2.playAnimation();
                }
                if (currentItem == 2) {
                    fragment.update();
                }
                if (currentItem == 3) {
                    button3.setSpeed(6f);
                    button3.setAnimation("hide_left.json");
                    button3.playAnimation();
                    button2.setAnimation("hide_right.json");
                    button2.setSpeed(-6f);
                    button2.playAnimation();
                }
                button5.setColorFilter(Color.parseColor("#ffffff"));
                button4.setColorFilter(Color.parseColor("#9EA4AC"));
                button6.setColorFilter(Color.parseColor("#9EA4AC"));
                mViewPager.setCurrentItem(1);
                currentItem = 2;
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentProfile fragment = (FragmentProfile) mSectionsPagerAdapter.getItem(2);
                fragment.checkTutorial();
                if (currentItem == 1) {
                    button1.setAnimation("hide_right.json");
                    button3.setAnimation("hide_left.json");
                    button1.setSpeed(6f);
                    button1.playAnimation();
                    button3.setSpeed(-6f);
                    button3.playAnimation();
                }
                if (currentItem == 2) {
                    button2.setAnimation("hide_right.json");
                    button3.setAnimation("hide_left.json");
                    button2.setSpeed(6f);
                    button2.playAnimation();
                    button3.setSpeed(-6f);
                    button3.playAnimation();
                }
                if (currentItem == 3) {
                    fragment.update();
                }

                button6.setColorFilter(Color.parseColor("#ffffff"));
                button4.setColorFilter(Color.parseColor("#9EA4AC"));
                button5.setColorFilter(Color.parseColor("#9EA4AC"));
                mViewPager.setCurrentItem(2);
                currentItem = 3;
            }
        });




        switch (page) {
            case "0":
                button4.setColorFilter(Color.parseColor("#ffffff"));
                button1.setAnimation("hide_right.json");
                button1.setSpeed(-6f);
                button1.playAnimation();
                currentItem = 1;
                break;
            case "1":
                button5.setColorFilter(Color.parseColor("#ffffff"));
                button2.setAnimation("hide_right.json");
                button2.setSpeed(-6f);
                button2.playAnimation();
                currentItem = 2;
                break;
            case "2":
                button6.setColorFilter(Color.parseColor("#ffffff"));
                button3.setAnimation("hide_right.json");
                button3.setSpeed(-6f);
                button3.playAnimation();
                currentItem = 3;
                break;
        }
        //presenter.showUserInfo();
    }

    public void coord() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                        FragmentGames fragment = (FragmentGames) mSectionsPagerAdapter.getItem(0);
                        fragment.update();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void initCoord() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();


                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    @SuppressLint("ApplySharedPref")
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            SharedPreferences sharedPref = this.getSharedPreferences("myPref", MODE_PRIVATE);
            sharedPref.edit().putString("lat", mCurrentLocation.getLatitude() + "").commit();
            sharedPref.edit().putString("lng", mCurrentLocation.getLongitude() + "").commit();
            FragmentGames fragment = (FragmentGames) mSectionsPagerAdapter.getItem(0);
            fragment.update();
        }

    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i("TAG", "All location settings are satisfied.");

                        //Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i("TAG", "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i("TAG", "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e("TAG", errorMessage);

                                //Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check for the integer request code originally supplied to startResolutionForResult().
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("LOG_photo1" , "0");
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.e("TAG", "User agreed to make required location settings changes.");
                    // Nothing to do. startLocationupdates() gets called in onResume again.
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e("TAG", "User chose not to make required location settings changes.");
                    mRequestingLocationUpdates = false;
                    break;
            }
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }else{
                    mLocationPermissionGranted = true;

                }
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        Branch branch = Branch.getInstance();
        Log.i("LOG_branch", "0");
        branch.initSession(new Branch.BranchReferralInitListener(){
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                String s=null;
                Log.i("LOG_branch", "1");
                if (error == null) {
                    Log.i("LOG_branch", "2 ");
                    s = referringParams.optString("game_id", "");
                }
                Log.i("LOG_branch", "3 "+s);
                if(s!=null&&!s.equals("")){
                    gameId=s;
                    FragmentGames fragment = (FragmentGames) mSectionsPagerAdapter.getItem(0);
                    fragment.update();
                }
            }
        }, this.getIntent().getData(), this);

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            stopLocationUpdates();
        }
    }

    public void checkGamePush(){
        //CHECK GAMEINFO PUSH
        if (getIntent().getExtras() != null && getIntent().getExtras().getString("NAME") != null && !getIntent().getExtras().getString("NAME").equals("")) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.popup_push, null);
            final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(this).create();

            TextView txt1 = promptView.findViewById(R.id.textView1);
            TextView txt2 = promptView.findViewById(R.id.textView2);
            ImageView img1 = promptView.findViewById(R.id.imageView);
            alertD.setView(promptView);
            txt1.setText(getIntent().getExtras().getString("NAME"));
            txt2.setText(getIntent().getExtras().getString("DESCRIPTION"));
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.unknow_wide);
            requestOptions.error(R.drawable.unknow_wide);
            requestOptions.override(1920, 1080);
            requestOptions.centerCrop();

            //if(extras.getString("USER")!=null && extras.getString("USER").equals("another"))
            //subscribe.setVisibility(View.GONE);
            Glide.with(this).setDefaultRequestOptions(requestOptions).load(getIntent().getExtras().getString("PHOTO")).thumbnail(0.3f).into(img1);
            alertD.show();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //button4.performClick();
                    return FragmentGames.newInstance(getApplicationContext(), presenter);
                case 1:
                    //button5.performClick();
                    return FragmentFeeds.newInstance(getApplicationContext(), presenter);
                case 2:
                    // button6.performClick();
                    return FragmentProfile.newInstance(getApplicationContext(), presenter);
                default:
                    return FragmentGames.newInstance(getApplicationContext(), presenter);


            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


    }

}
