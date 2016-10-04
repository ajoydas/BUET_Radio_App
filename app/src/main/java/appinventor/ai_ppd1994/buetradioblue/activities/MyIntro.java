package appinventor.ai_ppd1994.buetradioblue.activities;

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

        addSlide(AppIntroFragment.newInstance("Player", "With recording, calling & sending sms feature", R.drawable.introplayer, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("Sign In", "With Google or Facebook & Connect to the community", R.drawable.introsignin, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("Chatroom", "Chat with BUET Radio Community and share your feelings", R.drawable.introchatroom, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("Rewind", "Rewind the archive from SoundCloud & Buetradio.com", R.drawable.introhits, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("Events", "Get the events details from the app", R.drawable.introevents, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("eSMS", "Send SMS even if you are out of balance", R.drawable.introesms, R.color.colorAccent));

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