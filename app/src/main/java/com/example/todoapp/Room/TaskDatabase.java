package com.example.todoapp.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {TaskEntry.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class TaskDatabase extends RoomDatabase {
    private static TaskDatabase minstance;
    private static final String DB_NAME = "task_db";

    public abstract TaskDAO getTaskDAO();

    public static synchronized TaskDatabase getInstance(Context ctx) { if(minstance == null) {
        minstance = Room.databaseBuilder(ctx.getApplicationContext(), TaskDatabase.class,
                DB_NAME)
                .fallbackToDestructiveMigration()
                .build(); }
        return minstance;
    }
}
