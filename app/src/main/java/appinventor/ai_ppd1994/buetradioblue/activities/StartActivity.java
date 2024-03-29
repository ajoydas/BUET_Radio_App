package appinventor.ai_ppd1994.buetradioblue.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;

import appinventor.ai_ppd1994.buetradioblue.adapters.GridviewAdapter;
import appinventor.ai_ppd1994.buetradioblue.BuildConfig;
import appinventor.ai_ppd1994.buetradioblue.fragments.FragmentDrawer;
import appinventor.ai_ppd1994.buetradioblue.objects.PlayerConnection;
import appinventor.ai_ppd1994.buetradioblue.R;

public class StartActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 123;


    private Toolbar mToolbar;
    private ViewGroup mContainerToolbar;
    private FragmentDrawer mDrawerFragment;
    private GridviewAdapter mAdapter;
    private ArrayList<String> listMenuItem;
    private ArrayList<Integer> listIcon;
    private static final int REQUEST_INVITE = 1;

    private GridView gridView;

    FirebaseAuth mAuth;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private Intent callIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //Showing Intro

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(StartActivity.this, MyIntro.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });



        //Redirect to notification
        try {
            if (getIntent().getExtras().getString("Message") != null) {
                Intent intent = new Intent(this, NotificationActivity.class);
                intent.putExtra("From","Notification");
                intent.putExtra("Message", getIntent().getExtras().getString("Message"));
                finish();
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




        // Start the thread
        t.start();

        mAuth = FirebaseAuth.getInstance();
        setupDrawer();

        prepareList();

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        PlayerConnection.setChannel1(mFirebaseRemoteConfig.getString("Channel1"));
        PlayerConnection.setChannel2(mFirebaseRemoteConfig.getString("Channel2"));
        PlayerConnection.setContactNumber(mFirebaseRemoteConfig.getString("ContactNumber"));
        PlayerConnection.setCallNumber(mFirebaseRemoteConfig.getString("CallNumber"));
        PlayerConnection.setSmsNumber(mFirebaseRemoteConfig.getString("SmsNumber"));

        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(StartActivity.this, "Fetch Successfull", Toast.LENGTH_SHORT).show();
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            //Toast.makeText(StartActivity.this, "Fetch Failed", Toast.LENGTH_SHORT).show();
                        }
                        PlayerConnection.setChannel1(mFirebaseRemoteConfig.getString("Channel1"));
                        PlayerConnection.setChannel2(mFirebaseRemoteConfig.getString("Channel2"));
                        PlayerConnection.setContactNumber(mFirebaseRemoteConfig.getString("ContactNumber"));
                        PlayerConnection.setCallNumber(mFirebaseRemoteConfig.getString("CallNumber"));
                        PlayerConnection.setSmsNumber(mFirebaseRemoteConfig.getString("SmsNumber"));
                    }
                });


        // prepared arraylist and passed it to the Adapter class
        mAdapter = new GridviewAdapter(this,listMenuItem, listIcon);

        // Set custom adapter to gridview
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);

        // Implement On Item click listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //Toast.makeText(StartActivity.this, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                if(position==0)
                {
                    Intent intent=new Intent(StartActivity.this,PlayerActivity.class);
                    intent.putExtra("Player","Channel 1");
                    //finish();
                    startActivity(intent);
                }
                else if(position==1)
                {
                    Intent intent=new Intent(StartActivity.this,PlayerActivity.class);
                    intent.putExtra("Player","Channel 2");
                    //finish();
                    startActivity(intent);
                }
                else if (position==2)
                {
                    if(mAuth.getCurrentUser()!=null)
                    {
                        Intent intent=new Intent(StartActivity.this,ChatActivity.class);
                        finish();
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(StartActivity.this,SignInActivity.class);
                        intent.putExtra("From","Chatroom");
                        finish();
                        startActivity(intent);
                    }
                }
                else if (position==3)
                {
                    if(mAuth.getCurrentUser()!=null)
                    {
                        Intent intent=new Intent(StartActivity.this,RequestActivity.class);
                        //finish();
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(StartActivity.this,SignInActivity.class);
                        intent.putExtra("From","Request");
                        //finish();
                        startActivity(intent);
                    }
                }
                else if(position==4)
                {
                    Intent intent=new Intent(StartActivity.this,WebLoad.class);
                    intent.putExtra("Url","https://soundcloud.com/buet-radio");
                    //finish();
                    startActivity(intent);
                }
                else if(position==5)
                {
                    Intent intent=new Intent(StartActivity.this,WebLoad.class);
                    intent.putExtra("Url","http://buetradio.com/archive.html");
                    //finish();
                    startActivity(intent);
                }
                else if (position==6)
                {
                    if(mAuth.getCurrentUser()!=null)
                    {
                        Intent intent=new Intent(StartActivity.this,ProfileActivity.class);
                        //finish();
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(StartActivity.this,SignInActivity.class);
                        intent.putExtra("From","Signin");
                        //finish();
                        startActivity(intent);
                    }
                }
                else if (position==7)
                {
                    Intent intent=new Intent(StartActivity.this,NotificationActivity.class);
                    intent.putExtra("From","Events");
                    //finish();
                    startActivity(intent);
                }

            }
        });


        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,
                PhoneStateListener.LISTEN_CALL_STATE);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted!
                    // Perform the action
                    startActivity(callIntent);
                } else {
                    // Permission was denied
                    // :(
                    // Gracefully handle the denial
                    Toast.makeText(StartActivity.this, "Requesting Call Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    void makeCallfunc()
    {
        callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(PlayerConnection.getCallNumber()));
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    // We will need to request the permission
                    System.out.println("Inside version Requesting.....");

                    System.out.println("Requesting.....");
                    ActivityCompat.requestPermissions(StartActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an app-defined int constant
                } else {
                    // The permission is granted, we can perform the action
                    startActivity(callIntent);
                }
            }
            else
            {
                startActivity(callIntent);
            }
        } catch (Exception e) {
            Toast.makeText(StartActivity.this, "Call Permission Denied!", Toast.LENGTH_LONG).show();
        }
    }

    public void prepareList() {
        listMenuItem = new ArrayList<String>();

        listMenuItem.add("Channel 1");
        listMenuItem.add("Channel 2");
        listMenuItem.add("Chat Room");
        listMenuItem.add("eSMS");
        listMenuItem.add("Radio Hits");
        listMenuItem.add("Archive");
        listMenuItem.add("Profile");
        listMenuItem.add("Events");

        listIcon = new ArrayList<Integer>();
        listIcon.add(R.drawable.channels);
        listIcon.add(R.drawable.channels);
        listIcon.add(R.drawable.chatroom);
        listIcon.add(R.drawable.esms);
        listIcon.add(R.drawable.radiohits);
        listIcon.add(R.drawable.archive);
        listIcon.add(R.drawable.profile);
        listIcon.add(R.drawable.events);


    }



    public View getContainerToolbar() {
        return mContainerToolbar;
    }
    private void setupDrawer() {
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mContainerToolbar = (ViewGroup) findViewById(R.id.container_app_bar);
        //set the Toolbar as ActionBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setup the NavigationDrawer
        mDrawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
    }

    public void onDrawerSlide(float slideOffset) {

    }
    public void onDrawerItemClicked(int index) {
        if (index == 0) {

            if(mAuth.getCurrentUser()!=null)
            {
                Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                //finish();
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                intent.putExtra("From","Signin");
                //finish();
                startActivity(intent);
            }
        }else if(index==1)
        {
            Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
            intent.putExtra("Player","Channel 1");
            //finish();
            startActivity(intent);
        }
        else if(index==2)
        {
            Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
            intent.putExtra("Player","Channel 2");
            //finish();
            startActivity(intent);
        }
        else if(index==5)
        {
            Intent intent=new Intent(getApplicationContext(),WebLoad.class);
            intent.putExtra("Url","https://soundcloud.com/buet-radio");
            //finish();
            startActivity(intent);
        }
        else if(index==6)
        {
            Intent intent=new Intent(getApplicationContext(),WebLoad.class);
            intent.putExtra("Url","http://buetradio.com/archive.html");
            //finish();
            startActivity(intent);
        }
        else if (index==3)
        {
            if(mAuth.getCurrentUser()!=null)
            {
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                //finish();
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                intent.putExtra("From","Chatroom");
                //finish();
                startActivity(intent);
            }
        }
        else if (index==4)
        {
            if(mAuth.getCurrentUser()!=null)
            {
                Intent intent=new Intent(getApplicationContext(),RequestActivity.class);
                //finish();
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                intent.putExtra("From","Request");
                //finish();
                startActivity(intent);
            }
        }
        else if (index==7)
        {
            Intent intent=new Intent(StartActivity.this,NotificationActivity.class);
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


    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended, need detect flag
                // from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite_menu:
                sendInvitation();
                return true;
            case R.id.exit_menu:
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                //System.exit(0);
                PlayerConnection.getMediaPlayer().stop();
                PlayerConnection.setIsRecording(false);

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.fbpage_menu:
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(this);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
                return true;
            case R.id.remoteconfig_menu:
                mFirebaseRemoteConfig.fetch(0)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(StartActivity.this, "Fetch Successfull", Toast.LENGTH_SHORT).show();
                                    mFirebaseRemoteConfig.activateFetched();
                                } else {
                                    Toast.makeText(StartActivity.this, "Fetch Failed! Please Try Again.", Toast.LENGTH_SHORT).show();
                                }
                                PlayerConnection.setChannel1(mFirebaseRemoteConfig.getString("Channel1"));
                                PlayerConnection.setChannel2(mFirebaseRemoteConfig.getString("Channel2"));
                                PlayerConnection.setContactNumber(mFirebaseRemoteConfig.getString("ContactNumber"));
                                PlayerConnection.setCallNumber(mFirebaseRemoteConfig.getString("CallNumber"));
                                PlayerConnection.setSmsNumber(mFirebaseRemoteConfig.getString("SmsNumber"));
                            }
                        });
                return true;
            case R.id.contact_menu:
                final CharSequence[] items = { "Call Us", "Email Us",
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                builder.setTitle("Contact Us!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Call Us")) {
                            //System.out.println("Call us clicked");
                            dialog.dismiss();
                            makeCallfunc();
                        } else if (items[item].equals("Email Us")) {
                            //System.out.println("Email us clicked");
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"buetradio@gmail.com"});
                            i.putExtra(Intent.EXTRA_SUBJECT, "Contact");
                            i.putExtra(Intent.EXTRA_TEXT   , "Please write here");
                            try {
                                startActivity(Intent.createChooser(i, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(StartActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }

                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendInvitation() {
        Intent intent = new AppInviteInvitation.IntentBuilder(" Invitation")
                .setMessage("Please join BUET Radio community and stay tuned")
                .setCallToActionText("Call to action")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

            PlayerConnection.getMediaPlayer().stop();
            PlayerConnection.setIsRecording(false);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//            finish();
//            android.os.Process.killProcess(android.os.Process.myPid());
            //System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public static String FACEBOOK_URL = "https://www.facebook.com/BUETradio";
    public static String FACEBOOK_PAGE_ID = "1657458381203485";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }


}
