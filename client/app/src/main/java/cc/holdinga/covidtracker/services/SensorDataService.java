package cc.holdinga.covidtracker.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import cc.holdinga.covidtracker.App;
import cc.holdinga.covidtracker.MainActivity;
import cc.holdinga.covidtracker.R;

import static cc.holdinga.covidtracker.App.SENSOR_DATA_ID;

public class SensorDataService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, App.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, SENSOR_DATA_ID)
                .setContentTitle("sssss")
                .setContentText("dgkdni")
                .setSmallIcon(R.drawable.ic_contact)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2, notification);
        return START_STICKY;
    }
}
