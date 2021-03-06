package com.example.travel_guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
     mMap=googleMap;
     if (mLocationPermissionGranted){
         getDeviceLocation();
         mMap.setMyLocationEnabled(true);
         mMap.getUiSettings().setMyLocationButtonEnabled(false);
         LatLng Lumbini = new LatLng(27.469554, 83.275788);
         LatLng Tilaurakot = new LatLng(27.5829, 83.0845);
         LatLng Ranimahal = new LatLng(27.9267, 83.5278);
         LatLng Banbatika = new LatLng(27.6527, 83.4824);

         mMap.addMarker(new MarkerOptions()
                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon))
                 .position(Lumbini)
                 .title("Lumbini")
                 .snippet("Wanna Visit"));
         mMap.addMarker(new MarkerOptions()
                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon))
                 .position(Tilaurakot)
                 .title("Tilaurakot")
                 .snippet("Wanna Visit"));
         mMap.addMarker(new MarkerOptions()

                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon))
                 .position(Ranimahal)
                 .title("Ranimahal")
                 .snippet("Wanna Visit"));
         mMap.addMarker(new MarkerOptions()
                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon))
                 .position(Banbatika)
                 .title("Banbatika")
                 .snippet("Wanna Visit"));

         mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()

         {

             @Override
             public void onInfoWindowClick(Marker arg0) {
                 if(arg0 != null && arg0.getTitle().equals("Lumbini")){
                     Intent intent1 = new Intent(MapActivity.this, Lumbini.class);
                     startActivity(intent1);}

                 if(arg0 != null && arg0.getTitle().equals("Tilaurakot")){
                     Intent intent2 = new Intent(MapActivity.this, Tilaurakot.class);
                     startActivity(intent2);}

                 if(arg0 != null && arg0.getTitle().equals("Ranimahal")){
                     Intent intent3 = new Intent(MapActivity.this, Ranimahal.class);
                     startActivity(intent3);}

                 if(arg0 != null && arg0.getTitle().equals("Banbatika")){
                     Intent intent4 = new Intent(MapActivity.this, Banbatika.class);
                     startActivity(intent4);}
             }
         });
         init();

     }
    }
         private static final String TAG = "MapActivity";

         private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
         private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
         private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
         private static final float DEFAULT_ZOOM = 9.1f;
        //widgets
            private EditText mSearchText;
            private ImageView mGps;
         private Boolean mLocationPermissionGranted = false;
         private GoogleMap mMap;
         private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ImageView but1 = (ImageView) findViewById(R.id.ic_hot);
      but1.setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View v) {
               startActivity(new Intent(MapActivity.this, Hot.class));
         }
       });
      mSearchText = (EditText) findViewById(R.id.input_search);
       mGps =(ImageView) findViewById(R.id.ic_gps);
        getLocationPermission();
      init();
    }
    private void init(){
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH || actionId== EditorInfo.IME_ACTION_DONE || keyEvent.getAction()== KeyEvent.ACTION_DOWN||keyEvent.getAction()== KeyEvent.KEYCODE_ENTER){
                    //execute our method for searching
                    geoLocate();
                }
                return false;
            }
        });
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
        HideSoftKeyboard();
    }
     private void geoLocate(){
         Log.d(TAG, "geoLocate: geoLocating");
         String searchString = mSearchText.getText().toString();
         Geocoder geocoder =  new Geocoder(MapActivity.this);
         List<Address> list = new ArrayList<>();
         try {
             list= geocoder.getFromLocationName(searchString, 1);
         }
         catch(IOException e){
             Log.e(TAG, "geoLocate: IOException:" + e.getMessage());
         }
         if(list.size()>0){
             Address address = list.get(0);
             Log.d(TAG, "geoLocate: found a location: " + address.toString());
            // Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

             moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
         }
     }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                final Task location  = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                   if(task.isSuccessful()){
                       Log.d(TAG, "OnComplete: found Location!");
                       Location currentLocation = (Location) task.getResult();
                       moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                   }
                   else{
                       Log.d(TAG, "OnComplete: current location is null!");
                       Toast.makeText(MapActivity.this,"unable to get current location", Toast.LENGTH_SHORT).show();
                   }
                    }
                });
            }

        } catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: "+ e.getMessage());
        }
    }

private void moveCamera(LatLng latLng, float zoom, String title){
    Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
if (!title.equals("My Location")){
    MarkerOptions options = new MarkerOptions()
            .position(latLng)
            .title(title);
    mMap.addMarker(options);
}
    HideSoftKeyboard();
    }


    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }
    private void getLocationPermission() {
        String[] permission = {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }
            else {
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted= false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length>0){
                    for (int i=0; i < grantResults.length; i++ ){
                        if (grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted=false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //inatialize map
                    initMap();
                }
            }
        }
    }

    private void  HideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



}
