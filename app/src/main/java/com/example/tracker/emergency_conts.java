package com.example.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class emergency_conts extends Fragment {
    private RecyclerView currRecyclerView;
    private RecyclerView.Adapter currAdapter;
    private RecyclerView.LayoutManager currLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.emergency_conts, container, false);

        ArrayList<contact> contactList = new ArrayList<>();
        contactList.add(new contact(0, "Sarosh Humayun","Brother", "0324138971"));
        contactList.add(new contact(0, "Qira Ahmad","Sister", "03244297873"));
        contactList.add(new contact(0, "Shankar Yadav","Brother", "0323298473"));
        contactList.add(new contact(0, "Babli Bhai","Father", "03232883838"));

        currRecyclerView = thisView.findViewById(R.id.ecRecycler);
        currRecyclerView.setHasFixedSize(true);
        currLayoutManager = new LinearLayoutManager(getContext());
        currAdapter = new contactAdapter(contactList);

        currRecyclerView.setLayoutManager(currLayoutManager);
        currRecyclerView.setAdapter(currAdapter);
        return thisView;
    }
}
