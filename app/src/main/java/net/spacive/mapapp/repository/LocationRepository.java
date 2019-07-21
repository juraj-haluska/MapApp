package net.spacive.mapapp.repository;

import androidx.lifecycle.LiveData;

import net.spacive.mapapp.model.LocationModel;

import java.util.List;

public interface LocationRepository {
    LiveData<LocationModel> getCurrentLocation();

    List<LocationModel> getAllLocations();
}
