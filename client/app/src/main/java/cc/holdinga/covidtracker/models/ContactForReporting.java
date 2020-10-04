package cc.holdinga.covidtracker.models;

public class ContactForReporting {

    private final String deviceName1;

    private final String deviceName2;

    public ContactForReporting(String deviceName1, String deviceName2) {
        this.deviceName1 = deviceName1;
        this.deviceName2 = deviceName2;
    }

    public String getDeviceName1() {
        return deviceName1;
    }

    public String getDeviceName2() {
        return deviceName2;
    }
}
