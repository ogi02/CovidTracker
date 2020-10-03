package cc.holdinga.covidtracker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cc.holdinga.covidtracker.App.CHECK_FOR_CONTACT_ID;

public class checkForContactService extends Service {

    private final OkHttpClient httpClient = new OkHttpClient();
    private NotificationManagerCompat notificationManager;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        checkForContacted.run();
        return START_STICKY;
    }

    private Runnable checkForContacted = new Runnable() {
        @Override
        public void run() {
            Request request = buildWasIsContactCheckRequest();
            httpClient.newCall(request).enqueue(ResponseHandler);
        }
    };

    private Request buildWasIsContactCheckRequest(){
        return new Request.Builder()
                .url("https://api.mocki.io/v1/993b1ec5")
                .get()
                .build();
    }

    private final Callback ResponseHandler = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) {
            if(true){
                notificationManager = NotificationManagerCompat.from(checkForContactService.this);

                Notification notification = new NotificationCompat.Builder(checkForContactService.this, CHECK_FOR_CONTACT_ID)
                        .setSmallIcon(R.drawable.ic_contact)
                        .setContentTitle("Warning")
                        .setContentText("You were in contact with an infected person")
                        .build();
                notificationManager.notify(1, notification);
            }
            handler.postDelayed(checkForContacted, 1000 * 60 * 3);
        }
    };

}
