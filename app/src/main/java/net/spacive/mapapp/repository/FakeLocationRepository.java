package net.spacive.mapapp.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.spacive.mapapp.model.LocationModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FakeLocationRepository implements LocationRepository {

    private static LocationRepository self;

    private boolean streamStarted = false;

    private List<LocationModel> locationCollection = new ArrayList<>();
    private MutableLiveData<List<LocationModel>> locations = new MutableLiveData<>();

    private FakeLocationRepository() {

        locationCollection.add(new LocationModel(Double.MIN_NORMAL, 0, new Date(Calendar.getInstance().getTimeInMillis()), "", 5));
        locationCollection.add(new LocationModel(0, 1, new Date(Calendar.getInstance().getTimeInMillis()), "", 5));
        locationCollection.add(new LocationModel(0, 2, new Date(Calendar.getInstance().getTimeInMillis()), "", 5));
        locationCollection.add(new LocationModel(0, 3, new Date(Calendar.getInstance().getTimeInMillis()), "", 5));
        locationCollection.add(new LocationModel(0, 4, new Date(Calendar.getInstance().getTimeInMillis()), "", 5));

        locations.setValue(locationCollection);

        startDataStream();
    }

    public static LocationRepository getInstance() {
        if (self == null) {
            self = new FakeLocationRepository();
        }

        return self;
    }

    @Override
    public LiveData<List<LocationModel>> getLocations() {
        return locations;
    }

    private void startDataStream() {
        if (streamStarted) {
            return;
        }

        streamStarted = true;

        new AsyncTask<Void, LocationModel, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.sleep(1000);
                        LocationModel newModel = new LocationModel(
                                i,
                                i,
                                new Date(Calendar.getInstance().getTimeInMillis()),
                                "zdroj: " + i,
                                5
                        );
                        publishProgress(newModel);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(LocationModel... values) {
                locationCollection.add(values[0]);
                locations.postValue(locationCollection);
            }
        }.execute();
    }
}
