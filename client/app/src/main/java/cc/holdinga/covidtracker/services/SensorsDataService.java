package cc.holdinga.covidtracker.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import cc.holdinga.covidtracker.App;
import cc.holdinga.covidtracker.R;
import cc.holdinga.covidtracker.models.SensorData;
import cc.holdinga.covidtracker.utils.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.util.*;

import static cc.holdinga.covidtracker.App.SENSOR_DATA_ID;

public class SensorsDataService extends Service {
    private static final Map<Integer, String> SENSOR_TYPES = new HashMap<>();

    static {
        SENSOR_TYPES.put(Sensor.TYPE_ACCELEROMETER, "accelerometer");
        SENSOR_TYPES.put(Sensor.TYPE_GYROSCOPE, "gyroscope");
        SENSOR_TYPES.put(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, "gyroscope_uncalibrated");
        SENSOR_TYPES.put(Sensor.TYPE_LINEAR_ACCELERATION, "linear_acceleration");
        SENSOR_TYPES.put(Sensor.TYPE_ROTATION_VECTOR, "rotation_vector");
        SENSOR_TYPES.put(Sensor.TYPE_GAME_ROTATION_VECTOR, "game_rotation_vector");
    }

    private final OkHttpClient httpClient = new OkHttpClient();

    private final Map<Integer, List<Double>> sensorsRawData = new HashMap<>();

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            int sensorType = event.sensor.getType();
            System.out.println(sensorType + " : " + Arrays.toString(event.values));
            List<Double> sensorValues = sensorsRawData.get(sensorType);
            if (sensorValues == null) {
                return;
            }
            double magnitude;
            if (sensorType == Sensor.TYPE_ROTATION_VECTOR || sensorType == Sensor.TYPE_GAME_ROTATION_VECTOR) {
                magnitude = calculateMagnitudeForRotationVector(event.values);
            } else {
                magnitude = calculateMagnitude(event.values);
            }
            sensorValues.add(magnitude);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private double calculateMagnitudeForRotationVector(float[] values) {
        return Math.sin(Math.acos(values[3]));
    }

    private final Callback responseHandler = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response httpResponse) {

        }
    };

    private double calculateMagnitude(float[] values) {
        return Math.pow((Math.pow(values[0], 2) + Math.pow(values[1], 2) + Math.pow(values[2], 2)), 0.5);
    }

    private final Runnable runnable = () -> {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sensorsRawData.clear();
                SENSOR_TYPES.keySet().forEach(sensorType -> {
                    sensorsRawData.put(sensorType, new ArrayList<>());
                    Sensor sensor = sensorManager.getDefaultSensor(sensorType);
                    sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                });
                setTimeout(() -> {
                    sensorManager.unregisterListener(sensorEventListener);
                    sendSensorData();
                }, 30000);
            }
        }, 20, 1000 * 60 * 10);
    };

    private void sendSensorData() {
        Map<String, SensorData> sensorsProcessedData = new HashMap<>();
        sensorsRawData.forEach((sensorType, sensorRawData) -> {
            String sensorKey = SENSOR_TYPES.get(sensorType);
            sensorsProcessedData.put(sensorKey, processSensorRawData(sensorRawData));
        });
        Request request = buildSensorDataRequest(sensorsProcessedData);
        httpClient.newCall(request).enqueue(responseHandler);
    }

    private SensorData processSensorRawData(List<Double> sensorRawData) {
        double max = sensorRawData.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double min = sensorRawData.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double mean = sensorRawData.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double std = calculateStd(sensorRawData);
        return new SensorData(max, min, mean, std);
    }

    private Request buildSensorDataRequest(Map<String, SensorData> sensorsProcessedData) {
        String json = JsonParser.stringify(sensorsProcessedData);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        return new Request.Builder()
                .url("http://192.168.200.132:3000/sensor-data")
                .post(requestBody)
                .build();
    }

    private double calculateStd(List<Double> values) {
        double mean = values.stream().mapToDouble(Double::valueOf).average().orElse(0);
        int valuesSize = values.size();
        return Math.sqrt(values
                .stream()
                .mapToDouble(Double::doubleValue)
                .map(value -> Math.pow((value - mean), 2))
                .sum() / valuesSize);
    }

    private static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, App.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, SENSOR_DATA_ID)
                .setContentTitle("sssss")
                .setContentText("dgkdni")
                .setSmallIcon(R.drawable.ic_contact)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2, notification);

        System.out.println("- 0- " + Thread.currentThread().getId());

        new Thread(runnable).start();

        return START_STICKY;
    }
}
