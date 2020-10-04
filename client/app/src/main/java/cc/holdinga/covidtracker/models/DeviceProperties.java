package cc.holdinga.covidtracker.models;

public class DeviceProperties {

    private final String deviceName;

    public DeviceProperties(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
