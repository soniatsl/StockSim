package com.example.stocksim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class News extends AppCompatActivity {

    WebView webSCMP;
    WebView webNow;
    WebView webSingTao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        webSCMP = findViewById(R.id.webSCMP);

        webSCMP.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webSCMP.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSCMP.loadUrl("http://www.scmp.com/business");

        webNow = findViewById(R.id.webNow);

        webNow.setWebViewClient(new WebViewClient());

        WebSettings webSettingsNow = webSCMP.getSettings();

        webSettingsNow.setJavaScriptEnabled(true);
        webNow.loadUrl("https://news.now.com/home/finance");

        webSingTao = findViewById(R.id.webSingTao);

        webSingTao.setWebViewClient(new WebViewClient());

        WebSettings webSettingsSingTao = webSCMP.getSettings();

        webSettingsSingTao.setJavaScriptEnabled(true);
        webSingTao.loadUrl("http://std.stheadline.com/instant/articles/listview/財經/");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
