package com.example.stocksim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DetailActivity extends AppCompatActivity {

    EditText searchStock;
    public static String stockCode;
    Button hsi, tips, list, profile, search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        hsi = (Button) findViewById(R.id.hsi);
        tips = (Button) findViewById(R.id.tips);
        list = (Button) findViewById(R.id.tradedList);
        profile = (Button) findViewById(R.id.profile);
        search = (Button)findViewById(R.id.buttonSearch);
        searchStock = (EditText)findViewById(R.id.searchStock);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockCode = searchStock.getText().toString();
                Intent i = new Intent(getApplicationContext(), stockSearchActivity.class);
                startActivity(i);
            }
        });

        hsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HSIActivity.class);
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
                Intent intent = new Intent(getApplicationContext(), ListLoginActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileNoLoginActivity.class);
                startActivity(intent);
            }
        });

    }

}
