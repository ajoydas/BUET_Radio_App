package appinventor.ai_ppd1994.buetradioblue.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import appinventor.ai_ppd1994.buetradioblue.activities.NotificationActivity;
import appinventor.ai_ppd1994.buetradioblue.application.BUETRadio;
import appinventor.ai_ppd1994.buetradioblue.R;

/**
 * Created by ajoy on 9/4/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Intent intent = new Intent(this,NotificationActivity.class);
        if(remoteMessage.getData().size()>0)
        {
            String message = remoteMessage.getData().get("Message");
            Bundle bundle = new Bundle();
            bundle.putString("From","Notification");
            bundle.putString("Message",message);
            intent.putExtras(bundle);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        /*NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("BUET Radio Notifications!");
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.icon);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
*/

       /* Context ctx;
        ctx= BUETRadio.getAppContext();
        RemoteViews contentView=new RemoteViews(ctx.getPackageName(), R.layout.notification_bar);
        contentView.setTextViewText(R.id.message,remoteMessage.getNotification().getBody());
*/
        long when = System.currentTimeMillis();
        android.support.v4.app.NotificationCompat.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder = new android.support.v7.app.NotificationCompat.Builder(BUETRadio.getAppContext())
                    .setSmallIcon(R.drawable.black)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("Buet Radio Notification")
                    .setWhen(when)
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setOngoing(false);
        }
        else {
            notificationBuilder = new android.support.v7.app.NotificationCompat.Builder(BUETRadio.getAppContext())
                    .setSmallIcon(R.mipmap.icon)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("Buet Radio Notification")
                    .setWhen(when)
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setOngoing(false);
        }
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, notificationBuilder.build());

    }
}
