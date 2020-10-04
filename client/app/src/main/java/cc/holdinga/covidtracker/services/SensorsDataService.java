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
import android.media.MediaRecorder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import cc.holdinga.covidtracker.App;
import cc.holdinga.covidtracker.R;
import cc.holdinga.covidtracker.models.SensorData;
import cc.holdinga.covidtracker.models.SensorsDataRequest;
import cc.holdinga.covidtracker.utils.BluetoothUtils;
import cc.holdinga.covidtracker.utils.Constants;
import cc.holdinga.covidtracker.utils.HttpUtils;
import cc.holdinga.covidtracker.utils.JsonParser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SensorsDataService extends Service {
    private static final Map<Integer, String> ANDROID_SENSOR_TYPES = new HashMap<>();

    static {
        ANDROID_SENSOR_TYPES.put(Sensor.TYPE_ACCELEROMETER, "accelerometer");
        ANDROID_SENSOR_TYPES.put(Sensor.TYPE_GYROSCOPE, "gyroscope");
        ANDROID_SENSOR_TYPES.put(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, "gyroscope_uncalibrated");
        ANDROID_SENSOR_TYPES.put(Sensor.TYPE_LINEAR_ACCELERATION, "linear_acceleration");
        ANDROID_SENSOR_TYPES.put(Sensor.TYPE_ROTATION_VECTOR, "rotation_vector");
        ANDROID_SENSOR_TYPES.put(Sensor.TYPE_GAME_ROTATION_VECTOR, "game_rotation_vector");
        ANDROID_SENSOR_TYPES.put(Sensor.TYPE_ORIENTATION, "orientation");
    }

    private final OkHttpClient httpClient = new OkHttpClient();
    private final MediaRecorder mediaRecorder = new MediaRecorder();

    private final Map<Integer, List<Double>> androidSensorsRawData = new HashMap<>();
    private final List<Integer> mediaRecorderRawData = new ArrayList<>();

    private final SensorEventListener sensorsEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            int sensorType = event.sensor.getType();
            List<Double> sensorValues = androidSensorsRawData.get(sensorType);
            if (sensorValues == null) {
                return;
            }

            double magnitude;
            switch (sensorType) {
                case Sensor.TYPE_ROTATION_VECTOR:
                case Sensor.TYPE_GAME_ROTATION_VECTOR:
                    magnitude = calculateMagnitudeForRotationVector(event.values);
                    break;
                default:
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

    private double calculateMagnitude(float[] values) {
        return Math.pow((Math.pow(values[0], 2) + Math.pow(values[1], 2) + Math.pow(values[2], 2)), 0.5);
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
        Notification notification = new NotificationCompat.Builder(this, Constants.SENSOR_DATA__NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Sending sensors data...")
                .setContentText("Sending sensors data...")
                .setSmallIcon(R.drawable.ic_contact)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(20, notification);

        new Thread(this::handleSensorsData).start();

        return START_STICKY;
    }

    private void handleSensorsData() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                startAndroidSensors(sensorManager);
                startMediaRecorder();
                Timer mediaRecorderTimer = new Timer();
                mediaRecorderTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        mediaRecorderRawData.add(mediaRecorder.getMaxAmplitude());
                    }
                }, 0, 20);
                setTimeout(() -> {
                    sensorManager.unregisterListener(sensorsEventListener);
                    mediaRecorder.stop();
                    mediaRecorderTimer.cancel();
                    sendSensorsData();
                }, Constants.COLLECTING_SENSORS_DATA_INTERVAL);
            }
        }, 0, Constants.INTERVAL_BETWEEN_COLLECTING_SENSORS_DATA);
    }

    private void startAndroidSensors(SensorManager sensorManager) {
        androidSensorsRawData.clear();
        ANDROID_SENSOR_TYPES.keySet().forEach(sensorType -> {
            androidSensorsRawData.put(sensorType, new ArrayList<>());
            Sensor sensor = sensorManager.getDefaultSensor(sensorType);
            sensorManager.registerListener(sensorsEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        });
    }

    private void startMediaRecorder() {
        mediaRecorderRawData.clear();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile("/dev/null");
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendSensorsData() {
        Map<String, SensorData> sensorsProcessedData = new HashMap<>();
        androidSensorsRawData.forEach((sensorType, sensorRawData) -> {
            String sensorKey = ANDROID_SENSOR_TYPES.get(sensorType);
            sensorsProcessedData.put(sensorKey, processSensorRawData(sensorRawData));
        });
        sensorsProcessedData.put("sound", processMediaRecorderRawData());
        Request request = buildSensorDataRequest(sensorsProcessedData);
        httpClient.newCall(request).enqueue(HttpUtils.noActionResponseHandler);
    }

    private SensorData processSensorRawData(List<Double> sensorRawData) {
        double max = sensorRawData.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double min = sensorRawData.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double mean = sensorRawData.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double std = calculateStd(sensorRawData);
        return new SensorData(max, min, mean, std);
    }

    private double calculateStd(List<Double> values) {
        int valuesSize = values.size();
        if (valuesSize == 0) {
            return 0;
        }
        double mean = values.stream().mapToDouble(Double::valueOf).average().orElse(0);
        return Math.sqrt(values
                .stream()
                .mapToDouble(Double::doubleValue)
                .map(value -> Math.pow((value - mean), 2))
                .sum() / valuesSize);
    }

    private SensorData processMediaRecorderRawData() {
        List<Double> filteredData = mediaRecorderRawData.stream()
                .filter(value -> value != 0)
                .map(value -> (double) value / 1000)
                .collect(Collectors.toList());
        return processSensorRawData(filteredData);
    }

    private Request buildSensorDataRequest(Map<String, SensorData> sensorsData) {
        SensorsDataRequest sensorsDataRequest = new SensorsDataRequest(BluetoothUtils.currentDeviceName, sensorsData);
        String json = JsonParser.stringify(sensorsDataRequest);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        return new Request.Builder()
                .url(Constants.API_URL + "/sensor-data")
                .post(requestBody)
                .build();
    }
}
