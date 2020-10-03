package cc.holdinga.covidtracker.utils;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

public class HttpUtils {

    public static final Callback noActionResponseHandler = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            System.out.println(e.toString());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            System.out.println(call.request().body() + "    " + response.body().string());
        }
    };
}
