package com.example.schoolNeedsOrganizer.AppSettings;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.example.exercises.R;

import java.util.Calendar;
import java.util.Date;

public class MyNotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(notificationId, notification);
        scheduleNotification(context,127);
    }

    public void scheduleNotification(Context context, int notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String time = com.example.schoolNeedsOrganizer.AppSettings.Settings.getTime();
        int hourSetter = Integer.valueOf(time.split(":")[0]);
        int minuteSetter = Integer.valueOf(time.split(":")[1]);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        System.out.println(((hour == hourSetter) && (minute > minuteSetter)));
        if ((hour > hourSetter) || ((hour == hourSetter) && (minute >= minuteSetter))) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hourSetter);
        calendar.set(Calendar.MINUTE, minuteSetter);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("SNO", "SchoolNeedsOrganize",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("SchoolNeedsOrganizer");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
        Notification.Builder builder = new Notification.Builder(context, "SNO");
        Intent intent = new Intent("android.intent.action.SCREEN");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                .setTicker("Привет! Проверь уведомление!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentText("Не забудь выполнить все запланированные на сегодня дела!")
                .setContentTitle("Напоминание");
        Date date = calendar.getTime();
        System.out.println(date.toString());
        Notification notification = builder.build();
        Intent nIntent = new Intent(context, MyNotificationPublisher.class);
        nIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
        nIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntentSecond = PendingIntent.getBroadcast(context, 0, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentSecond);
    }
}
