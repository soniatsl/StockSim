package com.example.stocksim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shinobicontrols.charts.CandlestickSeries;
import com.shinobicontrols.charts.ChartFragment;
import com.shinobicontrols.charts.Data;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DateTimeAxis;
import com.shinobicontrols.charts.NumberAxis;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.SimpleDataAdapter;

import java.util.Date;
import java.util.List;

public class HSIActivity extends AppCompatActivity {

    Button detail;
    Button tips;
    Button list;
    Button profile;
    Button horizontalView;

    public static TextView currentHSI;
    public static TextView lastUpdateDate;
    public static TextView HSIChange;
    public static TextView HSIPercentage;

    private static ShinobiChart shinobiChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsi);

        detail = (Button) findViewById(R.id.hkDetailStock);
        tips = (Button) findViewById(R.id.tips);
        list = (Button) findViewById(R.id.tradedList);
        profile = (Button) findViewById(R.id.profile);
        horizontalView = (Button)findViewById(R.id.horizontalView);

        currentHSI = (TextView)findViewById(R.id.currentHSI);
        lastUpdateDate = (TextView)findViewById(R.id.lastUpdateDate);
        HSIChange = (TextView)findViewById(R.id.HSIChange);
        HSIPercentage = (TextView)findViewById(R.id.HSIPercentage);

        if (savedInstanceState == null){
            ChartFragment chartFragment =
                    (ChartFragment)getFragmentManager().findFragmentById(R.id.chart);

            shinobiChart = chartFragment.getShinobiChart();
            shinobiChart.setTrialKey("c3Ji-fVQG-AQUA-ayJw-YH5e-A1VR");
            shinobiChart.setTitle("HSI / Date");

            DateTimeAxis xAxis = new DateTimeAxis();
            xAxis.enableGesturePanning(true);
            xAxis.enableGestureZooming(true);
            shinobiChart.addXAxis(xAxis);

            NumberAxis yAxis = new NumberAxis();
            yAxis.enableGesturePanning(true);
            yAxis.enableGestureZooming(true);
            shinobiChart.addYAxis(yAxis);

            fetchedHSI fh = new fetchedHSI();
            fh.execute();
        }

        horizontalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), hsiChartHorizontal.class);
                startActivity(i);
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

    public static void getHSIChart(List<Data<Date, Double>> dataPoints){
        CandlestickSeries series = new CandlestickSeries();
        DataAdapter<Date, Double> dataAdapter = new SimpleDataAdapter<>();
        dataAdapter.addAll(dataPoints);
        series.setDataAdapter(dataAdapter);
        shinobiChart.addSeries(series);
    }

}
