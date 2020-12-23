package com.example.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class directory extends Fragment {
    ListView listView;
    String contacts[]={"RESCUE", "POLICE", "AMBULANCE", "EDHI", "BOMB DISPOSAL", "BOMB DISPOSAL UNIT", "FIRE FIGHTING"};
    String numbers[]={"1122", "15", "115", "04237847050", "042-99212111", "5120516", "16"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.directory, container, false);
        listView = (ListView) v.findViewById(R.id.contacts);
        DirectoryContactAdapter customAdapter = new DirectoryContactAdapter(getContext(), contacts, numbers);
        listView.setAdapter(customAdapter);
        return v;
    }
}


