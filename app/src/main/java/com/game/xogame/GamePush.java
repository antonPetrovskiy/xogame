package com.game.xogame;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.game.xogame.views.game.PlayActivity;
import com.game.xogame.views.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class GamePush extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        sendNotification(data);





//        final SharedPreferences sharedPref = this.getSharedPreferences("myPref", MODE_PRIVATE);
//
//        Log.i("WTF", "1: "+n+"");
//        new CountDownTimer(60000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                n--;
//                sharedPref.edit().putString("sec", n+"").commit();
//                Log.i("WTF", "1: "+n+"");
//            }
//
//            public void onFinish() {
//                //toMainActivity();
//            }
//        }.start();

    }


    private void sendNotification(Map<String, String> data) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long notificatioId = System.currentTimeMillis();

        Intent intent = new Intent(getApplicationContext(), PlayActivity.class); // Here pass your activity where you want to redirect.
        intent.putExtra("TITLE",data.get("title"));
        intent.putExtra("TASK",data.get("deskTask"));
        intent.putExtra("LOGO",data.get("logo"));
        intent.putExtra("NUMBERTASK",data.get("numberTask"));
        intent.putExtra("TASKS",data.get("tasks"));
        intent.putExtra("TASKID",data.get("taskid"));

        long currentTime = Calendar.getInstance().getTimeInMillis();
        intent.putExtra("TIME",currentTime+"");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(this.getResources().getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("title")))
                .setContentText(data.get("body"))
                .setAutoCancel(true)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getPackageName()+"/"+R.raw.push))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent);
        mNotificationManager.notify((int) notificatioId, notificationBuilder.build());
    }

}
