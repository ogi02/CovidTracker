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
import okhttp3.*;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private final String deviceName = getDeviceName();

    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                System.out.println(deviceName + deviceHardwareAddress);
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mBluetoothAdapter.startDiscovery();
            }
        }
    };

    private String getDeviceName() {
        if (mBluetoothAdapter == null) {
            return null;
        }
        return mBluetoothAdapter.getName();
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
        mBluetoothAdapter.startDiscovery();
    }

    private void reportInfectedness() {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = buildReportInfectednessRequest();
        httpClient.newCall(request).enqueue(new ReportInfectednessResponseHandler());
    }

    private Request buildReportInfectednessRequest() {
        RequestBody requestBody = new FormBody.Builder()
                .add("deviceName", deviceName)
                .build();
        return new Request.Builder()
                .url("https://api.mocki.io/v1/993b1ec5")
                .post(requestBody)
                .build();
    }

    private static class ReportInfectednessResponseHandler implements Callback {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) {

        }
    }
}