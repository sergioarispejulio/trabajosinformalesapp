package com.example.sergioarispejulio.proyectogrado.clases;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.sergioarispejulio.proyectogrado.Loader;
import com.example.sergioarispejulio.proyectogrado.R;
import com.example.sergioarispejulio.proyectogrado.index_login;
import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sergio Arispe Julio on 7/18/2016.
 */
public class myGCMListener extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.i("TAG", "Notification received");

        String message = data.getString("message");
        String title = data.getString("title");
        String tikerText = data.getString("tickerText");

        String smallIconURL = data.getString("smallIcon");
        String LargeIconURL = data.getString("largeIcon");

        Bitmap smalliconbitmap = getBitmapFromURL("https://cdn4.iconfinder.com/data/icons/iconsimple-logotypes/512/github-512.png");

        //ShowSimpleNotifications(message,title,tikerText,smalliconbitmap);
        //Bitmap largeiconbitmap = getBitmapFromURL(LargeIconURL);
        showBigPictureStyleNotifications(message,title,tikerText,smalliconbitmap,smalliconbitmap);
    }


    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showBigPictureStyleNotifications(String message, String title, String tickertext, Bitmap smallIcon,
                                                 Bitmap largeIcon)
    {
        //Create notification object and set the content.
        NotificationCompat.Builder nb= new NotificationCompat.Builder(this);
        nb.setSmallIcon(R.drawable.common_ic_googleplayservices);

        nb.setLargeIcon(smallIcon);
        nb.setContentTitle(title);
        nb.setContentText(message);
        nb.setTicker(tickertext);
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.musicaprueba);
        nb.setSound(sound);


        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(largeIcon);
        s.setSummaryText(message);
        nb.setStyle(s);

        Intent resultIntent = new Intent(this, Loader.class);
        TaskStackBuilder TSB = TaskStackBuilder.create(this);
        TSB.addParentStack(index_login.class);
        // Adds the Intent that starts the Activity to the top of the stack
        TSB.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                TSB.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(11221, nb.build());
    }

    public void ShowSimpleNotifications(String message, String title, String tickertext, Bitmap smallIcon) {

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setSmallIcon(R.drawable.common_ic_googleplayservices);
        nb.setLargeIcon(smallIcon);
        nb.setContentTitle(title);
        nb.setContentText(message);
        nb.setTicker(tickertext);

        NotificationCompat.BigTextStyle bigTextnotifications = new NotificationCompat.BigTextStyle();
        bigTextnotifications.bigText(message);
        bigTextnotifications.setBigContentTitle(title);
        bigTextnotifications.setSummaryText("feelZdroid");
        nb.setStyle(bigTextnotifications);

        Intent resultIntent = new Intent(this, Loader.class);
        TaskStackBuilder TSB = TaskStackBuilder.create(this);
        TSB.addParentStack(index_login.class);
        // Adds the Intent that starts the Activity to the top of the stack
        TSB.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                TSB.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(11221, nb.build());

    }
}
