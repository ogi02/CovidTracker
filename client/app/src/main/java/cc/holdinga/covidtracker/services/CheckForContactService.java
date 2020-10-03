package cc.holdinga.covidtracker.services;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;

import cc.holdinga.covidtracker.R;
import cc.holdinga.covidtracker.models.CheckForContactResponse;
import cc.holdinga.covidtracker.models.DeviceProperties;
import cc.holdinga.covidtracker.utils.JsonParser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static cc.holdinga.covidtracker.App.CHECK_FOR_CONTACT_ID;

public class CheckForContactService extends Service {

    private final OkHttpClient httpClient = new OkHttpClient();
    private NotificationManagerCompat notificationManager;
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final String currentDeviceName = getCurrentDeviceName();
    private Handler handler = new Handler();

    private String getCurrentDeviceName() {
        if (bluetoothAdapter == null) {
            return null;
        }
        return bluetoothAdapter.getName();
    }

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

        String json = JsonParser.stringify(new DeviceProperties(currentDeviceName));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        return new Request.Builder()
                .url("http://192.168.200.132:3000/check-for-contact")
                .post(requestBody)
                .build();
    }

    private final Callback ResponseHandler = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response httpResponse) {
            handler.postDelayed(checkForContacted, 1000);
            try {
                ResponseBody responseBody = httpResponse.body();
                if (responseBody == null) {
                    return;
                }
                CheckForContactResponse checkForContactResponse =  JsonParser.parse(responseBody.string(), CheckForContactResponse.class);

                if(checkForContactResponse == null){
                    return;
                }
                if(checkForContactResponse.getIsContacted()){
                    notificationManager = NotificationManagerCompat.from(CheckForContactService.this);

                    Notification notification = new NotificationCompat.Builder(CheckForContactService.this, CHECK_FOR_CONTACT_ID)
                            .setSmallIcon(R.drawable.ic_contact)
                            .setContentTitle("Warning")
                            .setContentText("You were in contact with an infected person")
                            .build();
                    notificationManager.notify(1, notification);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

}
