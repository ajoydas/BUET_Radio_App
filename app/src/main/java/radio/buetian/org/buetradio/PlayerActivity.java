package radio.buetian.org.buetradio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton play,stop,record,call;
    //Button directory;
    Button sendsms;
    boolean isPlaying= false;
    boolean isRecording= false;
    TextView tplaying,trecording,tcall;
    private Toolbar mToolbar;
    EditText textSMS;

    String channel;
    String stream_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        stream_url=getIntent().getExtras().getString("Stream");
        channel=getIntent().getExtras().getString("Player");
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        play= (ImageButton) findViewById(R.id.bPlay);
        stop= (ImageButton) findViewById(R.id.bStop);
        record= (ImageButton) findViewById(R.id.bRecord);
        call= (ImageButton) findViewById(R.id.bCall);
        //directory= (Button) findViewById(R.id.bDir);
        sendsms= (Button) findViewById(R.id.bSms);
        textSMS= (EditText) findViewById(R.id.eSms);
        tplaying= (TextView) findViewById(R.id.tplaying);
        trecording= (TextView) findViewById(R.id.tRecording);
        tcall= (TextView) findViewById(R.id.tCall);
        tplaying.setVisibility(View.INVISIBLE);
        trecording.setVisibility(View.INVISIBLE);

        tplaying.setText("......Playing "+channel+"......");
        trecording.setText("......Recording "+channel+"......");
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        record.setOnClickListener(this);
        call.setOnClickListener(this);
        tcall.setOnClickListener(this);
        //directory.setOnClickListener(this);
        sendsms.setOnClickListener(this);

        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onClick(View view) {
        if(view==play)
        {
            if(!isPlaying) {
                play.setImageResource(R.drawable.play);
                isPlaying=true;
                tplaying.setVisibility(View.VISIBLE);
                tplaying.startAnimation((Animation) AnimationUtils.loadAnimation(PlayerActivity.this,R.anim.translate));
            }
            else if(isPlaying)
            {
                play.setImageResource(R.drawable.pause);
                isPlaying=false;
                tplaying.clearAnimation();
                tplaying.setVisibility(View.INVISIBLE);
            }
        }
        if(view==record)
        {
            if(!isRecording) {
                //play.setImageResource(R.drawable.play);
                isRecording=true;
                trecording.setVisibility(View.VISIBLE);
                trecording.startAnimation((Animation) AnimationUtils.loadAnimation(PlayerActivity.this,R.anim.translate));
            }
            else if(isRecording)
            {
                //play.setImageResource(R.drawable.pause);
                isRecording=false;
                trecording.clearAnimation();
                trecording.setVisibility(View.INVISIBLE);
            }
        }
        if(view==stop)
        {

        }
        if (view==tcall||view==call)
        {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:01834855306"));
            try {
                startActivity(callIntent);
            }
            catch (Exception e)
            {
                Toast.makeText(this,"Call Permission Denied!",Toast.LENGTH_SHORT).show();
            }

        }
        if(view==sendsms)
        {
            String phoneNo = "01521487525";
            String sms = textSMS.getText().toString();
            if(sms.equals(""))
            {
                Toast.makeText(getApplicationContext(), "SMS is empty! Please write your message.",
                        Toast.LENGTH_LONG).show();
            }
            else {

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "SMS Sent!",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
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

}
