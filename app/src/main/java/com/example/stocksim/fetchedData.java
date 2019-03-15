package com.example.stocksim;

import android.graphics.Color;
import android.os.AsyncTask;

import com.shinobicontrols.charts.Data;
import com.shinobicontrols.charts.DataPoint;

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

public class fetchedData extends AsyncTask<Void, Void, Void>{

    String priceData = "";
    String stockData = "";
    String chartData = "";
    String lineChartData = "";

    String stockPrice;
    String stockDate;
    String stockName;
    String preStockPrice;
    String stockChange;
    String stockPercentage;
    String open;
    String high;
    String low;
    String volume;
    String lotSize;
    String costPerLot;

    double priceChange;
    double percentage;

    String stockCode;

    List<Data<Date,Double>> dataPoints = new ArrayList<>();

    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    BufferedReader bufferedReader;

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            stockCode = DetailActivity.stockCode;

            URL url2 = new URL("http://money18.on.cc/js/daily/hk/quote/" +
                    stockCode +
                    "_d.js?t");

            URL url = new URL("http://money18.on.cc/js/real/quote/" +
                    stockCode +
                    "_r.js");

            URL url3 = new URL("http://money18.on.cc/chartdata/m3/price/"+
                    stockCode+
                    "_price_m3.txt");

            URL url4 = new URL("http://money18.on.cc/chartdata/d1/price/"+
                    stockCode+
                    "_price_d1.txt");

            //for url1
            httpURLConnection = (HttpURLConnection) url.openConnection();

            inputStream = httpURLConnection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";

            while(line != null){

                line = bufferedReader.readLine();

                priceData = priceData + line;
            }
            String substrPrice = new String(priceData.substring(14, priceData.length()-2));

            JSONObject JO = new JSONObject(substrPrice);

            stockPrice = JO.getString("np");

            stockDate = JO.getString("ltt");

            high = JO.getString("dyh");

            low = JO.getString("dyl");

            volume = JO.getString("vol");


            //for url2
            httpURLConnection = (HttpURLConnection)url2.openConnection();

            inputStream = httpURLConnection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line2 = "";

            while(line2 != null){
                line2 = bufferedReader.readLine();
                stockData = stockData + line2;
            }

            String substrData = new String(stockData.substring(13, stockData.length()-1));

            JSONObject JO2 = new JSONObject(substrData);

            stockName = JO2.getString("name");

            preStockPrice = JO2.getString("preCPrice");

            priceChange = Double.parseDouble(stockPrice) - Double.parseDouble(preStockPrice);

            percentage = (priceChange/Double.parseDouble(stockPrice))*100;

            if (priceChange > 0)
                stockChange = String.valueOf(String.format("%.3f", priceChange));
            else if(priceChange < 0)
                stockChange = String.valueOf(String.format("%.3f", -priceChange));

            stockPercentage = String.valueOf(String.format("%.2f", percentage));

            lotSize = JO2.getString("lotSize");

            int lotSizeInt = ((int) Double.parseDouble(lotSize));

            lotSize = String.valueOf(lotSizeInt);

            costPerLot = String.format("%.3f", Double.parseDouble(stockPrice) * Double.parseDouble(lotSize));

            //for url4
            httpURLConnection = (HttpURLConnection)url4.openConnection();
            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line4 = "";
            while(line4 != null){
                line4 = bufferedReader.readLine();
                lineChartData = lineChartData + line4;
            }

            JSONObject lineChart = new JSONObject(lineChartData);

            JSONObject price = lineChart.getJSONObject("price");
            JSONArray priceArray = price.getJSONArray("values");

            JSONObject time = lineChart.getJSONObject("x_axis");
            JSONArray timeArray = time.getJSONArray("labels");

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);

            for (int i = 0; i < timeArray.length(); i++)
                dataPoints.add(new DataPoint<>(formatter.parse(timeArray.getString(i)), priceArray.getDouble(i)));

            open = priceArray.getString(0);

        }catch(MalformedURLException e){
            e.printStackTrace();


        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        stockSearchActivity.stockPrice.setText(stockPrice);
        stockSearchActivity.stockDate.setText(stockDate);
        stockSearchActivity.stockName.setText(stockName);
        stockSearchActivity.stockPercentage.setText("(" + stockPercentage + "%" + ")");
        if(priceChange > 0) {
            stockSearchActivity.stockChange.setText('\u2191' + stockChange);
            stockSearchActivity.stockChange.setTextColor(Color.GREEN);
            stockSearchActivity.stockPercentage.setTextColor(Color.GREEN);
        }
        else if (priceChange < 0){
            stockSearchActivity.stockChange.setText('\u2193' + stockChange);
            stockSearchActivity.stockChange.setTextColor(Color.RED);
            stockSearchActivity.stockPercentage.setTextColor(Color.RED);
        }
        else{
            stockSearchActivity.stockPercentage.setTextColor(Color.BLACK);
            stockSearchActivity.stockChange.setTextColor(Color.BLACK);
        }
        stockSearchActivity.open.setText(open);
        stockSearchActivity.volume.setText(volume);
        stockSearchActivity.high.setText(high);
        stockSearchActivity.low.setText(low);
        stockSearchActivity.lotSize.setText(lotSize);
        stockSearchActivity.costPerLot.setText(costPerLot);

        stockSearchActivity.getStockChart(dataPoints);
    }

}
