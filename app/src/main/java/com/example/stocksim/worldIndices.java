package com.example.stocksim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class worldIndices extends AppCompatActivity {

    public static String mainland;
    Context context = this;

    ListView mainlandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_indices);

        mainlandList = findViewById(R.id.mainlandList);

        final String[] mainlandIndices= getResources().getStringArray(R.array.mainlandIndices);

        ArrayAdapter arrayAdapter = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                mainlandIndices);

        mainlandList.setAdapter(arrayAdapter);

        mainlandList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    mainland = "CSCSZQ";
                }

                if(position == 1){
                    mainland = "CSCSHQ";
                }

                if(position == 2){
                    mainland = "SZSEASI";
                }

                if(position == 3){
                    mainland = "CSI300";
                }

                Intent i = new Intent(getApplicationContext(), indexDialog.class);
                startActivity(i);

            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), TipsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);

    }
}
