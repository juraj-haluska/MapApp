package net.spacive.mapapp.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import net.spacive.mapapp.model.LocationModel;

import java.util.List;

@Dao
public interface LocationDao extends LocationRepository{
    @Query("SELECT * FROM locations")
    LiveData<List<LocationModel>> getLocations();

    @Query("SELECT * FROM locations")
    List<LocationModel> getLocationsSync();

    @Insert
    void insertLocation(LocationModel locationModel);
}
