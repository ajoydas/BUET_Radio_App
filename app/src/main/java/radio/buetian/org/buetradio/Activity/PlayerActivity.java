package radio.buetian.org.buetradio.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import radio.buetian.org.buetradio.Application.BUETRadio;
import radio.buetian.org.buetradio.BuildConfig;
import radio.buetian.org.buetradio.Fragment.FragmentDrawer;
import radio.buetian.org.buetradio.Fragment.FragmentDrawerPlayer;
import radio.buetian.org.buetradio.Objects.PlayerConnection;
import radio.buetian.org.buetradio.R;
import radio.buetian.org.buetradio.Services.MyNotification;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton play, stop, record, call;
    //Button directory;
    Button sendsms, chat, request;
    boolean isPlaying = false;
    boolean isRecording = false;
    TextView tplaying, trecording, tcall;
    private Toolbar mToolbar;
    EditText textSMS;

    String channel;
    String stream_url;
    private String CallNumber;
    private String SmsNumber;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private ViewGroup mContainerToolbar;
    private FragmentDrawerPlayer mDrawerFragment;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mAuth = FirebaseAuth.getInstance();
        stream_url = getIntent().getExtras().getString("Stream");
        channel = getIntent().getExtras().getString("Player");
        if (!channel.equals(PlayerConnection.getChannel())) {
            PlayerConnection.getMediaPlayer().stop();
            PlayerConnection.setIsRecording(false);
            PlayerConnection.setChannel(channel);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        }

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        setupDrawer();

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
       /* View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }*/
        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(PlayerActivity.this, "Fetch Successfull", Toast.LENGTH_SHORT).show();
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            //Toast.makeText(PlayerActivity.this, "Fetch Failed", Toast.LENGTH_SHORT).show();
                        }
                        
                        CallNumber = mFirebaseRemoteConfig.getString("CallNumber");
                        SmsNumber = mFirebaseRemoteConfig.getString("SmsNumber");

                    }
                });

        PlayerConnection.setUi(true);
        play = (ImageButton) findViewById(R.id.bPlay);
        stop = (ImageButton) findViewById(R.id.bStop);
        record = (ImageButton) findViewById(R.id.bRecord);
        call = (ImageButton) findViewById(R.id.bCall);
        //directory= (Button) findViewById(R.id.bDir);
        sendsms = (Button) findViewById(R.id.bSms);
        chat = (Button) findViewById(R.id.bChat);
        request = (Button) findViewById(R.id.bRequest);

        textSMS = (EditText) findViewById(R.id.eSms);
        tplaying = (TextView) findViewById(R.id.tplaying);
        trecording = (TextView) findViewById(R.id.tRecording);
        //tcall= (TextView) findViewById(R.id.tCall);
        tplaying.setText("......Playing " + channel + "......");
        trecording.setText("......Recording " + channel + "......");

        if (!PlayerConnection.getMediaPlayer().isPlaying()) {
            play.setImageResource(R.drawable.pause);
            tplaying.setVisibility(View.INVISIBLE);
            //trecording.setVisibility(View.INVISIBLE);
        } else {
            play.setImageResource(R.drawable.play);
            tplaying.setVisibility(View.VISIBLE);
            tplaying.startAnimation((Animation) AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.translate));
        }
        if (!PlayerConnection.isRecording()) {
            record.setImageResource(R.drawable.record);
            trecording.setVisibility(View.INVISIBLE);
            //trecording.setVisibility(View.INVISIBLE);
        } else {
            record.setImageResource(R.drawable.stoprecord);
            trecording.setVisibility(View.VISIBLE);
            trecording.startAnimation((Animation) AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.translate));
        }

        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        record.setOnClickListener(this);
        call.setOnClickListener(this);
        //tcall.setOnClickListener(this);
        //directory.setOnClickListener(this);
        sendsms.setOnClickListener(this);
        chat.setOnClickListener(this);
        request.setOnClickListener(this);

        PlayerConnection.getMediaPlayer().setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                System.out.println("loading error.....");
                play.setImageResource(R.drawable.pause);
                tplaying.setVisibility(View.INVISIBLE);
                PlayerConnection.getMediaPlayer().stop();
                return true;
            }
        });

        textSMS.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void showNotification() {
        new MyNotification(this);
        //finish();
    }


    private void setupDrawer() {
//        mToolbar = (Toolbar) findViewById(R.id.app_bar);
//        mContainerToolbar = (ViewGroup) findViewById(R.id.container_app_bar);
        //set the Toolbar as ActionBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setup the NavigationDrawer
        mDrawerFragment = (FragmentDrawerPlayer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
    }


    @Override
    public void onClick(View view) {
        if (view == play) {
            if (!PlayerConnection.getMediaPlayer().isPlaying()) {
                try {
                    progressDialog = ProgressDialog.show(PlayerActivity.this, "Connecting for playing......", "Please wait...", true, true);
                    PlayerConnection.getMediaPlayer().reset();
                    PlayerConnection.getMediaPlayer().setDataSource(stream_url);
                    PlayerConnection.getMediaPlayer().prepareAsync();
                    PlayerConnection.getMediaPlayer().setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            play.setImageResource(R.drawable.pause);
                            tplaying.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Broadcast not available.Please check events & connection", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });

                    PlayerConnection.getMediaPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            PlayerConnection.getMediaPlayer().start();

                            //showNotification();
                            Context ctx;
                            ctx = PlayerActivity.this;
                            RemoteViews contentView = new RemoteViews(ctx.getPackageName(), R.layout.messageview);
                            contentView.setTextViewText(R.id.tChannel, "Playing " + PlayerConnection.getChannel());
                            Intent notificationIntent = new Intent(PlayerActivity.this, HelperActivity.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(PlayerActivity.this, 4, notificationIntent, 0);
                            contentView.setOnClickPendingIntent(R.id.btn1, contentIntent);

                            long when = System.currentTimeMillis();
                            android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(PlayerActivity.this)
                                    .setSmallIcon(R.drawable.icon)
                                    .setContentTitle("Buet Radio Online Stream")
                                    .setWhen(when)
                                    .setCustomBigContentView(contentView)
                                    .setOngoing(true);

                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            mNotificationManager.notify(1, notificationBuilder.build());


                            progressDialog.dismiss();
                            play.setImageResource(R.drawable.play);
                            tplaying.setVisibility(View.VISIBLE);
                            tplaying.startAnimation((Animation) AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.translate));
                        }
                    });

                } catch (IOException e) {
                    Toast.makeText(this, "Couldn't Connect", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else {

                PlayerConnection.getMediaPlayer().stop();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                play.setImageResource(R.drawable.pause);
                tplaying.clearAnimation();
                tplaying.setVisibility(View.INVISIBLE);
            }
        }
        if (view == record) {
            if (!PlayerConnection.isRecording()) {
                progressDialog = ProgressDialog.show(this, "Connecting for Recording......", "Please wait...", true, true);
                new Recording().execute();
                //play.setImageResource(R.drawable.play);
                //trecording.setVisibility(View.VISIBLE);
                //trecording.startAnimation((Animation) AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.translate));
            } else {
                //play.setImageResource(R.drawable.pause);
                PlayerConnection.setIsRecording(false);
                record.setImageResource(R.drawable.record);
                trecording.clearAnimation();
                trecording.setVisibility(View.INVISIBLE);
            }
        }
        if (view == stop) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

            PlayerConnection.getMediaPlayer().stop();
            PlayerConnection.setIsRecording(false);
            play.setImageResource(R.drawable.pause);
            record.setImageResource(R.drawable.record);
            tplaying.clearAnimation();
            tplaying.setVisibility(View.INVISIBLE);
            trecording.clearAnimation();
            trecording.setVisibility(View.INVISIBLE);
        }
        if (view == call) {

            if (PlayerConnection.isRecording()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
                builder.setTitle("Call Us.");
                builder.setMessage("Your recording will be aborted.You can download the show from the archive.Proceed?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(CallNumber));
                        try {
                            startActivity(callIntent);
                        } catch (Exception e) {
                            Toast.makeText(PlayerActivity.this, "Call Permission Denied!", Toast.LENGTH_LONG).show();
                        }
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(CallNumber));
                try {
                    startActivity(callIntent);
                } catch (Exception e) {
                    Toast.makeText(PlayerActivity.this, "Call Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (view == sendsms) {
            String phoneNo = SmsNumber;
            String sms = textSMS.getText().toString();
            if (sms.equals("")) {
                Toast.makeText(getApplicationContext(), "SMS is empty! Please write your message.",
                        Toast.LENGTH_LONG).show();
            } else {

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "SMS Sent!",
                            Toast.LENGTH_LONG).show();

                    textSMS.setText("");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

        }

        if (view == chat) {
            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(PlayerActivity.this, ChatActivity.class);
                //finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(PlayerActivity.this, SignInActivity.class);
                intent.putExtra("From", "Chatroom");
                //finish();
                startActivity(intent);
            }
        }
        if (view == request) {
            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(PlayerActivity.this, RequestActivity.class);
                //finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(PlayerActivity.this, SignInActivity.class);
                intent.putExtra("From", "Request");
                //finish();
                startActivity(intent);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            progressDialog.dismiss();
        }
        catch (Exception e)
        {
            System.out.println("Can't end progress dialog in player");
        }
    }

    /*
        if(view==directory)
        {
            *//*Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/BuetRadioRecord/");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(selectedUri, "resource/folder");

            if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
            {
                startActivity(intent);
            }
            else
            {
                // if you reach this place, it means there is no any file
                // explorer app installed on your device

            }*//*

            if(createDirIfNotExists("BuetRadioRecords"))
            {
                *//*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                        + "/BuetRadioRecord/");
                intent.setDataAndType(uri, "text/csv");
                startActivity(Intent.createChooser(intent, "Open folder"));*//*
                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/BuetRadioRecord/");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(selectedUri, "text/csv");

                if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
                {
                    startActivity(Intent.createChooser(intent, "Open folder"));
                }
                else{
                    Toast.makeText(this,"The folder can't be loaded.",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this,"The directory can't be created.",Toast.LENGTH_SHORT).show();
            }

        }*/

    void update() {
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        record.setImageResource(R.drawable.stoprecord);
        trecording.setVisibility(View.VISIBLE);
        trecording.startAnimation((Animation) AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.translate));

    }

    private class Recording extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {


            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(stream_url)
                        .build();

                Response response = client.newCall(request).execute();
                PlayerConnection.setInputStream(response.body().byteStream());
                PlayerConnection.setIsRecording(true);

                boolean firstTime = true;

                int c;
                while ((c = PlayerConnection.getInputStream().read()) != -1 && PlayerConnection.isRecording()) {

                    if (firstTime) {
                        firstTime = false;
                        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

                        String fileName = File.separator + "radio_" + "recording_" + System.currentTimeMillis();
                        String outputSource;
                        if (createDirIfNotExists("BuetRadioRecords")) {
                            if (isSDPresent) {
                                outputSource = Environment.getExternalStorageDirectory() + "/BuetRadioRecords" + fileName;

                            } else {
                                outputSource = Environment.getDataDirectory() + "/BuetRadioRecords" + fileName;
                            }
                            PlayerConnection.setFileOutputStream(new FileOutputStream(outputSource + ".mp3"));

                            if (PlayerConnection.isUi()) {
                                publishProgress();
                            }
                        } else {
                            System.out.println("Closed Recording......2.");
                            break;
                        }
                    }
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    PlayerConnection.getFileOutputStream().write(c);
                }
                /*int bytesRead = 0;
                int bytes;
                while (((bytes = inputStream.read()) != -1) && isRecording) {

                    fileOutputStream.write(bytes);
                    bytesRead++;

                    stopTime = System.currentTimeMillis();

                    long seconds = (Math.abs(startTime-stopTime));
                    int minutes = 1000 * 60 * 60;

                    if(minutes<=seconds)
                    {
                        Log.d("xxx", "recording task exceed stopped");
                        break;
                    }
                }*/
                System.out.println("Closed Recording.......");
                PlayerConnection.getInputStream().close();
                try {
                    PlayerConnection.getFileOutputStream().close();
                }
                catch (Exception e)
                {
                    System.out.println("Output stream error on close");
                }

                return null;

            } catch (IOException e) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                System.out.println("Closed Recording.......3");
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Recording can't be started.Please check your connection, storage space & Try again.",Toast.LENGTH_LONG).show();
            }

            PlayerConnection.setIsRecording(false);
            return null;

            /*URL url = null;
            try {
                url = new URL(stream_url);
                inputStream = url.openStream();
                fileOutputStream = new FileOutputStream(outputSource);
                int c;

                while ((c = inputStream.read()) != -1) {
                    fileOutputStream.write(c);
                }
                fileOutputStream.close();
            } catch (IOException e) {
                System.out.println("Can't Record.");
                e.printStackTrace();
            }*/
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            update();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            trecording.clearAnimation();
            trecording.setVisibility(View.INVISIBLE);
        }
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
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
    public void onBackPressed() {
        super.onBackPressed();
        PlayerConnection.setUi(false);
        try {
            progressDialog.dismiss();
        }
        catch (Exception e) {
            System.out.println("Can't end progress dialog in player");
        }
            finish();
    }


    public void onDrawerSlide(float slideOffset) {

    }

    public void onDrawerItemClicked(int index) {
        if (index == 0) {

            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.putExtra("From", "Signin");
                finish();
                startActivity(intent);
            }
        } else if (index == 1) {
            Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
            intent.putExtra("Stream", "http://87.117.217.103:38164");
            intent.putExtra("Player", "Channel 1");
            finish();
            startActivity(intent);
        } else if (index == 2) {
            Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
            intent.putExtra("Stream", "http://87.117.217.103:38164");
            intent.putExtra("Player", "Channel 2");
            finish();
            startActivity(intent);
        } else if (index == 3) {
            Intent intent = new Intent(getApplicationContext(), WebLoad.class);
            intent.putExtra("Url", "https://soundcloud.com/buet-radio");
            finish();
            startActivity(intent);
        } else if (index == 4) {
            Intent intent = new Intent(getApplicationContext(), WebLoad.class);
            intent.putExtra("Url", "http://buetradio.com/archive.html");
            finish();
            startActivity(intent);
        } else if (index == 5) {
            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                //finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.putExtra("From", "Chatroom");
                //finish();
                startActivity(intent);
            }
        } else if (index == 6) {
            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(getApplicationContext(), RequestActivity.class);
                //finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.putExtra("From", "Request");
                //finish();
                startActivity(intent);
            }
        } else if (index == 7) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "appinventor.ai_ppd1994.buetradioblue")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "appinventor.ai_ppd1994.buetradioblue")));
            }
        }
    }


}
