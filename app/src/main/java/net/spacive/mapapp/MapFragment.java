package net.spacive.mapapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import net.spacive.mapapp.model.LocationModel;
import net.spacive.mapapp.view.LocationDetailDialog;
import net.spacive.mapapp.viewmodel.LocationDetailViewModel;
import net.spacive.mapapp.viewmodel.MapViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends SupportMapFragment {

    private GoogleMap googleMap;
    private MapViewModel viewModel;
    private List<Marker> markers = new ArrayList<>();
    private Polyline polyline;


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View mapView = super.onCreateView(layoutInflater, viewGroup, bundle);

        RelativeLayout view = new RelativeLayout(getActivity());
        view.addView(mapView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        RelativeLayout.LayoutParams arrowLayoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        arrowLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        arrowLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);

        View arrows = layoutInflater.inflate(R.layout.layout_arrows, viewGroup, false);
        view.addView(arrows, arrowLayoutParams);

        attachArrowClickListeners(arrows);

        return view;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this::onMapLoaded);
    }

    private void onMapLoaded(GoogleMap googleMap) {
        this.googleMap = googleMap;

        setMarkerClickListener();

        viewModel = ViewModelProviders.of(getActivity()).get(MapViewModel.class);

        viewModel.getLocations().observe(getViewLifecycleOwner(), this::updateLocations);
        viewModel.getFocusedLocation().observe(getViewLifecycleOwner(), this::changeFocusToLocation);
    }

    private void setMarkerClickListener() {
        googleMap.setOnMarkerClickListener(marker -> {
            LocationModel location = (LocationModel) marker.getTag();
            if (location != null) {
                showBottomSheet(location);
                return true;
            }
            return false;
        });
    }

    private void updateLocations(List<LocationModel> locations) {
        if (googleMap != null) {
            List<LatLng> latLngs = new ArrayList<>();
            for (LocationModel location : locations) {
                latLngs.add(location.getLatLng());
            }

            updateMarkers(locations);
            updatePolyline(latLngs);
        }
    }

    private void updateMarkers(List<LocationModel> locations) {
        for (LocationModel locationModel : locations) {
            // check if marker of this location is in map
            boolean contains = false;
            for (Marker marker : markers) {
                if (locationModel.getLatLng().equals(marker.getPosition())) {
                    contains = true;
                }
            }

            if (!contains) {
                MarkerOptions options = new MarkerOptions().position(locationModel.getLatLng());
                Marker marker = googleMap.addMarker(options);
                marker.setTag(locationModel);
                markers.add(marker);
            }
        }
    }

    private void updatePolyline(List<LatLng> latLngs) {
        if (polyline == null) {
            // generate polyline options
            PolylineOptions polylineOptions = new PolylineOptions();
            for (LatLng latLng : latLngs) {
                // add polyline
                polylineOptions.add(latLng);
            }

            polyline = googleMap.addPolyline(polylineOptions);
        } else {
            polyline.setPoints(latLngs);
        }
    }

    private void showBottomSheet(LocationModel location) {
        LocationDetailViewModel viewModel = ViewModelProviders.of(getActivity()).get(LocationDetailViewModel.class);
        viewModel.setLocationModel(location);
        new LocationDetailDialog().show(getFragmentManager(), LocationDetailDialog.class.getName());
    }

    private void attachArrowClickListeners(View view) {
        view.findViewById(R.id.arrowLeft).setOnClickListener(v -> viewModel.setMostWesternPosition());
        view.findViewById(R.id.arrowRight).setOnClickListener(v -> viewModel.setMostEasternPosition());
        view.findViewById(R.id.arrowUp).setOnClickListener(v -> viewModel.setMostNorthernPosition());
        view.findViewById(R.id.arrowDown).setOnClickListener(v -> viewModel.setMostSouthernPosition());
    }

    private void changeFocusToLocation(LocationModel location) {
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
            googleMap.moveCamera(cameraUpdate);
        }
    }
}
