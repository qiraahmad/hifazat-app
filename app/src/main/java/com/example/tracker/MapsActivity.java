package com.example.tracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import android.telephony.SmsManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationListener locationListener;
    /**
     * checks if location is changed or not
     */
    private LocationManager locationManager;
    /**
     * for requesting to check change in location, where provider is GPS service
     */
    private final long Min_time = 1000;
    /**
     * 1 second
     */
    private final long Min_distance = 5;
    /**
     * 5 meters
     */
    private LatLng latLng;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    Marker marker = null;
    private static final int SMS_PERMISSION_CODE = 0;
    String SMS_SENT = "SMS_SENT";
    String SMS_DELIVERED = "SMS_DELIVERED";

    PendingIntent sentPendingIntent;
    PendingIntent deliveredPendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        /**
         * checking if permissions have been granted
         */
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);


        sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try {
                    // Add a marker in Sydney and move the camera
                    double loc = location.getLatitude();
                    double loc1 = location.getLongitude();
                    LatLng nloc = new LatLng(loc, loc1);

                    if (marker != null) {
                        marker.remove();
                    }

                    marker = mMap.addMarker(new MarkerOptions().position(nloc).title("Marker in Pakistan").icon(BitmapDescriptorFactory.defaultMarker()));
                    marker.showInfoWindow();
                    moveToCurrentLocation(nloc, mMap);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }


            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        try { /** check if permissions have been granted */
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Min_time, Min_distance, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }


    public void onZoom(View view) {
        if (view.getId() == R.id.button2) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if (view.getId() == R.id.button3) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());

        }
        if (view.getId() == R.id.button4) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
                {
                    SendSMS();
                }
            }
            else{

                requestPermissions(new String[] {Manifest.permission.SEND_SMS}, 1);
            }

        }

    }

    public void onLogOut(View view) {
        if (view.getId() == R.id.button1) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(MapsActivity.this, SignUpActivity.class);
            startActivity(i);

        }

    }

    private void moveToCurrentLocation(LatLng currentLocation, GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void SendSMS() {
        String no = "+923371424828";
        String msg = "WASSSSUPPP";


        try {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(no, null, msg, null, null);
            Toast.makeText(MapsActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MapsActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }

    }

}

