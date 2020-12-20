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

public class LogInActivity extends AppCompatActivity {


    EditText emailId, password;
    Button signIn;
    TextView SignUp;
    FirebaseAuth FAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Hifazat");



        FAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        signIn = findViewById(R.id.button);
        SignUp = findViewById(R.id.textView);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFireBaseUser = FAuth.getCurrentUser();
                if (mFireBaseUser != null ) {
                    Toast.makeText(LogInActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(i);
                }

            }
        };

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pass  = password.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter email");
                    emailId.requestFocus();
                }
                else if (pass.isEmpty()) {
                    password.setError("Please enter password");
                    password.requestFocus();
                }
                else if (pass.isEmpty() && email.isEmpty()) {
                    Toast.makeText(LogInActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else if (!(pass.isEmpty() && email.isEmpty())) {
                    FAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LogInActivity.this, "Sign In Unsuccessful, try again!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(LogInActivity.this, "An error occurred, try again!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FAuth.addAuthStateListener(mAuthStateListener);
    }


}