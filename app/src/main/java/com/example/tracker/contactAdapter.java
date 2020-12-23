package com.example.tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class contactAdapter extends RecyclerView.Adapter<contactAdapter.contactViewHolder> {
    private ArrayList<contact> mContacts;

    public static class contactViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPhotoView;
        public TextView mNameView;
        public TextView mRelationView;
        public TextView mPhoneView;

        public contactViewHolder(View itemView) {
            super(itemView);
            mPhotoView = itemView.findViewById(R.id.imageView);
            mNameView = itemView.findViewById(R.id.name);
            mRelationView = itemView.findViewById(R.id.relation);
            mPhoneView = itemView.findViewById(R.id.mobNum);
        }
    }

    public contactAdapter(ArrayList<contact> exampleList) {
        mContacts = exampleList;
    }

    @Override
    public contactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_ec_item, parent, false);
        contactViewHolder evh = new contactViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(contactViewHolder holder, int position) {
        contact currentItem = mContacts.get(position);
        //holder.mPhotoView.setImageResource(currentItem.getPhoto());
        holder.mNameView.setText(currentItem.getName());
        holder.mPhoneView.setText(currentItem.getPhone());
        holder.mRelationView.setText(currentItem.getRelation());
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}
