package cc.holdinga.covidtracker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHECK_FOR_CONTACT_ID = "checkForContact";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel checkForContactServiceChannel = new NotificationChannel(
                    CHECK_FOR_CONTACT_ID,
                    "Check For Contact Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(checkForContactServiceChannel);
        }
    }
}
