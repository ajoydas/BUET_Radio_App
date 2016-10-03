package appinventor.ai_ppd1994.buetradioblue.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import appinventor.ai_ppd1994.buetradioblue.Application.BUETRadio;
import appinventor.ai_ppd1994.buetradioblue.Fragment.FragmentDrawerWebLoad;
import appinventor.ai_ppd1994.buetradioblue.R;

import static appinventor.ai_ppd1994.buetradioblue.R.id.webView;

public class WebLoad extends AppCompatActivity {
    protected FrameLayout webViewPlaceholder;
    private Toolbar mToolbar;
    private WebView browser ;
    String value=null;
    private FirebaseAuth mAuth;
    private FragmentDrawerWebLoad mDrawerFragment;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_web);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        setupDrawer();
        mAuth = FirebaseAuth.getInstance();
        value=getIntent().getExtras().getString("Url");
        browser=null;
        load();

    }
    private void setupDrawer() {
//        mToolbar = (Toolbar) findViewById(R.id.app_bar);
//        mContainerToolbar = (ViewGroup) findViewById(R.id.container_app_bar);
        //set the Toolbar as ActionBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setup the NavigationDrawer
        mDrawerFragment = (FragmentDrawerWebLoad)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
    }


    public void load()
    {
        try {
            progressDialog = ProgressDialog.show(WebLoad.this, "Connecting to the Archive..", "Please wait..", true, true);
            browser = (WebView) findViewById(webView);
            if (browser != null) {
                browser .setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        progressDialog.dismiss();
                    }
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
    protected void onPause() {
        super.onPause();

        try {
            if(browser!=null) {
                browser.stopLoading();
                browser.onPause();
                browser.pauseTimers();
                //browser.destroy();

            }
            progressDialog.dismiss();
        }
        catch (Exception e)
        {
            System.out.println("Can't end progress dialog in player");
        }
    }


    @Override
    public void onBackPressed() {
        if (browser.canGoBack()) {
            browser.goBack();
        } else {
            super.onBackPressed();

            try {
                if(browser!=null) {
                    browser.loadUrl("about:blank");
                    browser.stopLoading();
                    browser.onPause();
                    browser.pauseTimers();
                    //browser.destroy();

                }
                progressDialog.dismiss();
            }
            catch (Exception e)
            {
                System.out.println("Can't end progress dialog in player");
            }
            finish();
        }
    }


    public void onDrawerSlide(float slideOffset) {

    }
    public void onDrawerItemClicked(int index) {
        if (index == 0) {

            if(mAuth.getCurrentUser()!=null)
            {
                Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                finish();
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                intent.putExtra("From","Signin");
                finish();
                startActivity(intent);
            }
        }else if(index==1)
        {
            Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
            //intent.putExtra("Stream","http://87.117.217.103:38164");
            intent.putExtra("Player","Channel 1");
            finish();
            startActivity(intent);
        }
        else if(index==2)
        {
            Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
            //intent.putExtra("Stream","http://87.117.217.103:38164");
            intent.putExtra("Player","Channel 2");
            finish();
            startActivity(intent);
        }
        else if(index==5)
        {
            Intent intent=new Intent(getApplicationContext(),WebLoad.class);
            intent.putExtra("Url","https://soundcloud.com/buet-radio");
            finish();
            startActivity(intent);
        }
        else if(index==6)
        {
            Intent intent=new Intent(getApplicationContext(),WebLoad.class);
            intent.putExtra("Url","http://buetradio.com/archive.html");
            finish();
            startActivity(intent);
        }
        else if (index==3)
        {
            if(mAuth.getCurrentUser()!=null)
            {
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                finish();
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                intent.putExtra("From","Chatroom");
                finish();
                startActivity(intent);
            }
        }
        else if (index==4)
        {
            if(mAuth.getCurrentUser()!=null)
            {
                Intent intent=new Intent(getApplicationContext(),RequestActivity.class);
                finish();
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                intent.putExtra("From","Request");
                finish();
                startActivity(intent);
            }
        }
        else if (index==7)
        {
            Intent intent=new Intent(WebLoad.this,NotificationActivity.class);
            intent.putExtra("From","Info");
            //finish();
            startActivity(intent);
        }
        else if (index==8)
        {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "appinventor.ai_ppd1994.buetradioblue")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "appinventor.ai_ppd1994.buetradioblue")));
            }
        }
    }


}
