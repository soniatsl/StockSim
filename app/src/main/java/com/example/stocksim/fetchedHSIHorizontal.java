package com.example.stocksim;

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

public class fetchedHSIHorizontal extends AsyncTask<Void, Void, Void> {

    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    BufferedReader bufferedReader;

    String HSIChartData = "";

    List<Data<Date, Double>> dataPoints = new ArrayList<>();


    @Override
    protected Void doInBackground(Void... voids) {

        try {
            URL url2 = new URL("http://money18.on.cc/chartdata/m3/price/HSI_price_m3.txt");

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
                for (int i = 0; i < openArray.length(); i++) {

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
        hsiChartHorizontal.getHSIChartHorizontal(dataPoints);

    }
}
