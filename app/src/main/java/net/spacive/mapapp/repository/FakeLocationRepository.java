package net.spacive.mapapp.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.spacive.mapapp.model.LocationModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FakeLocationRepository implements LocationRepository {

    private static LocationRepository self;

    private boolean streamStarted = false;

    private List<LocationModel> locationCollection = new ArrayList<>();
    private MutableLiveData<List<LocationModel>> locations = new MutableLiveData<>();

    private FakeLocationRepository() {
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
                Random r = new Random();
                for (int i = 0; i < 10000; i++) {
                    try {
                        Thread.sleep(1000);
                        LocationModel newModel = new LocationModel(
                                r.nextDouble() * 90,
                                r.nextDouble() * 180,
                                new Date(Calendar.getInstance().getTimeInMillis()),
                                "zdroj: " + r.nextInt(),
                                r.nextInt()
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

    @Override
    public void insertLocation(LocationModel locationModel) {

    }

    @Override
    public List<LocationModel> getLocationsSync() {
        return new ArrayList<>();
    }
}
