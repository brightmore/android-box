package com.virtualLink.msiptv.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class Weather extends Activity {

    // Declare variables
    WebView browser;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        browser = (WebView) findViewById(R.id.webWeather);
        progressBar = (ProgressBar) findViewById (R.id.progressbar);
        // We enable javascript and zoom
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setBuiltInZoomControls(true);

        // We enable plugins (flash)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            browser.getSettings().setPluginState(WebSettings.PluginState.ON);
        }
        browser.loadUrl("http://www.accuweather.com/en/gh/kumasi/176776/weather-forecast/176776");

        browser.setWebViewClient ( new  WebViewClient ()
        {
            // Prevent links from opening out our app in the android browser
            @Override
            public  boolean  shouldOverrideUrlLoading (WebView view, String url)
            {
                return  false ;
            }

        });

       // browser.setWebViewClient(new WebViewClient());
        browser.setWebChromeClient (new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                Weather.this.setProgress(progress * 1000);

                progressBar.incrementProgressBy(progress);

                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }


    class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageFinished(WebView view, String url) {


        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


