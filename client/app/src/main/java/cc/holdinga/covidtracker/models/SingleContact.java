package cc.holdinga.covidtracker.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class SingleContact {

    private final String deviceName;

    private final LocalDateTime initialContactTime;

    private LocalDateTime lastContactTime;

    public SingleContact(String deviceName, LocalDateTime initialContactTime, LocalDateTime lastContactTime) {
        this.deviceName = deviceName;
        this.initialContactTime = initialContactTime;
        this.lastContactTime = lastContactTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public LocalDateTime getInitialContactTime() {
        return initialContactTime;
    }

    public LocalDateTime getLastContactTime() {
        return lastContactTime;
    }

    public void setLastContactTime(LocalDateTime lastContactTime) {
        this.lastContactTime = lastContactTime;
    }

    public long getMillisecondsAfterInitialContact() {
        return Math.abs(Duration.between(LocalDateTime.now(), initialContactTime).toMillis());
    }
    public long getMillisecondsAfterLastContact() {
        return Math.abs(Duration.between(LocalDateTime.now(), lastContactTime).toMillis());
    }
}
