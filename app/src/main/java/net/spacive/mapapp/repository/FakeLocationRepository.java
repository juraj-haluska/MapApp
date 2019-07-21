package net.spacive.mapapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.spacive.mapapp.model.LocationModel;

import java.util.ArrayList;
import java.util.List;

public class FakeLocationRepository implements LocationRepository {

    private static LocationRepository self;

    private boolean streamStarted = false;

    private List<LocationModel> allLocations = new ArrayList<>();
    private MutableLiveData<LocationModel> currentLocation = new MutableLiveData<>();

    private FakeLocationRepository() {

        currentLocation.setValue(new LocationModel(10, 10, 0, ""));

        allLocations.add(new LocationModel(0, 0, 0, ""));
        allLocations.add(new LocationModel(0, 1, 0, ""));
        allLocations.add(new LocationModel(0, 2, 0, ""));
        allLocations.add(new LocationModel(0, 3, 0, ""));
        allLocations.add(new LocationModel(0, 4, 0, ""));

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

                LocationModel newModel = new LocationModel(i,i,0,"");
                addLocation(newModel);
            }
            streamStarted = false;
        }).start();
    }
}
