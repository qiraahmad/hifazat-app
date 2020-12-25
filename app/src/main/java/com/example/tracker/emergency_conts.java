package com.example.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class emergency_conts extends Fragment implements contactAdapter.adapterInterface{
    private RecyclerView currRecyclerView;
    private ArrayList<contact> contactList = new ArrayList<>();
    private RecyclerView.Adapter currAdapter;
    private ImageButton currNewContact;
    private RecyclerView.LayoutManager currLayoutManager;
    private SharedViewModel viewModel;
    private boolean edit;

    emergency_conts() {
        contactList.add(new contact("Sarosh Humayun", "Brother", "0324138971"));
        contactList.add(new contact("Qira Ahmad", "Sister", "03244297873"));
        contactList.add(new contact("Shankar Yadav", "Brother", "0323298473"));
        contactList.add(new contact("Babli Bhai", "Father", "03232883838"));

        currLayoutManager = new LinearLayoutManager(getContext());
        currAdapter = new contactAdapter(contactList, this);
    }

    emergency_conts(contact x) {
        contactList.add(x);
        contactList.add(new contact("Sarosh Humayun", "Brother", "0324138971"));
        contactList.add(new contact("Qira Ahmad", "Sister", "03244297873"));
        contactList.add(new contact("Shankar Yadav", "Brother", "0323298473"));
        contactList.add(new contact("Babli Bhai", "Father", "03232883838"));
        currLayoutManager = new LinearLayoutManager(getContext());
        currAdapter = new contactAdapter(contactList);
    }
    emergency_conts(contact old, contact new_cont) {
        contactList.add(new contact("Sarosh Humayun", "Brother", "0324138971"));
        contactList.add(new contact("Qira Ahmad", "Sister", "03244297873"));
        contactList.add(new contact("Shankar Yadav", "Brother", "0323298473"));
        contactList.add(new contact("Babli Bhai", "Father", "03232883838"));
        int x = contactList.indexOf(old);
        contactList.set(contactList.indexOf(old),new_cont);
        currLayoutManager = new LinearLayoutManager(getContext());
        currAdapter = new contactAdapter(contactList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.emergency_conts, container, false);

        currRecyclerView = thisView.findViewById(R.id.ecRecycler);
        currRecyclerView.setHasFixedSize(true);

        currRecyclerView.setLayoutManager(currLayoutManager);
        currRecyclerView.setAdapter(currAdapter);

        currNewContact = thisView.findViewById(R.id.add_new_ec);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        currNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setText("new_cont");
            }
        });
        return thisView;
    }

    @Override
    public void toFragComm(contact c1) {
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.setEditContact1(c1);
    }
}