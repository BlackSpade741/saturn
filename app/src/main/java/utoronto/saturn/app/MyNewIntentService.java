package utoronto.saturn.app;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;


import utoronto.saturn.app.front_end.views.BaseView;

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public MyNewIntentService() {
        super("MyNewIntentService");
    }


    /**
     * Set up the notification content and push the notification to the app.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Set up the alarm sound and notifyIntent.
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notifyIntent = new Intent(this, BaseView.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notification_manager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder;

        // If SDK version is bigger than API 26, then set up a notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Set up the basic information of the channel.
            String chanel_id = "3000";
            CharSequence name = "Channel Name";
            String description = "Chanel Description";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notification_manager.createNotificationChannel(mChannel);
            builder = new Notification.Builder(this, chanel_id);
        } else {
            builder  = new Notification.Builder(this);
        }

        // Prepare for the content to be sent.
        String content = "You have " + NotificationScheduler.getContent() + " tomorrow.";
        builder.setSmallIcon(R.drawable.ic_reminder)
                .setContentTitle("Event Reminder")
                .setContentText(content)
                .setSound(alarmSound)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        // Send the notifications.
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }
}