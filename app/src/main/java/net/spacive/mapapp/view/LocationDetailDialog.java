package net.spacive.mapapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import static android.text.format.DateFormat.getTimeFormat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.spacive.mapapp.R;
import net.spacive.mapapp.databinding.BottomSheetMarkerInfoBinding;
import net.spacive.mapapp.model.LocationModel;
import net.spacive.mapapp.viewmodel.LocationDetailViewModel;

import java.text.Format;

public class LocationDetailDialog extends BottomSheetDialogFragment {

    private LocationModel locationModel;
    private Format timeFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomSheetMarkerInfoBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.bottom_sheet_marker_info,
                container,
                false);

        LocationDetailViewModel viewModel = ViewModelProviders.of(getActivity()).get(LocationDetailViewModel.class);
        locationModel = viewModel.getLocationModel();

        timeFormat = getTimeFormat(getContext());

        binding.setLocation(locationModel);
        binding.setFormat(timeFormat);

        return binding.getRoot();
    }
}
