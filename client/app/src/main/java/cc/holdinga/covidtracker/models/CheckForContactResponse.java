package cc.holdinga.covidtracker.models;

public class CheckForContactResponse {
    private boolean isContacted;
    private String transport2;
    private String transport1;
    private String timestamp;
    private double longitude;
    private double latitude;

    public String getTransport1() {
        return transport1;
    }

    public String getTransport2() {
        return transport2;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean getIsContacted() {
        return isContacted;
    }
}
