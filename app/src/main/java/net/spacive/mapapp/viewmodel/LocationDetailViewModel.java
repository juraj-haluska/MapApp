package net.spacive.mapapp.viewmodel;

import androidx.lifecycle.ViewModel;

import net.spacive.mapapp.model.LocationModel;

public class LocationDetailViewModel extends ViewModel {

    private LocationModel locationModel;

    public LocationModel getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
    }
}
