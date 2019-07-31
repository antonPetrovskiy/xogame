package com.game.xogame.views.create;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.game.xogame.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateAuditoryActivity extends FragmentActivity implements OnMapReadyCallback {

    private ImageView back;
    private ImageView save;
    private LatLng point;
    private SeekBar radius;
    private EditText street;
    AutocompleteSupportFragment autocompleteFragment;

    private ImageView iconPoint;
    private ImageView iconMe;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private EditText country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_auditory);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Places.initialize(getApplicationContext(), "AIzaSyAFzTK5clEGuEaSL5AhRkp4s19CWBJHzFg");

// Create a new Places client instance.

        PlacesClient placesClient = Places.createClient(this);
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        MapsInitializer.initialize(getApplicationContext());
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.d("Maps", "Place selected: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d("Maps", "Place selected: " + status);
            }
        });
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init(){
        street = findViewById(R.id.edittext);
        back = findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        save = findViewById(R.id.button4);
        save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        save.animate().setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        save.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                        if(point!=null) {
                            Intent intent = new Intent(CreateAuditoryActivity.this, CreateGameActivity.class);
                            intent.putExtra("lat",point.latitude+"");
                            intent.putExtra("lng",point.longitude+"");
                            intent.putExtra("radius",radius.getProgress()+"");
                            intent.putExtra("street",street.getText().toString()+"");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else{
                            showToast(getString(R.string.activityCreateAuditory_enter));
                        }
                        break;
                }
                return true;
            }
        });

        radius = findViewById(R.id.seekBar);
        radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (point != null) {
                    mMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(point);
                    markerOptions.anchor(0.5f,0.5f);
                    final float scale = getResources().getDisplayMetrics().density;
                    int pixels = (int) (38 * scale + 0.5f);
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_map_point);
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, pixels, pixels, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 10));
                    mMap.addMarker(markerOptions);
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(point.latitude, point.longitude))
                            .radius(progress*1000)
                            .strokeColor(Color.parseColor("#00FF6800"))
                            .fillColor(Color.parseColor("#8CFF6800")));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (point != null) {
                    mMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(point);
                    markerOptions.anchor(0.5f,0.5f);
                    final float scale = getResources().getDisplayMetrics().density;
                    int pixels = (int) (38 * scale + 0.5f);
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_map_point);
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, pixels, pixels, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 10));
                    mMap.addMarker(markerOptions);
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(point.latitude, point.longitude))
                            .radius(seekBar.getProgress()*1000)
                            .strokeColor(Color.parseColor("#00FF6800"))
                            .fillColor(Color.parseColor("#8CFF6800")));
                }
            }
        });

        iconPoint = findViewById(R.id.imageView10);
        iconPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(point!=null)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 10));
            }
        });
        iconMe = findViewById(R.id.imageView9);
        iconMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
                String lat = sharedPref.getString("lat", "null");
                String lon = sharedPref.getString("lng", "null");
                mMap.clear();
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
                // Setting the position for the marker
                markerOptions.position(new LatLng(Double.parseDouble(lat),Double.parseDouble(lon)));
                markerOptions.anchor(0.5f,0.5f);
                point = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
                final float scale = getResources().getDisplayMetrics().density;
                int pixels = (int) (38 * scale + 0.5f);
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_map_point);
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, pixels, pixels, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 10));
                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);


                mMap.addCircle(new CircleOptions()
                        .center(new LatLng(point.latitude, point.longitude))
                        .radius(radius.getProgress()*1000)
                        .strokeColor(Color.parseColor("#00FF6800"))
                        .fillColor(Color.parseColor("#8CFF6800")));

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(CreateAuditoryActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String address = "";
                if(addresses!=null)
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                street.setText(address);
            }
        });




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = googleMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
//
//            if (!success) {
//                Log.e("Log", "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e("Log", "Can't find style. Error: ", e);
//        }

        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        if(!sharedPref.getString("lng", "null").equals("null"))
            iconMe.performClick();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
                // Setting the position for the marker
                markerOptions.position(latLng);
                markerOptions.anchor(0.5f,0.5f);
                point = latLng;
                final float scale = getResources().getDisplayMetrics().density;
                int pixels = (int) (38 * scale + 0.5f);
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_map_point);
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, pixels, pixels, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);


                mMap.addCircle(new CircleOptions()
                        .center(new LatLng(point.latitude, point.longitude))
                        .radius(radius.getProgress()*1000)
                        .strokeColor(Color.parseColor("#00FF6800"))
                        .fillColor(Color.parseColor("#8CFF6800")));


                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(CreateAuditoryActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                street.setText(address);
            }

        });
    }

    public void showToast(String s) {
        LayoutInflater layoutInflater = LayoutInflater.from(CreateAuditoryActivity.this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.error, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(this).create();
        TextView btnAdd1 = promptView.findViewById(R.id.textView1);
        btnAdd1.setText(s);
        alertD.setView(promptView);
        alertD.show();

    }
}
