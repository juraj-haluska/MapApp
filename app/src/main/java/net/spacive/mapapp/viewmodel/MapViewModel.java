package net.spacive.mapapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.spacive.mapapp.MapApp;
import net.spacive.mapapp.model.LocationModel;
import net.spacive.mapapp.repository.AppDatabase;
import net.spacive.mapapp.repository.LocationRepository;
import net.spacive.mapapp.util.Comparators;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapViewModel extends AndroidViewModel {

    private LocationRepository locationRepository;

    private MutableLiveData<LocationModel> focusedLocation;

    public MapViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = ((MapApp) getApplication()).getAppDatabase();

        locationRepository = db.locationDao();
        focusedLocation = new MutableLiveData<>();
    }


    public LiveData<List<LocationModel>> getLocations() {
        return locationRepository.getLocations();
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