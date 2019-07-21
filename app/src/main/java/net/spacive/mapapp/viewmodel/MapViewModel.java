package net.spacive.mapapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.spacive.mapapp.model.LocationModel;
import net.spacive.mapapp.repository.FakeLocationRepository;
import net.spacive.mapapp.repository.LocationRepository;
import net.spacive.mapapp.util.Comparators;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapViewModel extends ViewModel {

    private LocationRepository locationRepository;

    private MutableLiveData<LocationModel> focusedLocation;

    public MapViewModel() {
        locationRepository = FakeLocationRepository.getInstance();

        focusedLocation = new MutableLiveData<>();
    }

    public LiveData<List<LocationModel>> getLocations() {
        return locationRepository.getLocations();
    }

    public MutableLiveData<LocationModel> getFocusedLocation() {
        return focusedLocation;
    }

    public void setMostEasternPosition() {
        Comparator<LocationModel> comparator = Comparators.getLocationComparator(LocationModel::getLongitude);
        LocationModel mostEastern = Collections.max(locationRepository.getLocations().getValue(), comparator);

        focusedLocation.setValue(mostEastern);
    }

    public void setMostWesternPosition() {
        Comparator<LocationModel> comparator = Comparators.getLocationComparator(LocationModel::getLongitude);
        LocationModel mostWestern = Collections.min(locationRepository.getLocations().getValue(), comparator);

        focusedLocation.setValue(mostWestern);
    }

    public void setMostNorthernPosition() {
        Comparator<LocationModel> comparator = Comparators.getLocationComparator(LocationModel::getLatitude);
        LocationModel mostNorthern = Collections.max(locationRepository.getLocations().getValue(), comparator);

        focusedLocation.setValue(mostNorthern);
    }

    public void setMostSouthernPosition() {
        Comparator<LocationModel> comparator = Comparators.getLocationComparator(LocationModel::getLatitude);
        LocationModel mostSouthern = Collections.min(locationRepository.getLocations().getValue(), comparator);

        focusedLocation.setValue(mostSouthern);
    }
}