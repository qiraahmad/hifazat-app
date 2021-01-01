package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddContact extends AppCompatActivity {
    FirebaseUser firebaseUser;
    EditText Name;
    EditText relation;
    EditText mob;
    Button confirm;
    Button cancel;
    DatabaseReference reference;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_contact);
        getSupportActionBar().setTitle("Hifazat");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String d = firebaseUser.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Name = findViewById(R.id.edit_name);
        relation = findViewById(R.id.edit_rel);
        mob = findViewById(R.id.edit_num);
        confirm = (Button) findViewById(R.id.confirm_button);
        cancel = (Button) findViewById(R.id.cancel_button);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a= Name.getText().toString();
                String b = mob.getText().toString();
                String c = relation.getText().toString();

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", a);
                hashMap.put("mobile", b);
                hashMap.put("relation", c);
                hashMap.put("user_id", d);
                String userid = firebaseUser.getUid();



                reference.child("Contacts").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddContact.this, "Contact added successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddContact.this, MainActivity.class));
                        }
                        else {
                            Toast.makeText(AddContact.this, "An error occurred, try again!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                reference.keepSynced(true);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddContact.this, emergency_contact_frag.class));
            }
        });



    }
}
