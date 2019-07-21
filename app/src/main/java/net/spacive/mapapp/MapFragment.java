package net.spacive.mapapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import net.spacive.mapapp.model.LocationModel;
import net.spacive.mapapp.viewmodel.MapViewModel;

public class MapFragment extends SupportMapFragment {

    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this::onMapLoaded);
    }

    private void onMapLoaded(GoogleMap googleMap) {

        this.googleMap = googleMap;

        MapViewModel viewModel = ViewModelProviders.of(getActivity()).get(MapViewModel.class);

        for (LocationModel location : viewModel.getLocations()) {
            addMarker(location);
        }

        viewModel.getCurrentLocation().observe(getViewLifecycleOwner(), this::addMarker);

        googleMap.setOnMarkerClickListener(marker -> {
            LocationModel location = (LocationModel) marker.getTag();
            if (location != null) {
                showBottomSheet(location);
                return true;
            }
            return false;
        });
    }

    private void addMarker(LocationModel location) {
        if (googleMap != null) {

            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()));

            Marker marker = googleMap.addMarker(options);
            marker.setTag(location);
        }
    }

    private void showBottomSheet(LocationModel location) {
        Snackbar.make(getView(), location.getLatitude() + "/" + location.getLongitude(), Snackbar.LENGTH_SHORT).show();
    }
}
