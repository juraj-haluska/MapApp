package net.spacive.mapapp;

import android.content.Context;
import android.content.SharedPreferences;
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

    public static final float DEFAULT_ZOOM = 18f;

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

        googleMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));

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
        // remove old markers
        for (Marker marker : markers) {
            marker.remove();
        }

        markers = new ArrayList<>();

        for (LocationModel locationModel : locations) {
            MarkerOptions options = new MarkerOptions().position(locationModel.getLatLng());
            Marker marker = googleMap.addMarker(options);
            marker.setTag(locationModel);
            markers.add(marker);
        }

        if (!locations.isEmpty()) {
            changeFocusToLocation(locations.get(locations.size() - 1));
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

            configurePolylineOptions(polylineOptions);

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

    private void configurePolylineOptions(PolylineOptions polylineOptions) {
        // set polyline width
        SharedPreferences shp = getContext().getSharedPreferences(
                getString(R.string.prefs_key),
                Context.MODE_PRIVATE);

        int width = shp.getInt(
                getString(R.string.prefs_key_width),
                getResources().getInteger(R.integer.default_line_width));

        float divider = getResources().getFloat(R.dimen.map_line_factor);
        int bias = getResources().getInteger(R.integer.map_line_bias);

        // map width values 0-100 from seekbar to 5-50
        int lineWidth = (int) (width / divider + bias);

        int color = shp.getInt(
                getString(R.string.prefs_key_color),
                getResources().getColor(R.color.default_line_color));

        polylineOptions.width(lineWidth);
        polylineOptions.color(color);
    }
}
