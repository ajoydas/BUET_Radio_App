package appinventor.ai_ppd1994.buetradioblue.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;

import appinventor.ai_ppd1994.buetradioblue.objects.PlayerConnection;

/**
 * Created by ajoy on 9/10/16.
 */
public class HelperActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
            if(PlayerConnection.getMediaPlayer().isPlaying()) {
                System.out.println("Stop clicked");
                PlayerConnection.getMediaPlayer().stop();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancelAll();

                if(PlayerConnection.getChannel().equals("Channel 1"))
                {
                    Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
                    intent.putExtra("Player","Channel 1");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
                    intent.putExtra("Player","Channel 2");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                }
            }
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
