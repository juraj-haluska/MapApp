package net.spacive.mapapp.viewmodel;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import net.spacive.mapapp.MapApp;
import net.spacive.mapapp.R;
import net.spacive.mapapp.model.LocationModel;
import net.spacive.mapapp.repository.AppDatabase;
import net.spacive.mapapp.repository.LocationRepository;
import net.spacive.mapapp.util.Comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapViewModel extends AndroidViewModel {

    private final LocationRepository locationRepository;
    private final MutableLiveData<LocationModel> focusedLocation;
    private final float filterRadius;

    public MapViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = ((MapApp) getApplication()).getAppDatabase();

        filterRadius = getApplication().getResources().getFloat(R.dimen.filter_radius);

        Log.d("BAA", Float.toString(filterRadius));

        locationRepository = db.locationDao();
        focusedLocation = new MutableLiveData<>();
    }

    // performs data filtration
    public LiveData<List<LocationModel>> getLocations() {
        return Transformations.map(locationRepository.getLocations(), input -> {
            if (input.size() <= 1) {
                return input;
            }

            List<LocationModel> filteredLocations = new ArrayList<>();
            filteredLocations.add(input.get(0));

            // filter data with constant radius in meters
            double prevLat = input.get(0).getLatitude();
            double prevLng = input.get(0).getLongitude();

            for (int i = 1; i < input.size(); i++) {
                LocationModel currentLocation = input.get(i);

                float[] results = new float[1];
                Location.distanceBetween(
                        prevLat,
                        prevLng,
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude(),
                        results
                );

                if (results[0] > filterRadius) {
                    prevLat = currentLocation.getLatitude();
                    prevLng = currentLocation.getLongitude();
                    filteredLocations.add(currentLocation);
                }
            }

            return filteredLocations;
        });
    }

    public MutableLiveData<LocationModel> getFocusedLocation() {
        return focusedLocation;
    }

    public void setMostEasternPosition() {
        new Thread(() -> {
            List<LocationModel> locations = locationRepository.getLocationsSync();
            if (locations != null && !locations.isEmpty()) {
                Comparator<LocationModel> comparator = Comparators.getLocationComparator(LocationModel::getLongitude);
                LocationModel mostEastern = Collections.max(locationRepository.getLocationsSync(), comparator);

                focusedLocation.postValue(mostEastern);
            }
        }).start();
    }

    public void setMostWesternPosition() {
        new Thread(() -> {
            List<LocationModel> locations = locationRepository.getLocationsSync();
            if (locations != null && !locations.isEmpty()) {
                Comparator<LocationModel> comparator = Comparators.getLocationComparator(LocationModel::getLongitude);
                LocationModel mostWestern = Collections.min(locationRepository.getLocationsSync(), comparator);

                focusedLocation.postValue(mostWestern);
            }
        }).start();
    }

    public void setMostNorthernPosition() {
        new Thread(() -> {
            List<LocationModel> locations = locationRepository.getLocationsSync();
            if (locations != null && !locations.isEmpty()) {

                Comparator<LocationModel> comparator = Comparators.getLocationComparator(LocationModel::getLatitude);
                LocationModel mostNorthern = Collections.max(locationRepository.getLocationsSync(), comparator);

                focusedLocation.postValue(mostNorthern);
            }
        }).start();
    }

    public void setMostSouthernPosition() {
        new Thread(() -> {
            List<LocationModel> locations = locationRepository.getLocationsSync();
            if (locations != null && !locations.isEmpty()) {
                Comparator<LocationModel> comparator = Comparators.getLocationComparator(LocationModel::getLatitude);
                LocationModel mostSouthern = Collections.min(locationRepository.getLocationsSync(), comparator);

                focusedLocation.postValue(mostSouthern);
            }
        }).start();
    }
}