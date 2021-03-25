package com.example.firebasenotification;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String title,message;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");

        Intent activityIntent= new Intent(this,MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(),0,activityIntent,0);



        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){

            NotificationChannel channel= new NotificationChannel("My Notification","My Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(),"My Notification");
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setContentIntent(pendingIntent);
        builder.setColor(getResources().getColor(R.color.purple_200));
        builder.setSmallIcon(R.drawable.ic_launcher_background);


        // now notify the user.....
        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(1,builder.build());

    }
}
