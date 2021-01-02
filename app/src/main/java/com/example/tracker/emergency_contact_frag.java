package com.example.tracker;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

public class emergency_contact_frag extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new emergency_conts()).commit();}
    }
}