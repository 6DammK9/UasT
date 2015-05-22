package hk.ust.comp4521.UasT;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Darren on 21/4/2015.
 */
public class PlayReceiver extends BroadcastReceiver {
    static final String TAG = "UasT.PlayReceiver";
    public static final int NOTIFICATION_ID = 2;
    static final long[] vibPattern = { 0, 500, 250, 500 };
    String title, body;
    MainActivity main;
    CalendarFragment cal;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bData = intent.getExtras();
        Log.i(TAG, "AlarmReceived");
        if (bData.get("msg").equals("hk.ust.comp4521.UasT.reminder_alarm")) {
            // WORKS HERE
            title = "Reminder: " + bData.getString("remTitle");
            body = "At: " + (bData.getString("remTime"));

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

            mBuilder.setSmallIcon(R.drawable.ic_stat_gcm);
            mBuilder.setAutoCancel(true);
            mBuilder.setVibrate(vibPattern);
            mBuilder.setLights(0xFFFFFF00, 1000, 1000);

            mBuilder.setContentTitle(title);
            mBuilder.setContentText(body);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);

            Intent startActivity = new Intent(context, CalendarFragment.class);

            PendingIntent pi = PendingIntent.getActivity(context, 0,
                    startActivity, 0);

            mBuilder.setContentIntent(pi);

            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }

        if(main != null)
            main.gotoFragment(cal);
    }


    public void setCallback(MainActivity main) {
        this.main = main;
    }
}
