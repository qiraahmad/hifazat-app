package com.example.tracker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

public class sos extends Fragment {
    Button b;
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
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == 1) {


            PackageManager pm= getActivity().getPackageManager();
            try {
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("application/mp4");

                PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                waIntent.putExtra("jid", "923371424828" + "@s.whatsapp.net");
                waIntent.setPackage("com.whatsapp");

                waIntent.putExtra(Intent.EXTRA_STREAM,data.getData());
                startActivity(Intent.createChooser(waIntent, "Share with"));
            } catch (PackageManager.NameNotFoundException e) {
                //error message
            }
        }
    }
}
