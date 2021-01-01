package com.example.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ContactEditor extends Fragment {
    private SharedViewModel viewModel;
    contact editContact;
    boolean editOrNew;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    ContactEditor(contact c1)
    {
        editContact = c1;
        editOrNew = true;
    }
    ContactEditor()
    {
        editOrNew = false;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View temp = inflater.inflate(R.layout.new_contact, container, false);
        Button confirm = temp.findViewById(R.id.confirm_button);
        Button cancel = temp.findViewById(R.id.cancel_button);
        EditText mNameView = temp.findViewById(R.id.edit_name);
        EditText mPhoneView = temp.findViewById(R.id.edit_num);
        EditText mRelationView = temp.findViewById(R.id.edit_rel);
        if(editOrNew)
        {
            mNameView.setText(editContact.getName());
            mPhoneView.setText(editContact.getMobile());
            mRelationView.setText(editContact.getRelation());
        }
        contact orig = new contact(mNameView.getText().toString(),
                mRelationView.getText().toString(),
                mPhoneView.getText().toString(),
                firebaseUser.getEmail());
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact edited;
                edited = new contact(mNameView.getText().toString(),
                        mRelationView.getText().toString(),
                        mPhoneView.getText().toString(),
                        firebaseUser.getEmail());
                if(edited.getName().isEmpty())
                {
                    mNameView.setError("Name field is Empty");
                    mNameView.requestFocus();
                }
                else if(edited.getRelation().isEmpty())
                {
                    mRelationView.setError("Relation field is Empty");
                    mRelationView.requestFocus();
                }
                else if(edited.getMobile().isEmpty())
                {
                    mPhoneView.setError("Phone field is Empty");
                    mPhoneView.requestFocus();
                }
                else {
                    if (!editOrNew) {
                        viewModel.setContact(edited);
                    } else {
                        editOrNew = false;
                        viewModel.setEditContact(orig, edited);
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setText("cancel");
            }
        });

        return temp;
    }
}
