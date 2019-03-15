package com.example.stocksim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//profile which user has login
public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private  FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mRef;
    private String userID;

    TextView aUsername, aEmail;
    TextView aBalance;

    Button detail;
    Button tips;
    Button list;
    Button hsi;
    Button logout;
    Button resetBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference().child(userID).child("Balance");

        detail = (Button) findViewById(R.id.hkDetailStock);
        tips = (Button) findViewById(R.id.tips);
        list = (Button) findViewById(R.id.tradedList);
        hsi = (Button) findViewById(R.id.hsi);
        logout = (Button)findViewById(R.id.logout);
        resetBalance = findViewById(R.id.resetBalance);

        aUsername = (TextView) findViewById(R.id.aUsername);
        aEmail = (TextView) findViewById(R.id.aEmail);
        aBalance = (TextView) findViewById(R.id.aBalance);

        resetBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.setValue(100000.000);
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                startActivity(intent);
            }
        });

        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TipsActivity.class);
                startActivity(intent);
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
            }
        });

        hsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HSIActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        aEmail.setText(user.getEmail().toString());


        aUsername.setText(user.getDisplayName().toString());

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                aBalance.setText(String.valueOf(dataSnapshot.getValue(Double.class)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
