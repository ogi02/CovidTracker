package cc.holdinga.covidtracker.utils;

public class Constants {
    public static final String API_URL = "http://gorchilov.net:3000";

    public static final String CHECK_FOR_CONTACT_NOTIFICATION_CHANNEL_ID = "checkForContact";
    public static final String SEARCHING_FOR_NEARBY_DEVICES_NOTIFICATION_CHANNEL_ID = "searchingForNearbyDevices";
    public static final String SENSOR_DATA__NOTIFICATION_CHANNEL_ID = "sensorData";

    public static final int ON_CONTACT_NOTIFICATION_ID = 1;
    public static final int SEARCHING_DEVICES_NOTIFICATION_ID = 2;
    public static final int SENSORS_DATA_NOTIFICATION_ID = 3;


    public static final long CHECK_FOR_CONTACT_INTERVAL = (getOneMinuteInMilliseconds() * 10) / 30;
    public static final long IS_SINGLE_CONTACT_FOR_REPORT_INTERVAL = getOneMinuteInMilliseconds() / 6;
    public static final long IS_SINGLE_CONTACT_EXPIRED_INTERVAL = getOneMinuteInMilliseconds() * 3;

    public static final int COLLECTING_SENSORS_DATA_INTERVAL = 5000;
    public static final long INTERVAL_BETWEEN_COLLECTING_SENSORS_DATA = getOneMinuteInMilliseconds() / 3;


    private static long getOneMinuteInMilliseconds() {
        return  1000 * 60;
    }
}
