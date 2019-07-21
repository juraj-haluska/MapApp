package net.spacive.mapapp.util;

import androidx.arch.core.util.Function;

import net.spacive.mapapp.model.LocationModel;

import java.util.Comparator;

public class Comparators {
    public static Comparator<LocationModel> getLocationComparator(Function<LocationModel, Double> getter) {
        return (locationModel, t) -> {
            if (getter.apply(locationModel) < getter.apply(t)) return -1;
            if (getter.apply(locationModel) > getter.apply(t)) return 1;
            return 0;
        };
    }
}
