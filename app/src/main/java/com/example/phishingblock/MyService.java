package com.example.phishingblock;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    public static NotificationManager mNotificationManager;
    public static NotificationChannel channel;
    private static int notifyId = 2;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent testIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(this, 0, testIntent, FLAG_IMMUTABLE);


        // if message length is too long messages are divided


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("channel", "play!!",
                    NotificationManager.IMPORTANCE_DEFAULT);


            mNotificationManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
            mNotificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder notification
                    = new NotificationCompat.Builder(getApplicationContext(), "channel")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Phishing Block")
                    .setContentIntent(pendingIntent)
                    .setContentText("실행중");

            mNotificationManager.notify(1, notification.build());
            startForeground(1, notification.build());
        }

        return START_REDELIVER_INTENT;
    }

    public static int getNotifyId() {
        return notifyId++;
    }


    }



