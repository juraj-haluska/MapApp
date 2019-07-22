package net.spacive.mapapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

@Entity(tableName = "locations")
public class LocationModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }
}
