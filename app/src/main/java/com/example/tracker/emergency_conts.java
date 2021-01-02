package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class emergency_conts extends Fragment {
    private RecyclerView currRecyclerView;
    private ArrayList<contact> contactList = new ArrayList<>();
    private RecyclerView.Adapter currAdapter;
    private ImageButton currNewContact;
    private RecyclerView.LayoutManager currLayoutManager;
    private SharedViewModel viewModel;
    private boolean edit;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String a;
    String b;
    String c;


    emergency_conts() {
    }

    emergency_conts(contact x) {
        contactList.add(x);
        currLayoutManager = new LinearLayoutManager(getContext());
        currAdapter = new contactAdapter(contactList);
    }
    emergency_conts(contact old, contact new_cont) {

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

        currRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        reference = FirebaseDatabase.getInstance().getReference("Contacts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactList.clear();

                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    contact m = snapshot1.getValue(contact.class);

                    a= m.getName();
                    b = m.getMobile();
                    c = m.getRelation();
                    String d = m.getUser_id();
                    if(d.equals(firebaseUser.getEmail())) {
                        contactList.add(m);
                        reference.keepSynced(true);
                    }

                    if (contactList.size()>0) {
                        currAdapter = new contactAdapter(getContext(), contactList);
                        currRecyclerView.setAdapter(currAdapter);
                    }
                    reference.keepSynced(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        currNewContact = thisView.findViewById(R.id.add_new_ec);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        currNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewModel.setText("new_cont") ;
                Intent activity2Intent = new Intent(getContext(), AddContact.class);
                startActivity(activity2Intent);
            }
        });
        return thisView;
    }

}