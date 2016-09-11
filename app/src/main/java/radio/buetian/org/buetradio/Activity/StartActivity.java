package radio.buetian.org.buetradio.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import radio.buetian.org.buetradio.Adapter.GridviewAdapter;
import radio.buetian.org.buetradio.Fragment.FragmentDrawer;
import radio.buetian.org.buetradio.R;

public class StartActivity extends AppCompatActivity {

    private Button play, stop, record, signin, signup;
    private MediaPlayer mPlayer;
    private String stream_url = "http://87.117.217.103:38164";

    private InputStream inputStream;
    private FileOutputStream fileOutputStream;
    private String outputSource;
    private MediaRecorder mediaRecorder;
    private long startTime;
    private boolean isRecording;
    private long stopTime;
    private Toolbar mToolbar;
    private ViewGroup mContainerToolbar;
    private FragmentDrawer mDrawerFragment;
    private GridviewAdapter mAdapter;
    private ArrayList<String> listMenuItem;
    private ArrayList<Integer> listIcon;

    private GridView gridView;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

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



        mAuth = FirebaseAuth.getInstance();
        setupDrawer();

        prepareList();

        // prepared arraylist and passed it to the Adapter class
        mAdapter = new GridviewAdapter(this,listMenuItem, listIcon);

        // Set custom adapter to gridview
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);
        /*
        stopPlayer= (Button) findViewById(R.id.bStopAll);

        stopPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        */

        // Implement On Item click listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Toast.makeText(StartActivity.this, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                if(position==0)
                {
                    Intent intent=new Intent(StartActivity.this,PlayerActivity.class);
                    intent.putExtra("Stream","http://87.117.217.103:38164");
                    intent.putExtra("Player","Channel 1");
                    //finish();
                    startActivity(intent);
                }
                else if(position==1)
                {
                    Intent intent=new Intent(StartActivity.this,PlayerActivity.class);
                    intent.putExtra("Stream","http://87.117.217.103:38164");
                    intent.putExtra("Player","Channel 2");
                    //finish();
                    startActivity(intent);
                }
                else if(position==2)
                {
                    Intent intent=new Intent(StartActivity.this,WebLoad.class);
                    intent.putExtra("Url","https://soundcloud.com/buet-radio");
                    //finish();
                    startActivity(intent);
                }
                else if(position==3)
                {
                    Intent intent=new Intent(StartActivity.this,WebLoad.class);
                    intent.putExtra("Url","http://buetradio.com/archive.html");
                    //finish();
                    startActivity(intent);
                }
                else if (position==4)
                {
                    if(mAuth.getCurrentUser()!=null)
                    {
                        Intent intent=new Intent(StartActivity.this,ChatActivity.class);
                        //finish();
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(StartActivity.this,SignInActivity.class);
                        intent.putExtra("From","Chatroom");
                        //finish();
                        startActivity(intent);
                    }
                }
                else if (position==5)
                {
                    Intent intent=new Intent(StartActivity.this,NotificationActivity.class);
                    intent.putExtra("From","Events");
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
                else if (position==8)
                {
                    Intent intent=new Intent(StartActivity.this,NotificationActivity.class);
                    intent.putExtra("From","Info");
                    //finish();
                    startActivity(intent);
                }
                else if (position==9)
                {
                    final CharSequence[] items = { "Call Us", "Email Us",
                            "Cancel" };

                    AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                    builder.setTitle("Contact Us!");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Call Us")) {
                                //System.out.println("Call us clicked");
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:01834855306"));
                                try {
                                    startActivity(callIntent);
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(StartActivity.this,"Call Permission Denied!",Toast.LENGTH_SHORT).show();
                                }
                            } else if (items[item].equals("Email Us")) {
                                //System.out.println("Email us clicked");
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"1405079.ad@ugrad.cse.buet.ac.bd"});
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
                }

            }
        });


        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,
                PhoneStateListener.LISTEN_CALL_STATE);

        //animate the Toolbar when it comes into the picture
        //AnimationUtils.animateToolbarDroppingDown(mContainerToolbar);
/*


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "radio.buetian.org.buetradio", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        play = (Button) findViewById(R.id.bPlay);
        stop = (Button) findViewById(R.id.bStop);
        record = (Button) findViewById(R.id.bRecord);
        signin = (Button) findViewById(R.id.bSignin);

        mPlayer = new MediaPlayer();
        //outputSource=new File("BuetRadio.mp3");
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        record.setOnClickListener(this);
        signin.setOnClickListener(this);


*/

    }


    public void prepareList() {
        listMenuItem = new ArrayList<String>();

        listMenuItem.add("Channel 1");
        listMenuItem.add("Channel 2");
        listMenuItem.add("Radio Hits");
        listMenuItem.add("Archive");
        listMenuItem.add("Chat Room");
        listMenuItem.add("Events");
        listMenuItem.add("Login");
        listMenuItem.add("Request");
        listMenuItem.add("Info");
        listMenuItem.add("Contact Us");

        listIcon = new ArrayList<Integer>();
        listIcon.add(R.drawable.channel1);
        listIcon.add(R.drawable.channel2);
        listIcon.add(R.drawable.hits);
        listIcon.add(R.drawable.archive);
        listIcon.add(R.drawable.call);
        listIcon.add(R.drawable.play);
        listIcon.add(R.drawable.jhhjh);
        listIcon.add(R.drawable.jhhjh);
        listIcon.add(R.drawable.info);
        listIcon.add(R.drawable.jhhjh);
    }


    public void onDrawerSlide(float slideOffset) {

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


    public void onDrawerItemClicked(int index) {
        if (index == 0) {

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
        }else if(index==1)
        {
            Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
            intent.putExtra("Stream","http://87.117.217.103:38164");
            intent.putExtra("Player","Channel 1");
            //finish();
            startActivity(intent);
        }
        else if(index==2)
        {
            Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
            intent.putExtra("Stream","http://87.117.217.103:38164");
            intent.putExtra("Player","Channel 2");
            //finish();
            startActivity(intent);
        }
        else if(index==3)
        {
            Intent intent=new Intent(getApplicationContext(),WebLoad.class);
            intent.putExtra("Url","https://soundcloud.com/buet-radio");
            //finish();
            startActivity(intent);
        }
        else if(index==4)
        {
            Intent intent=new Intent(getApplicationContext(),WebLoad.class);
            intent.putExtra("Url","http://buetradio.com/archive.html");
            //finish();
            startActivity(intent);
        }
        else if (index==5)
        {
            if(mAuth.getCurrentUser()!=null)
            {
                Intent intent=new Intent(StartActivity.this,ChatActivity.class);
                //finish();
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(StartActivity.this,SignInActivity.class);
                intent.putExtra("From","Chatroom");
                //finish();
                startActivity(intent);
            }
        }
        else if (index==6)
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
        else if (index==7)
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


}
