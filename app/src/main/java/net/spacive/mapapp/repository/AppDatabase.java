package net.spacive.mapapp.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import net.spacive.mapapp.model.LocationModel;
import net.spacive.mapapp.util.Converters;

@Database(entities = {LocationModel.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
}
