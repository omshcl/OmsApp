package com.hcl.InstantPickup.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.activities.Login;

/** Makes use of Firebase service
 * to handle notifications for the app
 * @author HCL Intern Team
 * @version 1.0.0
 */
//FirebaseMessagingService used to receive push notifications
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        //Display API token so we can send cloud messages to the app
        Log.e("Firebase Token",token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("MFMS","onMessageReceived");
        //create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "Firebase Push Notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("FIREBASE_ID", "FIREBASE_NAME", importance);
            channel.setDescription(description);
            //channel.setSound(null, null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        //create and display notification
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "FIREBASE_ID");
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.ic_notification);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
