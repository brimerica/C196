package com.example.C196.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.C196.MainActivity;
import com.example.C196.R;

import androidx.core.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "CHANNEL_ID";
    static int notificationId;


    //@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive ran with notificationId: " + notificationId);
        String message = intent.getStringExtra(MainActivity.EXTRA_NOTE_MESSAGE);
        String title = intent.getStringExtra(MainActivity.EXTRA_NOTE_TITLE);

        createNotificationChannel(context);
        Notification n = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++,n);
    }



    //@RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Main Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Test Channel");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
