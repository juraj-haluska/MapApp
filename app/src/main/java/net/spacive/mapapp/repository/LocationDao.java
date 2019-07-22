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

    @Insert
    void insertLocation(LocationModel locationModel);
}
