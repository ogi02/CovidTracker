package cc.holdinga.covidtracker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import cc.holdinga.covidtracker.services.CheckForContactService;
import cc.holdinga.covidtracker.services.SearchForNearbyDevicesService;
import cc.holdinga.covidtracker.services.SensorsDataService;
import cc.holdinga.covidtracker.utils.Constants;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();

//        startService(new Intent(getBaseContext(), SensorsDataService.class));
        startService(new Intent(getBaseContext(), CheckForContactService.class));
        startService(new Intent(getBaseContext(), SearchForNearbyDevicesService.class));
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel checkForContactServiceNotificationChannel = new NotificationChannel(
                    Constants.CHECK_FOR_CONTACT_NOTIFICATION_CHANNEL_ID,
                    "Check For Contact Service Notification Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationChannel sensorsDataServiceNotificationChannel = new NotificationChannel(
                    Constants.SENSOR_DATA__NOTIFICATION_CHANNEL_ID,
                    "Sensor Data Service Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationChannel searchingForNearbyDevicesServiceNotificationChannel = new NotificationChannel(
                    Constants.SEARCHING_FOR_NEARBY_DEVICES_NOTIFICATION_CHANNEL_ID,
                    "Searching For Nearby Devices Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(checkForContactServiceNotificationChannel);
            manager.createNotificationChannel(searchingForNearbyDevicesServiceNotificationChannel);
            manager.createNotificationChannel(sensorsDataServiceNotificationChannel);
        }
    }
}
