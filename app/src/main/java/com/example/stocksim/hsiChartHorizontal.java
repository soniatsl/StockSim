package com.example.stocksim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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

public class hsiChartHorizontal extends AppCompatActivity {

    private static ShinobiChart shinobiChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsi_chart_horizontal);

        if (savedInstanceState == null){
            ChartFragment chartFragment =
                    (ChartFragment)getFragmentManager().findFragmentById(R.id.chartHorizontal);

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

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            fetchedHSIHorizontal f = new fetchedHSIHorizontal();
            f.execute();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent i = new Intent(getApplicationContext(), HSIActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public static void getHSIChartHorizontal(List<Data<Date, Double>> dataPoints){
        CandlestickSeries series = new CandlestickSeries();
        DataAdapter<Date, Double> dataAdapter = new SimpleDataAdapter<>();
        dataAdapter.addAll(dataPoints);
        series.setDataAdapter(dataAdapter);
        shinobiChart.addSeries(series);
    }
}
