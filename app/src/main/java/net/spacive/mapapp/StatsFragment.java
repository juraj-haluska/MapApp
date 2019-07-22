package net.spacive.mapapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.spacive.mapapp.repository.LocationRepository;

public class StatsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_stats, container, false);

        view.findViewById(R.id.button_clear).setOnClickListener(button -> {
            LocationRepository repo = ((MapApp) getActivity().getApplication())
                    .getAppDatabase().locationDao();

            new Thread(() -> repo.clearData()).start();
        });

        return view;
    }
}
