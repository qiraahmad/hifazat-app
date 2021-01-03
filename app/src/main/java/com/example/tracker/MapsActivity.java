package com.example.tracker;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class MapsActivity extends Fragment implements OnMapReadyCallback {

    ImageButton zoomIn;
    ImageButton zoomOut;
    Button sms;
    LatLng nloc;
    private GoogleMap googleMap;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Contacts");
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private LocationListener locationListener;    /**
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

    Marker marker = null;
    String SMS_SENT = "SMS_SENT";
    String SMS_DELIVERED = "SMS_DELIVERED";

    private GoogleMap mMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        PendingIntent sentPendingIntent;
        PendingIntent deliveredPendingIntent;
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        zoomIn = (ImageButton) rootView.findViewById(R.id.button3);
        zoomOut = (ImageButton) rootView.findViewById(R.id.button2);
        sms = (Button) rootView.findViewById(R.id.button4);


        try {
            MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * checking if permissions have been granted
         */
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);


        sentPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SMS_SENT), 0);
        deliveredPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SMS_DELIVERED), 0);
        return rootView;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void SendSMS() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    contact m = snapshot1.getValue(contact.class);
                    String phone = m.getMobile();
                    phone = phone.replaceFirst("0", "+92");
                    String Uid = m.getUser_id();
                    String lat = Double.toString(nloc.latitude);
                    String lng = Double.toString(nloc.longitude);

                    String msg = "HELP REQUIRED URGENTLY AT MAP LOCATION: " + "http://www.google.com/maps/place/" + lat + "," + lng;
                    if (Uid.equals(firebaseUser.getEmail())) {
                        try {
                            SmsManager smgr = SmsManager.getDefault();
                            smgr.sendTextMessage(phone, null, msg, null, null);
                            Toast.makeText(getActivity(), "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        reference.keepSynced(true);
                    }
                    reference.keepSynced(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }




    private void moveToCurrentLocation(LatLng currentLocation, GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                try {
                    // Add a marker in Sydney and move the camera
                    double loc = location.getLatitude();
                    double loc1 = location.getLongitude();
                    nloc = new LatLng(loc, loc1);


                    //Intent intent = new Intent(getActivity(), MainActivity.class);
                    //intent.putExtra("lat",loc);
                    //intent.putExtra("lng", loc1);
                    //startActivity(intent);
                    String locDeets = "Latitude: " + loc + ", Longitude: " + loc1;

                    if (marker != null) {
                        marker.remove();
                    }

                    marker = mMap.addMarker(new MarkerOptions().snippet(locDeets).position(nloc).title("Marker in Pakistan").icon(BitmapDescriptorFactory.defaultMarker()));
                    marker.showInfoWindow();
                    moveToCurrentLocation(nloc, mMap);
                    mMap.setMyLocationEnabled(true);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("lat", Double.toString(loc));
                    hashMap.put("lng", Double.toString(loc1));
                    hashMap.put("user_id", firebaseUser.getEmail());
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Locations");
                    ref.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            }
                            else {
                                Toast.makeText(getContext(), "An error occurred, try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    reference.keepSynced(true);


                    sms.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SendSMS();

                        }
                    });


                    zoomOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMap.animateCamera(CameraUpdateFactory.zoomOut());

                        }
                    });
                    zoomIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMap.animateCamera(CameraUpdateFactory.zoomIn());

                        }
                    });
                } catch (SecurityException e) {
                    e.printStackTrace();
                }


            }
        };

        locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        try { /** check if permissions have been granted */
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Min_time, Min_distance, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }



}

