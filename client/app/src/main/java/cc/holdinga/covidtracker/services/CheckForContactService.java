package cc.holdinga.covidtracker.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import cc.holdinga.covidtracker.MapActivity;
import cc.holdinga.covidtracker.R;
import cc.holdinga.covidtracker.models.CheckForContactResponse;
import cc.holdinga.covidtracker.models.DeviceProperties;
import cc.holdinga.covidtracker.utils.BluetoothUtils;
import cc.holdinga.covidtracker.utils.Constants;
import cc.holdinga.covidtracker.utils.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class CheckForContactService extends Service {
    private final OkHttpClient httpClient = new OkHttpClient();

    private final Runnable runnable = () -> new Timer().scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            Request request = buildCheckForContactRequest();
            httpClient.newCall(request).enqueue(responseHandler);
        }
    }, 0, Constants.CHECK_FOR_CONTACT_INTERVAL);

    private final Callback responseHandler = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response httpResponse) {
            ResponseBody responseBody = httpResponse.body();
            if (responseBody == null) {
                return;
            }

            CheckForContactResponse checkForContactResponse;
            try {
                checkForContactResponse = JsonParser.parse(responseBody.string(), CheckForContactResponse.class);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (checkForContactResponse == null) {
                return;
            }

            if (checkForContactResponse.getIsContacted()) {
                String notificationContent = "You may have been in contact with a COVID-19 infected person while being " + checkForContactResponse.getTransport1();
                if(!checkForContactResponse.getTransport1().equals(checkForContactResponse.getTransport2())){
                    notificationContent += " or " + checkForContactResponse.getTransport2();
                }
                notificationContent += " on " + checkForContactResponse.getTimestamp();
                pushNotificationForContact(notificationContent);
            }
        }
    };

    private Request buildCheckForContactRequest() {
        String json = JsonParser.stringify(new DeviceProperties(BluetoothUtils.currentDeviceName));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        return new Request.Builder()
                .url(Constants.API_URL + "/check-for-contact")
                .post(requestBody)
                .build();
    }


    private void pushNotificationForContact(String notificationContent) {
        Intent resultIntent = new Intent(this, MapActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat
                .Builder(CheckForContactService.this, Constants.CHECK_FOR_CONTACT_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_contact)
                .setContentTitle("Warning")
                .setContentText(notificationContent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationContent))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManagerCompat.from(CheckForContactService.this).notify(
                Constants.ON_CONTACT_NOTIFICATION_ID,
                notification
        );
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(runnable).start();
        return START_STICKY;
    }
}
