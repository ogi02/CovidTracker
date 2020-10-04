package cc.holdinga.covidtracker.models;

import java.util.Map;

public class SensorsDataRequest {
    private final String deviceName;
    private final Map<String, SensorData> sensorsData;

    public SensorsDataRequest(String deviceName, Map<String, SensorData> sensorsData) {
        this.deviceName = deviceName;
        this.sensorsData = sensorsData;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public Map<String, SensorData> getSensorsData() {
        return sensorsData;
    }
}
