package com.game.xogame;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.game.xogame.views.game.GameInfoActivity;
import com.game.xogame.views.game.ModerationActivity;
import com.game.xogame.views.game.PlayActivity;
import com.game.xogame.views.game.RatingGameActivity;
import com.game.xogame.views.game.WinActivity;
import com.game.xogame.views.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.Map;


public class GamePush extends FirebaseMessagingService {
    private Map<String, String> data;
    public static MainActivity activity;
    public static final String NOTIFICATION_CHANNEL_ID = "4655";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        data = remoteMessage.getData();

        if(data!=null && data.get("type")!=null && !sharedPref.getString("token","null").equals("null")){
            switch (data.get("type")) {
                case "win":
                    sendWin();
                    break;
                case "task":
                    sendTask();
                    break;
                case "endgame":
                    sendEndgame();
                    break;
                case "moderated":
                    sendModerated();
                    break;
                case "newgame":
                    sendNewGame();
                    break;
                case "gameinfo":
                    sendGameInfo();
                    break;
            }
        }
    }

    public void sendEndgame(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long notificatioId = System.currentTimeMillis();

        Intent intent = new Intent(getApplicationContext(), RatingGameActivity.class); // Here pass your activity where you want to redirect.
        intent.putExtra("gameid",data.get("gameid"));
        intent.putExtra("push","true");
        intent.putExtra("type","history");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(this.getResources().getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText("end"))
                .setContentText(getString(R.string.push_endgame))
                .setAutoCancel(true)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getPackageName()+"/"+R.raw.winpush))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent);
        mNotificationManager.notify((int) notificatioId, notificationBuilder.build());
    }

    public void sendModerated(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long notificatioId = System.currentTimeMillis();

        Intent intent = new Intent(getApplicationContext(), ModerationActivity.class); // Here pass your activity where you want to redirect.
        intent.putExtra("gameid",data.get("gameid"));
        intent.putExtra("title",data.get("title"));
        intent.putExtra("company",data.get("company"));
        intent.putExtra("logo",data.get("logo"));
        intent.putExtra("latePlace",data.get("latePlace"));
        intent.putExtra("newPlace",data.get("newPlace"));

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(this.getResources().getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText("end"))
                .setContentText(getString(R.string.push_moderate))
                .setAutoCancel(true)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getPackageName()+"/"+R.raw.winpush))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent);
        mNotificationManager.notify((int) notificatioId, notificationBuilder.build());
    }

    public void sendWin(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long notificatioId = System.currentTimeMillis();

        Intent intent = new Intent(getApplicationContext(), WinActivity.class); // Here pass your activity where you want to redirect.
        intent.putExtra("TITLE",data.get("title"));
        intent.putExtra("COMPANY",data.get("company"));
        intent.putExtra("LOGO",data.get("logo"));
        intent.putExtra("PHOTO",data.get("photo"));
        intent.putExtra("NAME",data.get("name"));
        intent.putExtra("NICKNAME",data.get("nickname"));
        intent.putExtra("NUMBERTASK",data.get("numberTask"));
        intent.putExtra("TASKS",data.get("tasks"));
        intent.putExtra("GAMEID",data.get("gameid"));
        intent.putExtra("MONEY",data.get("money"));
        intent.putExtra("POSITION",data.get("position"));


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(this.getResources().getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("title")))
                .setContentText(getString(R.string.push_win))
                .setAutoCancel(true)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getPackageName()+"/"+R.raw.winpush))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent);
        mNotificationManager.notify((int) notificatioId, notificationBuilder.build());
    }

    public void sendTask(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long notificatioId = System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "fcm";
            // The user-visible name of the channel.
            CharSequence name = "fcm";
            // The user-visible description of the channel.
            String description = "fcm";
            int importance = NotificationManager.IMPORTANCE_MAX;
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            mChannel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getPackageName()+"/"+R.raw.winpush),att);
            notificationManager.createNotificationChannel(mChannel);
        }
        Intent intent = new Intent(getApplicationContext(), PlayActivity.class); // Here pass your activity where you want to redirect.
        intent.putExtra("TITLE",data.get("title"));
        intent.putExtra("COMPANY",data.get("company"));
        intent.putExtra("TASK",data.get("deskTask"));
        intent.putExtra("LOGO",data.get("logo"));
        intent.putExtra("NUMBERTASK",data.get("numberTask"));
        intent.putExtra("TASKS",data.get("tasks"));
        intent.putExtra("TASKID",data.get("taskid"));
        intent.putExtra("TOTALTIME",data.get("totaltime"));

        long currentTime = Calendar.getInstance().getTimeInMillis();
        Log.i("LOG_play", "time0: " + currentTime);
        intent.putExtra("TIME",currentTime);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, PendingIntent.FLAG_CANCEL_CURRENT);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(this.getResources().getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("title")))
                .setContentText(data.get("body"))
                .setAutoCancel(true)
                .setChannelId("fcm")
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getPackageName()+"/"+R.raw.winpush))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent);
        mNotificationManager.notify((int) notificatioId, notificationBuilder.build());
    }

    public void sendNewGame(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long notificatioId = System.currentTimeMillis();

        Intent intent = new Intent(getApplicationContext(), GameInfoActivity.class); // Here pass your activity where you want to redirect.
        intent.putExtra("GAMEID", data.get("gameid"));
        intent.putExtra("SUBSCRIBE", data.get("gameSub"));
        intent.putExtra("TITLE", data.get("title"));
        intent.putExtra("NAME", data.get("company"));
        intent.putExtra("LOGO", data.get("logo"));
        intent.putExtra("BACKGROUND", data.get("background"));
        intent.putExtra("DATE", data.get("startdate") + " - " +data.get("enddate"));
        intent.putExtra("DESCRIPTION", data.get("description"));
        intent.putExtra("TASKS", data.get("tasks"));
        intent.putExtra("TIME", data.get("starttime") + " - " + data.get("endtime"));
        intent.putExtra("MONEY", data.get("reward"));
        intent.putExtra("PEOPLE", data.get("followers"));
        intent.putExtra("SHARE", data.get("gameid"));
        intent.putExtra("ADDRESS", data.get("address"));
        intent.putExtra("CATEGORY", data.get("category"));
        intent.putExtra("STATISTIC", "siteurl");
        //activity.finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(this.getResources().getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("title")))
                .setContentText(getString(R.string.push_newgame))
                .setAutoCancel(true)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getPackageName()+"/"+R.raw.winpush))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent);
        mNotificationManager.notify((int) notificatioId, notificationBuilder.build());

    }

    public void sendGameInfo(){

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            long notificatioId = System.currentTimeMillis();


            Intent intent = new Intent(getApplicationContext(), MainActivity.class); // Here pass your activity where you want to redirect.
            intent.putExtra("NAME", data.get("title"));
            intent.putExtra("PHOTO", data.get("photourl"));
            intent.putExtra("DESCRIPTION", data.get("reason"));
            //activity.finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


            PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(this.getResources().getString(R.string.app_name))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("title")))
                    .setContentText(getString(R.string.push_newgame))
                    .setAutoCancel(true)
                    .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getPackageName()+"/"+R.raw.winpush))
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                    .setContentIntent(contentIntent);
            mNotificationManager.notify((int) notificatioId, notificationBuilder.build());
        activity.finish();




    }

}
