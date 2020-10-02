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
import cc.holdinga.covidtracker.models.ContactForReporting;
import cc.holdinga.covidtracker.models.SingleContact;
import cc.holdinga.covidtracker.models.DeviceProperties;
import cc.holdinga.covidtracker.utils.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final String currentDeviceName = getCurrentDeviceName();
    private final Map<String, SingleContact> existingContacts = new HashMap<>();
    private final OkHttpClient httpClient = new OkHttpClient();

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
        if (isCurrentDeviceObligatedToReportForContact(contactedDevice)) {
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

        String json = "";
        try {
            json = JsonParser.stringify(new ContactForReporting(currentDeviceName, contactedDevice));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        return new Request.Builder()
                .url("http://192.168.200.132:3000/report-contact")
                .post(requestBody)
                .build();
    }

    private boolean isSingleContactExpired(SingleContact existingContact) {
        return getMinutesAfterContact(existingContact) >= 1000;
    }

    private boolean isCurrentDeviceObligatedToReportForContact(String contactedDevice) {
        return contactedDevice != null && contactedDevice.compareTo(currentDeviceName) < 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerDevice();
        searchForNearbyDevices();

        startService(new Intent(getBaseContext(), isContactedService.class));

        Button reportInfectednessButton = findViewById(R.id.alertButton);
        reportInfectednessButton.setOnClickListener(view -> reportInfectedness());

    }

    private void registerDevice(){
        Request request = new Request.Builder()
                .url("http://192.168.200.132:3000/register-device")
                .post(buildDevicePropertiesRequestBody())
                .build();
        httpClient.newCall(request).enqueue(noActionResponseHandler);
    }

    private RequestBody buildDevicePropertiesRequestBody(){
        String json = "";
        try {
            json = JsonParser.stringify(new DeviceProperties(currentDeviceName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RequestBody.create(MediaType.parse("application/json"), json);
    }

    private void searchForNearbyDevices() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    private void reportInfectedness() {
        Request request = new Request.Builder()
                .url("http://192.168.200.132:3000/report-infectedness")
                .post(buildDevicePropertiesRequestBody())
                .build();
        httpClient.newCall(request).enqueue(noActionResponseHandler);
    }
}