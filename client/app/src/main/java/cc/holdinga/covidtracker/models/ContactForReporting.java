package cc.holdinga.covidtracker.models;

public class ContactForReporting {
    private final String deviceName1;
    private final String deviceName2;
    private final double latitude;
    private final double longitude;

    public ContactForReporting(String deviceName1, String deviceName2, double latitude, double longitude) {
        this.deviceName1 = deviceName1;
        this.deviceName2 = deviceName2;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDeviceName1() {
        return deviceName1;
    }

    public String getDeviceName2() {
        return deviceName2;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
