package cc.holdinga.covidtracker;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.Button;
import cc.holdinga.covidtracker.models.Contact;
import okhttp3.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final String currentDeviceName = getCurrentDeviceName();
    private final Map<String, Contact> contacts = new HashMap<>();
    private final OkHttpClient httpClient = new OkHttpClient();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice contactedDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                addContact(contactedDevice.getName());
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                bluetoothAdapter.startDiscovery();
            }
        }
    };
    private final Callback noActionResponseHandler = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

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

    private void addContact(String contactedDevice) {
        if (contacts.containsKey(contactedDevice)) {
            if (isContactForReport(contacts.get(contactedDevice))) {
                contacts.remove(contactedDevice);
                reportContact(contactedDevice);
            }
            return;
        }
        if (isCurrentDeviceObligatedToReportForContact(contactedDevice)) {
            contacts.put(contactedDevice, new Contact(contactedDevice, LocalDateTime.now()));
        }
    }

    private boolean isContactForReport(Contact contact) {
        if (contact == null) {
            return false;
        }
        long minutesAfterFirstContact = Duration.between(LocalDateTime.now(), contact.getContactTime()).toMillis();
        return Math.abs(minutesAfterFirstContact) >= 10000;
    }

    private void reportContact(String contactedDevice) {
        Request request = buildReportContactRequest(contactedDevice);
        httpClient.newCall(request).enqueue(noActionResponseHandler);
    }

    private Request buildReportContactRequest(String contactedDevice) {
        RequestBody requestBody = new FormBody.Builder()
                .add("deviceName1", currentDeviceName)
                .add("deviceName2", contactedDevice)
                .build();
        return new Request.Builder()
                .url("https://api.mocki.io/v1/993b1ec5")
                .post(requestBody)
                .build();
    }

    private boolean isCurrentDeviceObligatedToReportForContact(String detectedDevice) {
        return detectedDevice != null && detectedDevice.compareTo(currentDeviceName) < 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchForNearbyDevices();

        Button reportInfectednessButton = findViewById(R.id.alertButton);
        reportInfectednessButton.setOnClickListener(view -> reportInfectedness());

    }

    private void searchForNearbyDevices() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    private void reportInfectedness() {
        Request request = buildReportInfectednessRequest();
        httpClient.newCall(request).enqueue(noActionResponseHandler);
    }

    private Request buildReportInfectednessRequest() {
        RequestBody requestBody = new FormBody.Builder()
                .add("deviceName", currentDeviceName)
                .build();
        return new Request.Builder()
                .url("https://api.mocki.io/v1/993b1ec5")
                .post(requestBody)
                .build();
    }
}