package com.example.stocksim;

import android.graphics.Color;
import android.os.AsyncTask;

import com.shinobicontrols.charts.Data;
import com.shinobicontrols.charts.MultiValueDataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class fetchedHSI extends AsyncTask<Void, Void, Void>{

    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    BufferedReader bufferedReader;

    String HSIData = "";
    String HSIChartData = "";

    String currentHSI;
    String lastUpdateDate;
    String HSIChange;
    String HSIPercentage;

    double HSIChangeDouble;
    double HSIPercentageDouble;

    List<Data<Date, Double>> dataPoints = new ArrayList<>();

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            URL url = new URL ("http://realtime-money18-cdn.on.cc/js/real/index/HSI_r.js");

            URL url2 = new URL("http://money18.on.cc/chartdata/m3/price/HSI_price_m3.txt");

            //for url1
            httpURLConnection = (HttpURLConnection)url.openConnection();

            inputStream = httpURLConnection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";

            while(line != null){

                line = bufferedReader.readLine();

                HSIData = HSIData + line;

            }

            String substr = new String(HSIData.substring(12, HSIData.length()-2));

            JSONObject JO = new JSONObject(substr);

            currentHSI = JO.getString("value");

            lastUpdateDate = JO.getString("ltt");

            String lastHSI = JO.getString("pc");

            HSIChangeDouble = Double.parseDouble(currentHSI) - Double.parseDouble(lastHSI);

            if (HSIChangeDouble > 0)
                HSIChange = String.valueOf(String.format("%.2f", HSIChangeDouble));
            else if (HSIChangeDouble < 0)
                HSIChange = String.valueOf(String.format("%.2f", -HSIChangeDouble));

            HSIPercentageDouble = (HSIChangeDouble/Double.parseDouble(lastHSI))*100;

            HSIPercentage = String.valueOf(String.format("%.2f", HSIPercentageDouble));

            //for url2
            httpURLConnection = (HttpURLConnection)url2.openConnection();
            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line2 = "";
            while(line2 != null){
                line2 = bufferedReader.readLine();
                HSIChartData = HSIChartData + line2;
            }

            JSONObject HSIchart = new JSONObject(HSIChartData);

            //for high value
            JSONObject high = HSIchart.getJSONObject("high");
            JSONArray highArray = high.getJSONArray("values");

            //for low value
            JSONObject low = HSIchart.getJSONObject("low");
            JSONArray lowArray = low.getJSONArray("values");

            //for open value
            JSONObject open = HSIchart.getJSONObject("open");
            JSONArray openArray = open.getJSONArray("values");

            //for close value
            JSONObject close = HSIchart.getJSONObject("price");
            JSONArray closeArray = close.getJSONArray("values");

            //date
            JSONObject date = HSIchart.getJSONObject("x_axis");
            JSONArray dateArray = date.getJSONArray("labels");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);

            try{
                for (int i = 37; i < openArray.length(); i++) {

                    MultiValueDataPoint<Date, Double> dataPoint = new MultiValueDataPoint<>(

                            formatter.parse(dateArray.getString(i)),
                            lowArray.getDouble(i),
                            highArray.getDouble(i),
                            openArray.getDouble(i),
                            closeArray.getDouble(i)

                    );
                    dataPoints.add(dataPoint);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        HSIActivity.currentHSI.setText(currentHSI);
        HSIActivity.lastUpdateDate.setText(lastUpdateDate);
        HSIActivity.HSIPercentage.setText("(" + HSIPercentage + "%" + ")");
        if(HSIChangeDouble > 0){
            HSIActivity.HSIChange.setText('\u2191' + HSIChange);
            HSIActivity.HSIChange.setTextColor(Color.GREEN);
            HSIActivity.HSIPercentage.setTextColor(Color.GREEN);
        }
        else if (HSIChangeDouble < 0){
            HSIActivity.HSIChange.setText('\u2193' + HSIChange);
            HSIActivity.HSIChange.setTextColor(Color.RED);
            HSIActivity.HSIPercentage.setTextColor(Color.RED);
        }
        else{
            HSIActivity.HSIChange.setTextColor(Color.BLACK);
            HSIActivity.HSIPercentage.setTextColor(Color.BLACK);
        }
        HSIActivity.getHSIChart(dataPoints);
    }
}
