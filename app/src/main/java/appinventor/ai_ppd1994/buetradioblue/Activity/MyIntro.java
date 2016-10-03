package appinventor.ai_ppd1994.buetradioblue.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import appinventor.ai_ppd1994.buetradioblue.R;

/**
 * Created by ajoy on 9/15/16.
 */
public class MyIntro extends AppIntro {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = ProgressDialog.show(MyIntro.this, "Welcome to BUET RADIO....", "Please wait..", true, true);
        //askForPermissions(new String[]{Manifest.permission.CAMERA}, 2);


        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
//        addSlide(first_fragment);
//        addSlide(second_fragment);
//        addSlide(third_fragment);
//        addSlide(fourth_fragment);
        //addSlide(SampleSlide.newInstance(R.layout.your_slide_here));

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Player", "With recording, calling & sending sms feature", R.drawable.player, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("Sign In", "With Google or Facebook & Connect to the community", R.drawable.signin, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("Chatroom", "Chat with BUET Radio Community and share your feelings", R.drawable.chatroom, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("Rewind", "Rewind the archive from SoundCloud & Buetradio.com", R.drawable.rewind, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("Events", "Get the events details from the app", R.drawable.events, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("Request", "Send your song and other request to the host", R.drawable.request, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("Notification", "Now you will be notified about the updates", R.drawable.notify, R.color.colorAccent));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));
        try{
            progressDialog.dismiss();
        }
        catch (Exception e)
        {
            System.out.println("My intro progress bar paused failed");
        }
        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        //setVibrate(true);
       // setVibrateIntensity(30);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            progressDialog.dismiss();
        }
        catch (Exception e)
        {
            System.out.println("My intro progress bar paused failed");
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}