package net.spacive.mapapp.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class LocationModel {
    private double latitude;
    private double longitude;
    private Date date;
    private String source;
    private float accuracy;

    public LocationModel(double latitude, double longitude, Date date, String source, float accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.source = source;
        this.accuracy = accuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }
}
