package com.example.butymovamoneytracker.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ItemEntity.class}, version = 1, exportSchema = false)
abstract class AppDatabase extends RoomDatabase {
    abstract ItemDao itemDao();
}
