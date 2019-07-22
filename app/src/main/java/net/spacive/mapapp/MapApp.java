package net.spacive.mapapp;

import android.app.Application;

import androidx.room.Room;

import net.spacive.mapapp.repository.AppDatabase;

public class MapApp extends Application {

    public static final String DATABASE_NAME = "MAP_APP_DB";
    private static AppDatabase appDatabase;

    // single instance of appDatabase
    public AppDatabase getAppDatabase() {
        if (appDatabase == null) {

            appDatabase = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME).build();
        }

        return appDatabase;
    }
}
