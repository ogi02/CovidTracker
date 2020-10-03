package cc.holdinga.covidtracker;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import cc.holdinga.covidtracker.models.DeviceProperties;
import cc.holdinga.covidtracker.utils.BluetoothUtils;
import cc.holdinga.covidtracker.utils.Constants;
import cc.holdinga.covidtracker.utils.HttpUtils;
import cc.holdinga.covidtracker.utils.JsonParser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class MainActivity extends AppCompatActivity {
    private final OkHttpClient httpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerDevice();

        Button alertButton = findViewById(R.id.alertButton);
        alertButton.setOnClickListener(view -> reportInfectedness());
    }


    private void registerDevice() {
        Request request = new Request.Builder()
                .url(Constants.API_URL + "/register-device")
                .post(buildDevicePropertiesRequestBody())
                .build();
        httpClient.newCall(request).enqueue(HttpUtils.noActionResponseHandler);
    }

    private RequestBody buildDevicePropertiesRequestBody() {
        String json = JsonParser.stringify(new DeviceProperties(BluetoothUtils.currentDeviceName));
        return RequestBody.create(MediaType.parse("application/json"), json);
    }

    private void reportInfectedness() {
        Request request = new Request.Builder()
                .url(Constants.API_URL + "/report-infectedness")
                .post(buildDevicePropertiesRequestBody())
                .build();
        httpClient.newCall(request).enqueue(HttpUtils.noActionResponseHandler);
    }
}