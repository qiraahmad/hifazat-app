package com.example.tracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private SharedViewModel viewModel;
    int button_count=0;
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
                String no = "+923371424828";
                String msg = "Whassappp";
                try {
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(no, null, msg, null, null);
                    Toast.makeText(this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
                button_count=0;
            }

        }

        return super.dispatchKeyEvent(event);
    }
}
