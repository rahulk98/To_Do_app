package com.example.rahulkrishnan;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        String taskName = intent.getStringExtra(Add_Task_Activity.TASK_NAME_LABEL);
        String taskDate = intent.getStringExtra(Add_Task_Activity.TASK_DATE_LABEL);
        String taskTime = intent.getStringExtra(Add_Task_Activity.TASK_TIME_LABEL);
        deliverNotification(context, taskName, taskDate, taskTime);
    }

    public void deliverNotification(Context context, String name, String date, String time) {
        Intent contentIntent = new Intent(context, Add_Task_Activity.class);
        contentIntent.putExtra(MainActivity.TASK_NAME_TAG, name);
        contentIntent.putExtra(MainActivity.TASK_DATE_TAG, date);
        contentIntent.putExtra(MainActivity.TASK_TIME_TAG, time);
        contentIntent.putExtra(MainActivity.UPDATE_TAG, true);
        int id = (int) System.currentTimeMillis();
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, id, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_done_all)
                .setContentTitle(name)
                .setContentText("Due on " + date)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void createNotificationChannel(Context context) {
        mNotificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            context.getString(R.string.notification_label),
                            NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
