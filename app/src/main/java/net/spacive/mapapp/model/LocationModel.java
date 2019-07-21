package net.spacive.mapapp.model;

public class LocationModel {
    private double latitude;
    private double longitude;
    private long timestamp;
    private String source;

    public LocationModel(double latitude, double longitude, long timestamp, String source) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.source = source;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }
}
