package com.example.stocksim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class indexDialog extends AppCompatActivity {

    public static TextView mainlandCode, mainlandPrice, latestUpdate, turnover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_dialog);

        mainlandCode = findViewById(R.id.mainlandCode);
        mainlandPrice = findViewById(R.id.mainlandPrice);
        latestUpdate = findViewById(R.id.latestUpdate);
        turnover = findViewById(R.id.turnover);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fetchIndices f = new fetchIndices();
        f.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent i = new Intent(getApplicationContext(), worldIndices.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
