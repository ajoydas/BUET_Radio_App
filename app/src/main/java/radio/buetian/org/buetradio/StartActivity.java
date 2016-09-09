package radio.buetian.org.buetradio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setupDrawer();

        prepareList();

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
                Toast.makeText(StartActivity.this, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                if(position==0)
                {
                    Intent intent=new Intent(StartActivity.this,PlayerActivity.class);
                    intent.putExtra("Stream","http://87.117.217.103:38164");
                    finish();
                    startActivity(intent);
                }
                else if(position==1)
                {
                    Intent intent=new Intent(StartActivity.this,PlayerActivity.class);
                    intent.putExtra("Stream","http://87.117.217.103:38164");
                    finish();
                    startActivity(intent);
                }

            }
        });

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

        try {
            if (getIntent().getExtras().getString("Message") != null) {
                Intent intent = new Intent(this, NotificationActivity.class);
                intent.putExtra("Message", getIntent().getExtras().getString("Message"));
                finish();
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (index == 1) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        } else {
            //mPager.setCurrentItem(index);
            finish();
            startActivity(new Intent(this, StartActivity.class));
        }
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
                inputStream = response.body().byteStream();
                isRecording = true;

                startTime = System.currentTimeMillis();

                Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

                String fileName = File.separator + "radio_" + "recording_" + System.currentTimeMillis();

                if (isSDPresent) {
                    outputSource = Environment.getExternalStorageDirectory() + fileName;

                } else {
                    outputSource = Environment.getDataDirectory() + fileName;
                }

                String contentType = "audio/mpeg";

                if (contentType.equals("audio/aacp"))
                    fileOutputStream = new FileOutputStream(outputSource + ".acc");
                else if (contentType.equals("audio/mpeg"))
                    fileOutputStream = new FileOutputStream(outputSource + ".mp3");
                else
                    fileOutputStream = new FileOutputStream(outputSource + ".nieznany_format");
                int c;

                while ((c = inputStream.read()) != -1 && isRecording) {

                    fileOutputStream.write(c);
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
                inputStream.close();
                fileOutputStream.close();
                return null;

            } catch (IOException e) {
                e.printStackTrace();
                isRecording = false;
            }

            isRecording = false;
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
    }

    @Override
    public void onClick(View view) {




        /*
        if (view == play) {
            try {
                mPlayer.reset();
                mPlayer.setDataSource(stream_url);
                mPlayer.prepareAsync();

                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mPlayer.start();
                    }
                });

            } catch (IOException e) {
                Toast.makeText(this, "Couldn't Connect", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
        if (view == stop) {
            mPlayer.stop();

            isRecording = false;

        }

        if (view == record) {
            new Recording().execute();
        }

        if (view == signin) {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
        }
*/




    }
}
