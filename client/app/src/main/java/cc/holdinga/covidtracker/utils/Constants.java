package cc.holdinga.covidtracker.utils;

public class Constants {
    public static final String API_URL = "http://192.168.200.132:3000";

    public static final String CHECK_FOR_CONTACT_ID = "checkForContact";
    public static final String SEARCHING_FOR_NEARBY_DEVICES_ID = "searchingForNearbyDevices";
    public static final String SENSOR_DATA_ID = "sensorData";

    public static final int ON_CONTACT_NOTIFICATION_ID = 1;
    public static final int SEARCHING_DEVICES_NOTIFICATION_ID = 2;
    public static final int SENSORS_DATA_NOTIFICATION_ID = 3;

    public static final long CHECK_FOR_CONTACT_INTERVAL = getOneMinutesInMilliseconds() * 3;
    public static final long IS_SINGLE_CONTACT_FOR_REPORT_INTERVAL = getOneMinutesInMilliseconds();
    public static final long IS_SINGLE_CONTACT_EXPIRED_INTERVAL = getOneMinutesInMilliseconds();

    private static long getOneMinutesInMilliseconds() {
        return  1000 * 60;
    }
}
