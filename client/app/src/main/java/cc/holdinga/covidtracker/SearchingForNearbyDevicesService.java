package cc.holdinga.covidtracker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import cc.holdinga.covidtracker.models.ContactForReporting;
import cc.holdinga.covidtracker.models.SingleContact;
import cc.holdinga.covidtracker.utils.JsonParser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cc.holdinga.covidtracker.App.SEARCHING_FOR_NEARBY_DEVICES_ID;

public class SearchingForNearbyDevicesService extends Service {

    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final String currentDeviceName = getCurrentDeviceName();
    private final Map<String, SingleContact> existingContacts = new HashMap<>();
    private final OkHttpClient httpClient = new OkHttpClient();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            searchForNearbyDevices();
        }
    };

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                System.out.println("found");
                BluetoothDevice contactedDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                handleSingleContact(contactedDevice.getName());
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                System.out.println("not found");
                bluetoothAdapter.startDiscovery();
            }
        }
    };

    private final Callback noActionResponseHandler = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            System.out.println(e.toString());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            System.out.println(call.request().body() + "    " + response.body().string());
        }
    };

    private String getCurrentDeviceName() {
        if (bluetoothAdapter == null) {
            return null;
        }
        return bluetoothAdapter.getName();
    }

    private void handleSingleContact(String contactedDevice) {
        System.out.println(contactedDevice);
        if (existingContacts.containsKey(contactedDevice)) {
            SingleContact existingContact = existingContacts.get(contactedDevice);
            if (isSingleContactForReport(existingContact)) {
                existingContacts.remove(contactedDevice);
                reportContact(contactedDevice);
            }
            if (isSingleContactExpired(existingContact)) {
                existingContacts.remove(contactedDevice);
            }
            return;
        }
        if (isCurrentDeviceObligedToReportForContact(contactedDevice)) {
            existingContacts.put(contactedDevice, new SingleContact(contactedDevice, LocalDateTime.now()));
        }
    }

    private boolean isSingleContactForReport(SingleContact existingContact) {
//        if (existingContact == null) {
//            return false;
//        }
        return getMinutesAfterContact(existingContact) >= 10000;
    }

    private long getMinutesAfterContact(SingleContact contact) {
        return Math.abs(Duration.between(LocalDateTime.now(), contact.getContactTime()).toMillis());
    }

    private void reportContact(String contactedDevice) {
        Request request = buildReportContactRequest(contactedDevice);
        httpClient.newCall(request).enqueue(noActionResponseHandler);
    }

    private Request buildReportContactRequest(String contactedDevice) {

        String json = JsonParser.stringify(new ContactForReporting(currentDeviceName, contactedDevice));

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        System.out.println(currentDeviceName);
        return new Request.Builder()
                .url("http://192.168.200.132:3000/report-contact")
                .post(requestBody)
                .build();
    }

    private boolean isSingleContactExpired(SingleContact existingContact) {
        return getMinutesAfterContact(existingContact) >= 10000;
    }

    private boolean isCurrentDeviceObligedToReportForContact(String contactedDevice) {
        return contactedDevice != null && contactedDevice.compareTo(currentDeviceName) < 0;
    }

    private void searchForNearbyDevices() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, App.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, SEARCHING_FOR_NEARBY_DEVICES_ID)
                .setContentTitle("Searching...")
                .setContentText("Searching For Nearby Devices")
                .setSmallIcon(R.drawable.ic_contact)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(3, notification);
        System.out.println( "----main----" + Thread.currentThread().getId());
        new Thread(runnable).start();
//        searchForNearbyDevices();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
