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

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button noLogin, createAccount, btnLogin;
    private EditText editEmail, editPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noLogin = (Button)findViewById(R.id.noLogin);
        createAccount = (Button)findViewById(R.id.createAccount);
        btnLogin = (Button)findViewById(R.id.login);
        editEmail = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);

        mAuth = FirebaseAuth.getInstance();


        noLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HSIActivity();

            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAccountActivity();

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Please wait...", "Progressing...", true);

               String email = editEmail.getText().toString();
               String password = editPassword.getText().toString();

                (mAuth.signInWithEmailAndPassword(email, password))
                       .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {

                               progressDialog.dismiss();

                               if(task.isSuccessful()){
                                   Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                   Intent i = new Intent(MainActivity.this, HSIActivity.class);
                                   i.putExtra("Email", mAuth.getCurrentUser().getEmail());
                                   startActivity(i);

                               }
                                else{
                                   Log.e("ERROR", task.getException().toString());
                                   Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                               }

                           }
                       });

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){

                    startActivity(new Intent(MainActivity.this, HSIActivity.class));
                    //start activity which user has login, i.e. HSIActivityq
                }

            }
        };


    }


    private void HSIActivity(){

        Intent intent = new Intent(getApplicationContext(), HSIActivity.class);
        startActivity(intent);
    }

    private void createAccountActivity(){
        Intent intent = new Intent(getApplicationContext(), createAccountActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }


}

