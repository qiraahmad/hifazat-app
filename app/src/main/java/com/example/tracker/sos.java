package com.example.tracker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sos extends Fragment {
    Button b;
    String mob;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sos, container, false);
        b = (Button) v.findViewById(R.id.record);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                startActivityForResult(intent, 1);
            }
        });

        Button fakeCall   = (Button)v.findViewById(R.id.BtnFakeCall);
        fakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fakeCallActivity = new Intent(getActivity(), FakeCall.class);
                startActivity(fakeCallActivity);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == 1) {
            PackageManager pm = getActivity().getPackageManager();
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("application/mp4");


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Contacts");
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            contact m = snapshot1.getValue(contact.class);
                            String phone = m.getMobile();
                            String Uid = m.getUser_id();
                            phone = phone.replaceFirst("0", "92");
                            Log.d("myTag", phone);



                            if (Uid.equals(firebaseUser.getEmail())) {
                                try {
                                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                                    waIntent.putExtra("jid", phone + "@s.whatsapp.net");
                                    waIntent.setPackage("com.whatsapp");

                                    waIntent.putExtra(Intent.EXTRA_STREAM, data.getData());
                                    startActivity(Intent.createChooser(waIntent, "Share with"));
                                }
                                catch (Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                            reference.keepSynced(true);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
    }
