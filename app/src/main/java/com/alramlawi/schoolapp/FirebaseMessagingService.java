package com.alramlawi.schoolapp;

import android.graphics.BitmapFactory;

import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        shownotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

    }
    public void shownotification(String title, String message)
    {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"MyNotification")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.school)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.school))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(""))
                .setContentText(message);


      ///  Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://.com"));
      /// PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,0);

       // builder.addAction(R.drawable.school,"Click To Visit Our Website.",pendingIntent);


        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(this);
        managerCompat.notify(999,builder.build());
    }
}
