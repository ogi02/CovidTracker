package cc.holdinga.covidtracker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import cc.holdinga.covidtracker.services.SensorDataService;

public class App extends Application {
    public static final String CHECK_FOR_CONTACT_ID = "checkForContact";
    public static final String SENSOR_DATA_ID = "sensorData";

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(getBaseContext(), SensorDataService.class));
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel checkForContactServiceNotificationChannel = new NotificationChannel(
                    CHECK_FOR_CONTACT_ID,
                    "Check For Contact Service Notification Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationChannel sensorDataNotificationChannel = new NotificationChannel(
                    SENSOR_DATA_ID,
                    "Sensor Data Service Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(checkForContactServiceNotificationChannel);
            manager.createNotificationChannel(sensorDataNotificationChannel);
        }
    }
}
