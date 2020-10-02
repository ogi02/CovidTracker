package cc.holdinga.covidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.Button;
import okhttp3.*;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private final String deviceName = getDeviceName();

    private String getDeviceName() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button alertButton = findViewById(R.id.alertButton);
        alertButton.setOnClickListener(view -> reportInfectedness());
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