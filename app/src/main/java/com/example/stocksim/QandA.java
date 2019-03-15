package com.example.stocksim;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class QandA extends AppCompatActivity {

    ListView qAndAListView;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qand);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        qAndAListView = findViewById(R.id.qAndAListView);

        final String[] qAndA = getResources().getStringArray(R.array.qAndA);
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                qAndA);

        qAndAListView.setAdapter(arrayAdapter);

        qAndAListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_q_dialog);
                dialog.setTitle(parent.getItemAtPosition(position).toString());

                TextView information = dialog.findViewById(R.id.information);

                if (position == 0){
                    information.setText(
                            "- Pre-opening auction session: 9:00 am to 9:30 am\n\n" +
                            "- Opening price of a security is reported shortly after 9:20 am\n\n" +
                            "- Morning continuous trading session: 09:30 am to 12:00 pm\n\n" +
                            "- Afternoon continuous trading session: 1:00 pm to 4:00 pm\n\n" +
                            "- Closing price is reported as the median of five price snapshots taken from 3:59 to 4:00 pm every 15 seconds\n\n" +
                            "- Closing auction session: 4:00 pm to 4:10 pm\n\n"
                    );
                }

                if (position == 1){
                    information.setText("The current Hang Seng Index is calculated from this formula:\n\n"+
                            "∑(Pt x IS x FAF x CF)" +
                            " / " +
                            "∑(Pt-1 x IS x FAF x CF)" +
                            "x\tYesterday’s Closing Index\n\n"+
                            "Descriptions on parameters:\n\n" +
                            "- P(t)：Current Price at Day t\n" +
                            "- P(t-1)：Closing Price at Day (t-1)\n" +
                            "- IS：Issued Shares (Only H-share portion is taken into calculation in case of H-share constituents.)\n" +
                            "- FAF：Freefloat-adjusted Factor, which is between 0 and 1, adjusted quarterly\n" +
                            "- CF：Cap Factor, which is between 0 and 1, adjusted quarterly\n");
                }

                if (position == 2){
                    information.setText("Hang Seng Finance Sub-index: \n\n" +
                            "0939 China Construction Bank\n" +
                            "0005 HSBC Holdings plc\n" +
                            "1299 AIA Group Limited\n" +
                            "1398 Industrial and Commercial Bank of China\n" +
                            "2388 BOC Hong Kong (Holdings) Ltd\n" +
                            "2318 Ping An Insurance\n" +
                            "3988 Bank of China Ltd\n" +
                            "3328 Bank of Communications Ltd\n" +
                            "0011 Hang Seng Bank Ltd\n" +
                            "2628 China Life\n" +
                            "0388 HKEx Limited\n" +
                            "0023 Bank of East Asia, Ltd\n" +
                            "Hang Seng Utilities Sub-index\n" +
                            "0002 CLP Holdings Limited\n" +
                            "0003 Hong Kong and China Gas Company Limited\n" +
                            "0006 Power Assets Holdings Limited\n" +
                            "0836 China Resources Power\n" +
                            "1038 Cheung Kong Infrastructure Holdings Limited\n" +
                            "Hang Seng Properties Sub-index\n" +
                            "0016 Sun Hung Kai Properties Limited\n" +
                            "1113 CK Property Holdings Limited\n" +
                            "0688 China Overseas Land & Investment Limited\n" +
                            "0004 Wharf (Holdings) Limited\n" +
                            "0012 Henderson Land Development Company Limited\n" +
                            "1109 China Resources Land Limited\n" +
                            "0101 Hang Lung Properties Limited\n" +
                            "0017 New World Development Company Limited\n" +
                            "0083 Sino Land Company Limited\n" +
                            "0823 The Link REIT\n" +
                            "Hang Seng Commerce & Industry Sub-index\n" +
                            "0941 China Mobile Ltd\n" +
                            "0700 Tencent Holdings Limited\n" +
                            "0883 CNOOC Ltd\n" +
                            "0001 CK Hutchison Holdings Limited\n" +
                            "0267 CITIC Pacific Ltd\n" +
                            "1928 Sands China\n" +
                            "0762 China Unicom (Hong Kong) Limited\n" +
                            "0027 Galaxy Entertainment Group Ltd.\n" +
                            "0066 MTR Corporation Ltd\n" +
                            "0857 PetroChina Company Limited\n" +
                            "0386 Sinopec Corp\n" +
                            "0151 Want Want China Holdings Ltd\n" +
                            "0992 Lenovo Group\n" +
                            "0322 Tingyi (Cayman Islands) Holding Corp\n" +
                            "1044 Hengan International Group Co. Ltd\n" +
                            "0019 Swire Group\n" +
                            "1088 China Shenhua Energy\n" +
                            "1880 Belle International\n" +
                            "0144 China Merchants Holdings (International)\n" +
                            "0293 Cathay Pacific Airways Ltd\n" +
                            "2319 Mengniu Dairy\n" +
                            "0135 Kunlun Energy\n" +
                            "0175 Geely Auto\n");
                }

                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent i = new Intent(getApplicationContext(), TipsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
