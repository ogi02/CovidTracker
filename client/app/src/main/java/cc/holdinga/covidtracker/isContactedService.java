package cc.holdinga.covidtracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class isContactedService extends Service {

    private final OkHttpClient httpClient = new OkHttpClient();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Request request = buildWasIsContactCheckRequest();
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                httpClient.newCall(request).enqueue(ResponseHandler);
            }
        },0,1000 * 60 * 3);

    }

    private Request buildWasIsContactCheckRequest(){
        return new Request.Builder()
                .url("https://api.mocki.io/v1/993b1ec5")
                .get()
                .build();
    }

    private final Callback ResponseHandler = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) {
            if(!response.isSuccessful()){
                return;
            }
            if(true){

            }
        }
    };

    @Override
    public void onDestroy() {
        return;
    }
}
