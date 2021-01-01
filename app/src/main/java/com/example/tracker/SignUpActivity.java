package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    EditText emailId, password, mobile, rpassword;
    Button signUp;
    TextView SignIn;
    FirebaseAuth FAuth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getSupportActionBar().setTitle("Hifazat");


        FAuth = FirebaseAuth.getInstance();

        if (FAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
        emailId = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        rpassword = findViewById(R.id.repeatPassword);
        mobile = findViewById(R.id.editTextPhone);
        signUp = findViewById(R.id.button);
        SignIn = findViewById(R.id.textView);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pass  = password.getText().toString();
                String rpass = rpassword.getText().toString();
                String mob = mobile.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter email");
                    emailId.requestFocus();
                }
                else if (pass.isEmpty()) {
                    password.setError("Please enter password");
                    password.requestFocus();
                }
                else if (mob.isEmpty()) {
                    mobile.setError("Please enter phone number");
                    mobile.requestFocus();
                }
                else if (rpass.isEmpty()) {
                    rpassword.setError("Please enter password again");
                    rpassword.requestFocus();
                }
                else if ((pass.isEmpty() && email.isEmpty()) && mob.isEmpty() && rpass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else if (!(pass.isEmpty() && email.isEmpty() && mob.isEmpty() && rpass.isEmpty())) {
                    if (rpass.equals(pass)) {
                        FAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Sign Up Unsuccessful, try again!", Toast.LENGTH_SHORT).show();
                                } else {
                                    firebaseUser = FAuth.getCurrentUser();
                                    String userid = firebaseUser.getUid();
                                    reference = FirebaseDatabase.getInstance().getReference("Users");
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("userid", userid);
                                    hashMap.put("mobile", mob);
                                    hashMap.put("email", email);

                                    reference.child(userid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(com.example.tracker.SignUpActivity.this, "Sign Up successful!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(com.example.tracker.SignUpActivity.this, MainActivity.class));
                                            }
                                        }
                                    });
                                    reference.keepSynced(true);

                                }
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(SignUpActivity.this, "An error occurred, try again!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });
    }
}