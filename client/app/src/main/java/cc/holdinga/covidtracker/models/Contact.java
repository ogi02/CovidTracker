package cc.holdinga.covidtracker.models;

import java.time.LocalDateTime;

public class Contact {

    private final String deviceName;

    private final LocalDateTime contactTime;

    public Contact(String deviceName, LocalDateTime contactTime) {
        this.deviceName = deviceName;
        this.contactTime = contactTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public LocalDateTime getContactTime() {
        return contactTime;
    }
}
