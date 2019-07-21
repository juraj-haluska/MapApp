package net.spacive.mapapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import net.spacive.mapapp.model.LocationModel;
import net.spacive.mapapp.repository.FakeLocationRepository;
import net.spacive.mapapp.repository.LocationRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private LocationRepository locationRepository;

    public MapViewModel() {
        locationRepository = FakeLocationRepository.getInstance();
    }

    public List<LocationModel> getLocations() {
        return locationRepository.getAllLocations();
    }

    public LiveData<LocationModel> getCurrentLocation() {
        return locationRepository.getCurrentLocation();
    }


}