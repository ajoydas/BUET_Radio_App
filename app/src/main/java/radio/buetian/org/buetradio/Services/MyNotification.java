package radio.buetian.org.buetradio.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import radio.buetian.org.buetradio.Activity.HelperActivity;
import radio.buetian.org.buetradio.R;

/**
 * Created by ajoy on 9/10/16.
 */
@SuppressLint("ParcelCreator")
public class MyNotification extends Notification {

    private Context ctx;
    private NotificationManager mNotificationManager;

    @SuppressLint("NewApi")
    public MyNotification(Context ctx){
        super();
        this.ctx=ctx;
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) ctx.getSystemService(ns);
        CharSequence tickerText = "BUET Radio Shortcut";
        long when = System.currentTimeMillis();
        Notification.Builder builder = new Notification.Builder(ctx);
        @SuppressWarnings("deprecation")
        Notification notification=builder.getNotification();
        notification.when=when;
        notification.tickerText=tickerText;
        notification.icon= R.drawable.icon;

        RemoteViews contentView=new RemoteViews(ctx.getPackageName(), R.layout.messageview);

        //set the button listeners
        setListeners(contentView);

        notification.contentView = contentView;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        CharSequence contentTitle = "From Shortcuts";
        mNotificationManager.notify(548853, notification);
    }

    public void setListeners(RemoteViews view){
       /* //radio listener
        Intent radio=new Intent(ctx,HelperActivity.class);
        radio.putExtra("DO", "radio");
        PendingIntent pRadio = PendingIntent.getActivity(ctx, 0, radio, 0);
        view.setOnClickPendingIntent(R.id., pRadio);

        //volume listener
        Intent volume=new Intent(ctx, HelperActivity.class);
        volume.putExtra("DO", "volume");
        PendingIntent pVolume = PendingIntent.getActivity(ctx, 1, volume, 0);
        view.setOnClickPendingIntent(R.id.volume, pVolume);

        //reboot listener
        Intent reboot=new Intent(ctx, HelperActivity.class);
        reboot.putExtra("DO", "reboot");
        PendingIntent pReboot = PendingIntent.getActivity(ctx, 5, reboot, 0);
        view.setOnClickPendingIntent(R.id.reboot, pReboot)
        ;

        //top listener
        Intent top=new Intent(ctx, HelperActivity.class);
        top.putExtra("DO", "top");
        PendingIntent pTop = PendingIntent.getActivity(ctx, 3, top, 0);
        view.setOnClickPendingIntent(R.id.top, pTop);
        */

        //app listener
        Intent app=new Intent(ctx, HelperActivity.class);
        app.putExtra("DO", "app");
        PendingIntent pApp = PendingIntent.getActivity(ctx, 4, app, 0);
        view.setOnClickPendingIntent(R.id.btn1, pApp);
    }

}
