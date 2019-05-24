package com.game.xogame.views.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.game.xogame.BuildConfig;
import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.models.GamesModel;
import com.game.xogame.models.UserInfoModel;
import com.game.xogame.presenters.MainPresenter;
import com.game.xogame.views.CustomViewPager;
import com.game.xogame.views.game.FragmentFeeds;
import com.game.xogame.views.game.FragmentGames;
import com.game.xogame.views.profile.FragmentProfile;
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


public class MainActivity extends AppCompatActivity {

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000000;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10;
    public static SectionsPagerAdapter mSectionsPagerAdapter;
    public static CustomViewPager mViewPager;
    public static String token;
    public int currentItem = 1;
    public ApiService api;
    private MainPresenter presenter;
    private LottieAnimationView button3;
    private LottieAnimationView button2;
    private LottieAnimationView button1;
    public static ImageView button4;
    public static ImageView button5;
    public static ImageView button6;
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


        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        token = sharedPref.getString("token", "null");

        api = RetroClient.getApiService();
        UserInfoModel model = new UserInfoModel(api, getApplicationContext());
        GamesModel modelGames = new GamesModel(api, getApplicationContext());
        presenter = new MainPresenter(model, modelGames);
        presenter.attachMainView(this);

        String page;
        if(getIntent().getExtras()!= null && getIntent().getExtras().getString("page")!=null && !getIntent().getExtras().getString("page").equals("")) {
            page = getIntent().getExtras().getString("page");
        }else{
            page = "0";
        }

        init(page);
        initCoord();


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
                if (currentItem == 1) {
                    button1.setSpeed(6f);
                    button1.setAnimation("hide_right.json");
                    button1.playAnimation();
                    button2.setAnimation("hide_left.json");
                    button2.setSpeed(-6f);
                    button2.playAnimation();
                }
                if (currentItem == 2) {
                    FragmentFeeds fragment = (FragmentFeeds) mSectionsPagerAdapter.getItem(1);
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
                    FragmentProfile fragment = (FragmentProfile) mSectionsPagerAdapter.getItem(2);
                    fragment.update();
                }

                button6.setColorFilter(Color.parseColor("#ffffff"));
                button4.setColorFilter(Color.parseColor("#9EA4AC"));
                button5.setColorFilter(Color.parseColor("#9EA4AC"));
                mViewPager.setCurrentItem(2);
                currentItem = 3;
            }
        });


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }


        });


        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(Integer.parseInt(page));

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

    @Override
    public void onBackPressed() {

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
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
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
    protected void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
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
                    return null;//Это для того, что бы что-то вернулось, если порядковый номер вдруг будет больше 2. И в данном случае приложение закроется с ошибкой.

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


    }

}
