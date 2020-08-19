package com.stubborn.quotesuserapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText name, contact, email, password;
    private Button create;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        name = findViewById(R.id.create_name);
        contact = findViewById(R.id.create_contact);
        email = findViewById(R.id.create_email);
        password = findViewById(R.id.create_password);
        create = findViewById(R.id.create_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String NAME = name.getText().toString().trim();
                final String CONTACT = contact.getText().toString().trim();
                final String EMAIL = email.getText().toString().trim();
                final String PASSWORD = password.getText().toString().trim();


                if (NAME.isEmpty()) {
                    name.setError("Name cant be empty");
                    name.requestFocus();
                    return;
                }
                if (CONTACT.isEmpty()) {
                    contact.setError("Contact cant be empty");
                    contact.requestFocus();
                    return;
                }
                if (EMAIL.isEmpty()) {
                    email.setError("Email cant be empty");
                    email.requestFocus();
                    return;
                }

                if (PASSWORD.isEmpty()) {
                    password.setError("Password cant be empty");
                    password.requestFocus();
                    return;
                }


                firebaseAuth.createUserWithEmailAndPassword(EMAIL,PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        ModalCreateUser modalCreateUser = new ModalCreateUser(NAME, CONTACT, EMAIL, PASSWORD);

                        databaseReference.setValue(modalCreateUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void loginActivty(View view) {
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
}
