package com.example.tracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private SharedViewModel viewModel;
    int button_count=0;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Contacts");
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    double lat = 0;
    double lng  = 0;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapsActivity()).commit();
        }
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        viewModel.getText().observe(this, item -> {
            if (item.equals("new_cont")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ContactEditor()).commit();
            }
            else if(item.equals("cancel")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new emergency_conts()).commit();
            }
        });
        viewModel.getContact().observe(this, item ->{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new emergency_conts(item)).commit();
        });
        viewModel.getEditContact1().observe(this, item ->
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ContactEditor(item)).commit();
        });
        viewModel.getEditContact().observe(this, item ->{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new emergency_conts(item.get(0), item.get(1))).commit();
        });
        viewModel.getDelContact1().observe(this, item ->{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new emergency_conts(item, false)).commit();
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.tab_map_item:
                            selectedFragment = new MapsActivity();
                            break;
                        case R.id.tab_emergency_cont_item:
                            selectedFragment = new emergency_conts();
                            break;
                        case R.id.tab_directory_item:
                            selectedFragment = new directory();
                            break;
                        case R.id.tab_sos_item:
                            selectedFragment = new sos();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) { //checks if volume down has been pressed 4 times to initiate sending emergency message to contacts

            button_count++;
            if (button_count==4) {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity)this,
                            new String[]{Manifest.permission.SEND_SMS},
                            PackageManager.PERMISSION_GRANTED);

                }
                else {
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Locations");
                    ref.orderByKey().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Location m = snapshot1.getValue(Location.class);
                                String a = m.getUser_id();
                                int flag = 0;

                                if (a.equals(firebaseUser.getEmail()) && flag!=1) {
                                    try {
                                        msg = "HELP REQUIRED URGENTLY AT MAP LOCATION: " + "http://www.google.com/maps/place/" + m.getLat() + "," + m.getLng();
                                        flag=1;
                                        break;
                                    } catch (Exception e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    ref.keepSynced(true);
                                }
                                ref.keepSynced(true);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                contact m = snapshot1.getValue(contact.class);
                                String phone = m.getMobile();
                                phone = phone.replaceFirst("0", "+92");
                                String Uid = m.getUser_id();
                                if (Uid.equals(firebaseUser.getEmail())) {
                                    try {
                                        SmsManager smgr = SmsManager.getDefault();
                                        smgr.sendTextMessage(phone, null, msg, null, null);
                                        Toast.makeText(MainActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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


                    button_count = 0;
                }
            }

        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent( MainActivity.this, SignUpActivity.class);
                startActivity(i);
                return true;

        }
        return false;
    }


}