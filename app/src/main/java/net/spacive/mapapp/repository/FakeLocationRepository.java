package net.spacive.mapapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.spacive.mapapp.model.LocationModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FakeLocationRepository implements LocationRepository {

    private static LocationRepository self;

    private boolean streamStarted = false;

    private List<LocationModel> allLocations = new ArrayList<>();
    private MutableLiveData<LocationModel> currentLocation = new MutableLiveData<>();

    private FakeLocationRepository() {

        currentLocation.setValue(new LocationModel(10, 10, new Date(Calendar.getInstance().getTimeInMillis()), "", 5));

        allLocations.add(new LocationModel(Double.MIN_NORMAL, 0, new Date(Calendar.getInstance().getTimeInMillis()), "", 5));
        allLocations.add(new LocationModel(0, 1, new Date(Calendar.getInstance().getTimeInMillis()), "", 5));
        allLocations.add(new LocationModel(0, 2, new Date(Calendar.getInstance().getTimeInMillis()), "",5 ));
        allLocations.add(new LocationModel(0, 3, new Date(Calendar.getInstance().getTimeInMillis()), "", 5));
        allLocations.add(new LocationModel(0, 4, new Date(Calendar.getInstance().getTimeInMillis()), "", 5));

        startDataStream();
    }

    public static LocationRepository getInstance() {
        if (self == null) {
            self = new FakeLocationRepository();
        }

        return self;
    }

    @Override
    public LiveData<LocationModel> getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public List<LocationModel> getAllLocations() {
        return allLocations;
    }

    private synchronized void addLocation(LocationModel locationModel) {
        currentLocation.postValue(locationModel);
        allLocations.add(locationModel);
    }

    private void startDataStream() {
        if (streamStarted)
        {
            return;
        }

        streamStarted = true;

        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                LocationModel newModel = new LocationModel(i,i,new Date(Calendar.getInstance().getTimeInMillis()),"zdroj: " + i, 5);
                addLocation(newModel);
            }
            streamStarted = false;
        }).start();
    }
}
