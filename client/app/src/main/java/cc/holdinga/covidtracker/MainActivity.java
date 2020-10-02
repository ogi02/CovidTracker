package cc.holdinga.covidtracker;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private final String deviceName = getDeviceName();
    private final OkHttpClient httpClient = new OkHttpClient();

    private String getDeviceName() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerDevice();
        Button alertButton = findViewById(R.id.alertButton);
        alertButton.setOnClickListener(view -> reportInfectedness());
    }

    private void registerDevice(){
        Request request = new Request.Builder()
                .url("https://api.mocki.io/v1/993b1ec5")
                .post(buildDevicePropertiesRequestBody())
                .build();
        httpClient.newCall(request).enqueue(new EmptyResponseHandler());
    }

    private void reportInfectedness() {
        Request request = new Request.Builder()
                .url("https://api.mocki.io/v1/993b1ec5")
                .post(buildDevicePropertiesRequestBody())
                .build();
        httpClient.newCall(request).enqueue(new EmptyResponseHandler());
    }

    private RequestBody buildDevicePropertiesRequestBody(){
        return new FormBody.Builder()
                .add("name", deviceName)
                .build();
    }

    private void sendMeeting(String nearbyDeviceName) {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String myDeviceName = mBluetoothAdapter.getName();

        int nearbyDeviceHash = nearbyDeviceName.hashCode();
        int myDeviceHash = myDeviceName.hashCode();

        if(myDeviceHash > nearbyDeviceHash){

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.mocki.io/v1/993b1ec5")
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response){

                }
            });
        }
    }

    private static class EmptyResponseHandler implements Callback {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) {

        }
    }
}