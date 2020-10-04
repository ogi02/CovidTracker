package cc.holdinga.covidtracker.services;

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
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import cc.holdinga.covidtracker.App;
import cc.holdinga.covidtracker.R;
import cc.holdinga.covidtracker.models.ContactForReporting;
import cc.holdinga.covidtracker.models.SingleContact;
import cc.holdinga.covidtracker.utils.BluetoothUtils;
import cc.holdinga.covidtracker.utils.Constants;
import cc.holdinga.covidtracker.utils.HttpUtils;
import cc.holdinga.covidtracker.utils.JsonParser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class SearchForNearbyDevicesService extends Service {
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final OkHttpClient httpClient = new OkHttpClient();

    private final Map<String, SingleContact> existingContacts = new HashMap<>();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice contactedDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                handleSingleContact(contactedDevice.getName());
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                bluetoothAdapter.startDiscovery();
            }
        }
    };

    private void handleSingleContact(String contactedDevice) {
        if (existingContacts.containsKey(contactedDevice)) {
            SingleContact existingContact = existingContacts.get(contactedDevice);
            if (isSingleContactForReport(existingContact)) {
                existingContacts.remove(contactedDevice);
                reportContact(contactedDevice);
            }
            if (isSingleContactExpired(existingContact)) {
                existingContacts.remove(contactedDevice);
            }else{
                existingContact.setLastContactTime(LocalDateTime.now());
            }
            return;
        }
        if (isCurrentDeviceObligedToReportForContact(contactedDevice)) {
            existingContacts.put(contactedDevice,
                    new SingleContact(contactedDevice, LocalDateTime.now(), LocalDateTime.now()));
        }
    }

    private boolean isSingleContactForReport(SingleContact existingContact) {
        return existingContact.getMillisecondsAfterInitialContact() >= Constants.IS_SINGLE_CONTACT_FOR_REPORT_INTERVAL;
    }


    private void reportContact(String contactedDevice) {
        Request request = buildReportContactRequest(contactedDevice);
        httpClient.newCall(request).enqueue(HttpUtils.noActionResponseHandler);
    }

    private Request buildReportContactRequest(String contactedDevice) {

        String json = JsonParser.stringify(new ContactForReporting(BluetoothUtils.currentDeviceName, contactedDevice));

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        return new Request.Builder()
                .url(Constants.API_URL + "/report-contact")
                .post(requestBody)
                .build();
    }

    private boolean isSingleContactExpired(SingleContact existingContact) {
        return existingContact.getMillisecondsAfterLastContact() >= Constants.IS_SINGLE_CONTACT_EXPIRED_INTERVAL;
    }

    private boolean isCurrentDeviceObligedToReportForContact(String contactedDevice) {
        return BluetoothUtils.currentDeviceName != null
                && contactedDevice != null
                && contactedDevice.compareTo(BluetoothUtils.currentDeviceName) > 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, App.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, Constants.SEARCHING_FOR_NEARBY_DEVICES_NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Searching...")
                .setContentText("Searching For Nearby Devices")
                .setSmallIcon(R.drawable.ic_contact)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(Constants.SEARCHING_DEVICES_NOTIFICATION_ID, notification);
        new Thread(this::searchForNearbyDevices).start();
        return START_STICKY;
    }

    private void searchForNearbyDevices() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
