package net.spacive.mapapp.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import net.spacive.mapapp.model.LocationModel;

import java.util.List;

@Dao
public interface LocationDao extends LocationRepository{
    @Override
    @Query("SELECT * FROM locations")
    LiveData<List<LocationModel>> getLocations();

    @Override
    @Query("SELECT * FROM locations")
    List<LocationModel> getLocationsSync();

    @Override
    @Insert
    void insertLocation(LocationModel locationModel);

    @Override
    @Query("DELETE FROM locations")
    void clearData();
}
