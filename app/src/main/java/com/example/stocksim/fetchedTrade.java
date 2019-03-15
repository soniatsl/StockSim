package com.example.stocksim;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchedTrade extends AsyncTask <Void, Void, Void> {

    String priceData = "";
    String stockData = "";

    String stockName;
    String stockCode;
    String stockPrice;
    String preStockPrice;
    String stockChange;
    String stockPercentage;
    String lotSize;
    String costPerLot;


    double priceChange;
    double percentage;

    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    BufferedReader bufferedReader;

    @Override
    protected Void doInBackground(Void... voids) {

        stockCode = tradeDetailActivity.tradeStockCode.getText().toString().trim();

        try {

            URL url2 = new URL("http://money18.on.cc/js/daily/hk/quote/" +
                    stockCode +
                    "_d.js?t");

            URL url = new URL("http://money18.on.cc/js/real/quote/" +
                    stockCode +
                    "_r.js");

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

            costPerLot = String.valueOf(Double.parseDouble(stockPrice) * Double.parseDouble(lotSize));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        tradeDetailActivity.tradeStockName.setText(stockName);
        tradeDetailActivity.lotSize.setText(lotSize);
        tradeDetailActivity.currentPrice.setText(stockPrice);
    }
}

