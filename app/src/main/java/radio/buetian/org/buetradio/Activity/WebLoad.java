package radio.buetian.org.buetradio.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import radio.buetian.org.buetradio.Application.BUETRadio;
import radio.buetian.org.buetradio.R;

public class WebLoad extends AppCompatActivity {
    protected FrameLayout webViewPlaceholder;
    private Toolbar mToolbar;
    private WebView browser ;
    String value=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_web);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        value=getIntent().getExtras().getString("Url");
        browser=null;
        load();
    }

    public void load()
    {
        try {

            browser = (WebView) findViewById(R.id.webView);
            if (browser != null) {
                browser .setWebViewClient(new WebViewClient() {
                });
            }

            if (browser != null) {
                browser.getSettings().setJavaScriptEnabled(true);
                browser.loadUrl(value);
            }
            browser.setDownloadListener(new DownloadListener() {
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });/*
            browser.setDownloadListener(new DownloadListener() {
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "download");
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);

                }
            });*/
        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Page Can't be loaded.",Toast.LENGTH_LONG).show();
            finish();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        finish();
        startActivity(new Intent(BUETRadio.getAppContext(),WebLoad.class));
        //load();
    }
    /*@Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        // Save the state of the WebView
        browser.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore the state of the WebView
        browser.restoreState(savedInstanceState);
    }*/
    @Override
    public void onBackPressed() {
        if (browser.canGoBack()) {
            browser.goBack();
        } else {
            super.onBackPressed();
            browser.destroy();
            finish();
        }
    }
}
