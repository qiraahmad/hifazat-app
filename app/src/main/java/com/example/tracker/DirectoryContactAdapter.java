package com.example.tracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DirectoryContactAdapter extends BaseAdapter {
    Context context;
    String title[];
    String contact_no[];
    LayoutInflater inflater;

    public DirectoryContactAdapter(Context context, String[] title, String contact_no[]) {
        this.context = context;
        this.title = title;
        this.contact_no = contact_no;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.contact, null);
        TextView n = (TextView)  view.findViewById(R.id.tvname);
        TextView ph = (TextView)  view.findViewById(R.id.tvphone);
        n.setText(title[i]);
        ph.setText(contact_no[i]);
        ImageButton b = (ImageButton) view.findViewById(R.id.button);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PackageManager.PERMISSION_GRANTED);

        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = ph.getText().toString();
                String s = "tel:" + phone;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(s));
                v.getContext().startActivity(intent);
            }
        });
        return view;
    }
}