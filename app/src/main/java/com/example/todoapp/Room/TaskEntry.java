package com.example.todoapp.Room;


import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "taskentry")
public class TaskEntry {
    @PrimaryKey(autoGenerate = true)
    @NonNull public int uid;

    public String name;
    public String description;
    public Date date;
    public String startTime;

    public TaskEntry(String name, String description, Date date, String startTime){
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.date = date;
    }

    public int getUid(){
        return uid;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getStartTime(){
        return startTime;
    }
    public Date getDate() {
        return date;
    }
}


