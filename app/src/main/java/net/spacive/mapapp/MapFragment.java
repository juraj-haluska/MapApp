package net.spacive.mapapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

    private void attachArrowClickListeners(View view) {
        view.findViewById(R.id.arrowLeft).setOnClickListener(view1 -> Log.d("CLICK", "LEFT"));
        view.findViewById(R.id.arrowRight).setOnClickListener(view1 -> Log.d("CLICK", "RIGHT"));
        view.findViewById(R.id.arrowUp).setOnClickListener(view1 -> Log.d("CLICK", "UP"));
        view.findViewById(R.id.arrowDown).setOnClickListener(view1 -> Log.d("CLICK", "DOWN"));
    }
}
