package com.example.tracker;

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

public class emergency_conts extends Fragment implements contactAdapter.adapterInterface{
    private RecyclerView currRecyclerView;
    private ArrayList<contact> contactList = new ArrayList<>();
    private RecyclerView.Adapter currAdapter;
    private ImageButton currNewContact;
    private RecyclerView.LayoutManager currLayoutManager;
    private SharedViewModel viewModel;
    private boolean edit;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Contacts");
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String name;
    private String phone;
    private String relation;
    private String Uid;
    //get contacts only
    emergency_conts() {
        currLayoutManager = new LinearLayoutManager(getContext());
        currAdapter = new contactAdapter(contactList, this);
    }
    //add a contact
    emergency_conts(contact x) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", x.getName());
        hashMap.put("mobile", x.getMobile());
        hashMap.put("relation", x.getRelation());
        hashMap.put("user_id", x.getUser_id());
        Uid = firebaseUser.getUid();
        contactList.add(x);
        reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Contact added successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "An error occurred, try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reference.keepSynced(true);
        currLayoutManager = new LinearLayoutManager(getContext());
    }
    //edit a contact
    emergency_conts(contact old, contact new_cont) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s1: snapshot.getChildren()) {
                    contact m = s1.getValue(contact.class);
                    assert m != null;
                    name = m.getName();
                    phone = m.getMobile();
                    relation = m.getRelation();
                    Uid = m.getUser_id();
                    if(old.getName().equals(name)
                            && old.getRelation().equals(relation)
                            && old.getMobile().equals(phone)
                            && old.getUser_id().equals(Uid))
                    {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("name", new_cont.getName());
                        hashMap.put("mobile", new_cont.getMobile());
                        hashMap.put("relation", new_cont.getRelation());
                        hashMap.put("user_id", new_cont.getUser_id());
                        s1.getRef().setValue(hashMap);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        currLayoutManager = new LinearLayoutManager(getContext());
    }
    //delete a contact
    emergency_conts(contact old, boolean del) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s1: snapshot.getChildren()) {
                    contact m = s1.getValue(contact.class);
                    assert m != null;
                    name = m.getName();
                    phone = m.getMobile();
                    relation = m.getRelation();
                    Uid = m.getUser_id();
                    if(old.getName().equals(name)
                            && old.getRelation().equals(relation)
                            && old.getMobile().equals(phone)
                            && old.getUser_id().equals(Uid))
                    {
                        s1.getRef().removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        currLayoutManager = new LinearLayoutManager(getContext());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.emergency_conts, container, false);

        currRecyclerView = thisView.findViewById(R.id.ecRecycler);
        currRecyclerView.setHasFixedSize(true);

        currRecyclerView.setLayoutManager(currLayoutManager);
        //currRecyclerView.setAdapter(currAdapter);

        currNewContact = thisView.findViewById(R.id.add_new_ec);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    contact m = snapshot1.getValue(contact.class);
                    assert m != null;
                    name = m.getName();
                    phone = m.getMobile();
                    relation = m.getRelation();
                    Uid = m.getUser_id();
                    if (Uid.equals(firebaseUser.getEmail())) {
                        contactList.add(m);
                        reference.keepSynced(true);
                    }
                    reference.keepSynced(true);
                }
                if (contactList.size() > 0) {
                    currAdapter = new contactAdapter(contactList, emergency_conts.this);
                    currRecyclerView.setAdapter(currAdapter);
                }
                else
                {
                    currAdapter = new contactAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    @Override
    public void toFragCommDel(contact c1) {
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.setDelContact1(c1);
    }
}