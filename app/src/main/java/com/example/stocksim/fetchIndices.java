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

public class fetchIndices extends AsyncTask<Void, Void, Void>{

    String indicesData = "";
    String code;
    String mainlandPrice;
    String latestUpdate;
    String turnover;

    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    BufferedReader bufferedReader;

    @Override
    protected Void doInBackground(Void... voids) {

        code = worldIndices.mainland;

        try {
            URL url = new URL("http://realtime-money18-cdn.on.cc/js/real/index/"+code+"_r.js");

            httpURLConnection = (HttpURLConnection) url.openConnection();

            inputStream = httpURLConnection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";

            while(line != null){

                line = bufferedReader.readLine();

                indicesData = indicesData + line;
            }

            String substrPrice = new String(indicesData.substring(14, indicesData.length()-2));

            JSONObject JO = new JSONObject(substrPrice);

            mainlandPrice = JO.getString("value");

            latestUpdate = JO.getString("ltt");

            turnover = JO.getString("turnover");


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
        if(code.equals("CSCSZQ"))
            indexDialog.mainlandCode.setText("CSCSZQ 深港通");
        if(code.equals("CSCSHQ"))
            indexDialog.mainlandCode.setText("CSCSHQ 沪港通");
        if(code.equals("SZSEASI"))
            indexDialog.mainlandCode.setText("SZSEASI 国证指数");
        if(code.equals("CSI300"))
            indexDialog.mainlandCode.setText("CSI300 滬深300");

        indexDialog.mainlandPrice.setText(mainlandPrice);
        indexDialog.latestUpdate.setText(latestUpdate);
        indexDialog.turnover.setText(turnover);
    }
}
