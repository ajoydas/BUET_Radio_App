package radio.buetian.org.buetradio.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import radio.buetian.org.buetradio.Objects.PlayerConnection;

/**
 * Created by ajoy on 9/10/16.
 */
public class HelperActivity extends Activity {

    private HelperActivity ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ctx = this;
            if(PlayerConnection.getMediaPlayer().isPlaying()) {
                System.out.println("Stop clicked");
                PlayerConnection.getMediaPlayer().stop();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                //Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                //startActivity(intent);

                if(PlayerConnection.getChannel().equals("Channel 1"))
                {
                    Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
                    intent.putExtra("Stream","http://87.117.217.103:38164");
                    intent.putExtra("Player","Channel 1");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(getApplicationContext(),PlayerActivity.class);
                    intent.putExtra("Stream","http://87.117.217.103:38164");
                    intent.putExtra("Player","Channel 2");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please select a channel to play on the app",Toast.LENGTH_SHORT).show();
            }
            finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
