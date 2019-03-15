package com.example.stocksim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class createAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private EditText regEmail, regPassword, regUsername, regRetypePassword;
    private Button registration;

    private DatabaseReference databaseReference;

    public static HashMap<String, Object> map = new HashMap<>();
    public static HashMap<String, Object>map2 = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        regEmail = (EditText)findViewById(R.id.regEmail);
        regPassword = (EditText) findViewById(R.id.regPassword);
        registration = (Button)findViewById(R.id.Continue);
        regUsername = (EditText) findViewById(R.id.regUsername);
        regRetypePassword = (EditText)findViewById(R.id.regRetypePassword);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = ProgressDialog.show(createAccountActivity.this, "Please wait...", "progressing...", true);

                String email = regEmail.getText().toString();
                String password = regPassword.getText().toString();


                if(!email.matches("") && !password.matches("") && !regUsername.getText().toString().matches("") && !regRetypePassword.getText().toString().matches("")) {

                    if (password.equals(regRetypePassword.getText().toString())) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(createAccountActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        progressDialog.dismiss();

                                        if (task.isSuccessful()) {
                                            userProfile();

                                            map.put("Balance", 100000.000);

                                            DatabaseReference balanceRef = databaseReference.child(user.getUid());
                                            balanceRef.setValue(map);

                                            Toast.makeText(createAccountActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(createAccountActivity.this, MainActivity.class);
                                            startActivity(i);
                                        } else {
                                            Log.e("ERROR", task.getException().toString());
                                            Toast.makeText(createAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });
                    }
                    else {

                        progressDialog.dismiss();
                        Toast.makeText(createAccountActivity.this, "Please input same content in both password and retype password.", Toast.LENGTH_SHORT).show();

                    }

                }
                    else {
                    progressDialog.dismiss();
                    Toast.makeText(createAccountActivity.this, "Please input all the columns", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void userProfile(){

        user = mAuth.getCurrentUser();
        if(user != null ){

            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(regUsername.getText().toString().trim())
                    .build();

            user.updateProfile(userProfileChangeRequest);

        }

    }

}



