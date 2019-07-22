package net.spacive.mapapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.android.colorpicker.ColorPickerDialog;

public class SettingsFragment extends Fragment {

    private SharedPreferences shp;
    private SeekBar seekBar;
    private View lineColorLayout;
    private View lineColor;
    private RadioGroup radioGroupRates;
    private int selectedColor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        seekBar = view.findViewById(R.id.seekbarLineWidth);
        lineColorLayout = view.findViewById(R.id.viewLineColor);
        lineColor = view.findViewById(R.id.view_line);
        radioGroupRates = view.findViewById(R.id.radio_rates);

        shp = getContext().getSharedPreferences(
                getString(R.string.prefs_key),
                Context.MODE_PRIVATE);

        setListeners();
        initializeValues();

        return view;
    }

    private void setListeners() {
        setSeekBarListener(seekBar);
        lineColorLayout.setOnClickListener(v -> openSelectColorDialog());
        radioGroupRates.setOnCheckedChangeListener((radioGroup, id) -> {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                if (radioGroup.getChildAt(i).getId() == id) {
                    shp.edit()
                            .putInt(getString(R.string.prefs_key_rate), i)
                            .apply();
                }
            }
        });
    }

    private void initializeValues() {
        int width = shp.getInt(
                getString(R.string.prefs_key_width),
                getResources().getInteger(R.integer.default_line_width));

        seekBar.setProgress(width);

        selectedColor = shp.getInt(
                getString(R.string.prefs_key_color),
                getResources().getColor(R.color.default_line_color));

        lineColor.setBackgroundColor(selectedColor);

        int selectedRate = shp.getInt(
                getString(R.string.prefs_key_rate),
                getResources().getInteger(R.integer.default_sampling_rate)
        );

        int checkedId = radioGroupRates.getChildAt(selectedRate).getId();
        radioGroupRates.check(checkedId);
    }

    private void setSeekBarListener(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                shp.edit()
                        .putInt(getString(R.string.prefs_key_width),
                                seekBar.getProgress())
                        .apply();
            }
        });
    }

    private void openSelectColorDialog() {
        int[] palette = getResources().getIntArray(R.array.color_palette);

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        colorPickerDialog.initialize(
                R.string.title_colorpicker,
                palette,
                selectedColor,
                getResources().getInteger(R.integer.palette_columns),
                palette.length
        );

        colorPickerDialog.setOnColorSelectedListener(color -> {
            selectedColor = color;
            lineColor.setBackgroundColor(color);
            shp.edit()
                    .putInt(getString(R.string.prefs_key_color), color)
                    .apply();
        });

        colorPickerDialog.show(getFragmentManager(), ColorPickerDialog.class.getName());
    }
}
