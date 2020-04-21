package com.example.butymovamoneytracker.data.db;

import android.content.Context;
import androidx.room.Room;

public class AppDatabaseInitializer {

    public Database initDatabase(Context context){
        AppDatabase appDatabase = Room.databaseBuilder(context, AppDatabase.class, "ButymovaMoneyTracker.db").build();
        return new DatabaseImpl(appDatabase);
    }
}
