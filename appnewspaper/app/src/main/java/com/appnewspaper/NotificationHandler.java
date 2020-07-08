package com.appnewspaper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class NotificationHandler extends ContextWrapper {
    private NotificationManager manager;

    public static final String CHANNEL_HIGH_ID ="1";
    private final String CHANNEL_HIGH_NAME = "HIGH CHANNEL";
    public static final String CHANNEL_LOW_ID ="2";
    private final String CHANNEL_LOW_NAME = "LOW CHANNEL";

    public static final int GROUP_ID = 1;
    private final String GROUP_NAME = "GROUP";

    public NotificationHandler(Context base) {
        super(base);
        //createChannels();
    }
    /*
    private void createChannels() {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
            // Create channel
            NotificationChannel highChannel = new NotificationChannel(CHANNEL_HIGH_ID, CHANNEL_HIGH_NAME, NotificationManager.IMPORTANCE_HIGH);
            // CONFIGURATION
            highChannel.setShowBadge(true);
            highChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationChannel lowChannel = new NotificationChannel(CHANNEL_LOW_ID, CHANNEL_LOW_NAME, NotificationManager.IMPORTANCE_LOW);
            lowChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(highChannel);
            getManager().createNotificationChannel(lowChannel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder createNotification (String title, String msg, boolean priority) {
        if (Build.VERSION.SDK_INT>= 26) {
            if (priority) {
                return createNotificationWithChannels (title, msg, CHANNEL_HIGH_ID);
            }
            return createNotificationWithChannels (title, msg, CHANNEL_LOW_ID);
        }
        return createNotificationWithoutChannels(title, msg);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification.Builder createNotificationWithChannels (String title, String msg, String channelID) {
        Intent intent = new Intent(this, basicActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("msg", msg);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Action action = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.ic_menu_send), "VIEW", pendingIntent).build();

        //Convert image to bitmap
        Bitmap my_image = BitmapFactory.decodeResource(getResources(), R.drawable.image);

        return new Notification.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                //.setActions(action)
                .setLargeIcon(my_image)
                //.setStyle(new Notification.BigPictureStyle().bigPicture(my_image).bigLargeIcon((Bitmap) null))
                .setStyle(new Notification.BigTextStyle().bigText(msg))
                .setGroup(GROUP_NAME)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void publishNotificationGroup (boolean priority) {
        String channelID = CHANNEL_LOW_ID;
        if (priority) {
            channelID = CHANNEL_HIGH_ID;
        }
        Notification groupNotification = new Notification.Builder(getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_menu_send)
                .setGroup(GROUP_NAME)
                .setGroupSummary(true)
                .build();
        getManager().notify(GROUP_ID, groupNotification);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification.Builder createNotificationWithoutChannels (String title, String msg) {
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setGroup(GROUP_NAME)
                .setContentText(msg).setAutoCancel(true);
    }

    public NotificationManager getManager() {
        if (manager==null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

     */
}
