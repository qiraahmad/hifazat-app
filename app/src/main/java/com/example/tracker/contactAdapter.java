package com.example.tracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class contactAdapter extends RecyclerView.Adapter<contactAdapter.contactViewHolder> {
    private ArrayList<contact> mContacts;
    public adapterInterface mAdapInt;

    public interface adapterInterface {
        void toFragComm(contact c1);
        void toFragCommDel(contact c1);
    }
    public contactAdapter()
    {
        mContacts = new ArrayList<>();
    }
    public contactAdapter(ArrayList<contact> exampleList, adapterInterface i) {
        mContacts = exampleList;
        mAdapInt = i;
    }

    public static class contactViewHolder extends RecyclerView.ViewHolder {
        public TextView mInitialsThumb;
        public TextView mNameView;
        public TextView mRelationView;
        public TextView mPhoneView;
        public ImageButton mDeleteContact;
        public ImageButton mEditContact;

        public contactViewHolder(View itemView) {
            super(itemView);
            mInitialsThumb = itemView.findViewById(R.id.thumbnail);
            mNameView = itemView.findViewById(R.id.name);
            mRelationView = itemView.findViewById(R.id.relation);
            mPhoneView = itemView.findViewById(R.id.mobNum);
            mDeleteContact = itemView.findViewById(R.id.delete_cont);
            mEditContact = itemView.findViewById(R.id.edit_cont);
        }
    }

    @Override
    public contactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_ec_item, parent, false);
        return new contactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(contactViewHolder holder, int position) {
        contact currentItem = mContacts.get(position);
        holder.mNameView.setText(currentItem.getName());
        holder.mPhoneView.setText(currentItem.getMobile());
        holder.mRelationView.setText(currentItem.getRelation());
        holder.mInitialsThumb.setText(currentItem.getInitials());
        holder.mDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                String Name = currentItem.getName();
                builder.setMessage("Do you really want to delete " + Name + " from emergency contacts?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mAdapInt.toFragCommDel(currentItem);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        holder.mEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapInt.toFragComm(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}